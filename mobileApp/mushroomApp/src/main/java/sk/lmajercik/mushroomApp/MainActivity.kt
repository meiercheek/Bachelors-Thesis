
package sk.lmajercik.mushroomApp

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.fotoapparat.Fotoapparat
import io.fotoapparat.configuration.CameraConfiguration
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.selector.back
import io.fotoapparat.selector.front
import io.fotoapparat.selector.off
import io.fotoapparat.selector.torch
import io.fotoapparat.view.CameraView
import sk.lmajercik.mushroomApp.ml.Dense121
import sk.lmajercik.mushroomApp.util.Mushroom
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.model.Model
import sk.lmajercik.mushroomApp.util.Analysis
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.reflect.Type
import java.text.DateFormat
import java.util.*


enum class CameraState{
    FRONT, BACK
}

enum class FlashState{
    ON, OFF
}

enum class FotoapparatState{
    ON, OFF
}

private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
private val FILE_PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
var GPU_SUPPORT = false
const val EXTRA_MESSAGE = "org.tensorflow.lite.examples.classification.RESULTS"
const val EXTRA_PIC = "org.tensorflow.lite.examples.classification.PICTURE"
const val EXTRA_PERM = "org.tensorflow.lite.examples.classification.PERM"


class MainActivity : AppCompatActivity() {
    private var fotoapparat: Fotoapparat? = null
    private var fotoapparatState : FotoapparatState? = null
    private var cameraStatus : CameraState? = null
    private var flashState: FlashState? = null
    private var model: Dense121? = null
    private lateinit var fabFlash: FloatingActionButton
    private var alert11: androidx.appcompat.app.AlertDialog?  = null
    private val buttons: MutableList<FloatingActionButton> = mutableListOf()


    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1000)
    }

    private fun requestPermission(perm: Array<String>){
        ActivityCompat.requestPermissions(this, perm,0)
    }

    private fun hasNoPermissions(perm: String): Boolean{
        return ContextCompat.checkSelfPermission(this,
            perm) != PackageManager.PERMISSION_GRANTED
    }

    override fun onStart() {
        super.onStart()

        if (!hasNoPermissions(Manifest.permission.CAMERA)) {
            fotoapparat?.start()
            fotoapparatState = FotoapparatState.ON
        }
    }

    override fun onStop() {
        super.onStop()
        fotoapparat?.stop()
        FotoapparatState.OFF
    }

    override fun onResume() {
        super.onResume()

        if (!hasNoPermissions(Manifest.permission.CAMERA)) {
            fotoapparat?.start()
            fotoapparatState = FotoapparatState.ON
            for(b in buttons){
                b.show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        val editor = getSharedPreferences("warning", MODE_PRIVATE).edit()
        editor.putBoolean("showWarning", false)
        editor.apply()

        model?.close()
    }

    private fun intializeFotoapparat(){
        createFotoapparat()
        fotoapparat?.start()
        fotoapparatState = FotoapparatState.ON
    }

    private fun makeErrorDialog(){
        val builder1: MaterialAlertDialogBuilder = MaterialAlertDialogBuilder(this)

        builder1.setIcon(R.drawable.ic_outline_info_24)
        builder1.setTitle(R.string.noresult)

        builder1.setMessage(R.string.recommend)
        builder1.setCancelable(false)

        builder1.setPositiveButton(
            R.string.continue_string,
            DialogInterface.OnClickListener { dialog, _ ->
                dialog.cancel()
            })
        builder1.setNegativeButton(
            R.string.help,
            DialogInterface.OnClickListener { dialog, _ ->
                val intent = Intent(this, Tips::class.java)
                startActivity(intent)
            })



        alert11 = builder1.create()
        alert11?.show()
    }

    private fun makeWarning(){
        val builder1: MaterialAlertDialogBuilder = MaterialAlertDialogBuilder(this)

        builder1.setIcon(android.R.drawable.ic_dialog_alert)

        builder1.setTitle(R.string.warning)

        builder1.setMessage(R.string.purpose)
        builder1.setCancelable(false)

        builder1.setPositiveButton(
            R.string.continue_string,
            DialogInterface.OnClickListener { dialog, _ ->
                dialog.cancel()
                if (hasNoPermissions(Manifest.permission.CAMERA)) {
                    requestPermission(REQUIRED_PERMISSIONS)
                }
                else{
                    intializeFotoapparat()
                }
            })


        alert11 = builder1.create()
        alert11?.show()

        val editor = getSharedPreferences("warning", MODE_PRIVATE).edit()
        editor.putBoolean("showWarning", false)
        editor.apply()

        //prefs.edit().putBoolean("isFirstRun", false).apply()
    }

    private var mLoading: View? = null

    private fun showLoading() {
        if (mLoading != null) {
            mLoading!!.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        if (mLoading != null) {
            mLoading!!.visibility = View.GONE
        }
    }


    /**
     * Checks the device's GPU configuration and builds the model accordingly
     * If supported, GPU inference is enabled, otherwise 4 CPU threads are allocated for inference
     *
     * @return model
     */
    private fun buildModel(): Dense121 {
        val compatList = CompatibilityList()

        val options = if(compatList.isDelegateSupportedOnThisDevice) {
            GPU_SUPPORT = true
            Model.Options.Builder().setDevice(Model.Device.GPU).build()
        } else {
            Model.Options.Builder().setNumThreads(4).build()
        }

        return Dense121.newInstance(this, options)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        cameraStatus = CameraState.BACK
        flashState = FlashState.OFF
        fotoapparatState = FotoapparatState.OFF

        mLoading = findViewById(R.id.my_loading_layout)
        hideLoading()

        model = buildModel()

        val warningPref = getSharedPreferences("warning", MODE_PRIVATE).getBoolean("showWarning", true)

        if(warningPref) {
            makeWarning()
        }
        else{
            if (hasNoPermissions(Manifest.permission.CAMERA)) {
                requestPermission(REQUIRED_PERMISSIONS)
            }
            else{
                intializeFotoapparat()
            }
        }


        val fabTakepic = findViewById<FloatingActionButton>(R.id.fab_takepic)
        val fabFlip = findViewById<FloatingActionButton>(R.id.fab_flip)
        val fabAbout = findViewById<FloatingActionButton>(R.id.fab_about)
        val fabHistory = findViewById<FloatingActionButton>(R.id.fab_history)
        val fabGalleryPick = findViewById<FloatingActionButton>(R.id.fab_gallerypick)
        fabFlash = findViewById(R.id.fab_flash)
        buttons.add(fabAbout)
        buttons.add(fabFlip)
        buttons.add(fabTakepic)
        buttons.add(fabHistory)
        buttons.add(fabGalleryPick)
        buttons.add(fabFlash)


        fabTakepic.setOnClickListener {
            takePhoto()
        }

        fabFlip.setOnClickListener {
            switchCamera(fabFlash)
        }

        fabAbout.setOnClickListener {
            val intent = Intent(this, About::class.java)
            startActivity(intent)
        }

        fabHistory.setOnClickListener {
            val intent = Intent(this, History::class.java)
            startActivity(intent)
        }


        fabGalleryPick.setOnClickListener {
            if (hasNoPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                requestPermission(FILE_PERMISSIONS)
            }
            else{
                chooseImageGallery()
            }

        }

        fabFlash.setOnClickListener {
            changeFlashState()
        }


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            0 -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        if(permissions[0] == "android.permission.READ_EXTERNAL_STORAGE"){
                            chooseImageGallery()
                        }
                        else if (permissions[0] == "android.permission.CAMERA"){
                            intializeFotoapparat()
                        }
                }
                else{
                    requestPermission(REQUIRED_PERMISSIONS)
                }
            }
        }
    }

    /**
     * Triggers as the album/gallery returns a picked image
     *
     * @param requestCode
     * @param resultCode
     * @param data received data in a form of Intent
     *
     * @see analyse
     */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 1000 && resultCode == RESULT_OK){
            val imageUri: Uri? = data?.data
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            val now = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time).filter { !it.isWhitespace() }

            val fname = "mushroomApp-gallery-".plus(now.plus(".jpg"))

            try {
                val f = File(getExternalFilesDir("photos"), fname)
                val fhandler = FileOutputStream(f).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)
                    out.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            showLoading()
            analyse(bitmap, fname)
        }
        else {
            super.onActivityResult(requestCode, resultCode, data)
        }


    }

    private fun switchCamera(fab_flash: FloatingActionButton) {
        fotoapparat?.switchTo(
            lensPosition =  if (cameraStatus == CameraState.BACK) front() else back(),
            cameraConfiguration = CameraConfiguration()
        )
        flashState = FlashState.OFF
        if(cameraStatus == CameraState.BACK) {
            cameraStatus = CameraState.FRONT
            fab_flash.hide()
        }
        else {
            cameraStatus = CameraState.BACK
            fab_flash.show()
        }
    }

    private fun createFotoapparat(){
        val cameraView = findViewById<CameraView>(R.id.camera_view)

        fotoapparat = Fotoapparat(
            context = this,
            view = cameraView,
            scaleType = ScaleType.CenterCrop,
            lensPosition = back(),
            cameraErrorCallback = { error ->
                println("Recorder errors: $error")
            }
        )
    }

    private fun changeFlashState() {
        fotoapparat?.updateConfiguration(
            CameraConfiguration(
                flashMode = if(flashState == FlashState.ON) off() else torch()
            )
        )

        if(flashState == FlashState.ON){
            flashState = FlashState.OFF
            this.fabFlash.setImageResource(R.drawable.flash_on)
        }

        else {
            flashState = FlashState.ON
            this.fabFlash.setImageResource(R.drawable.flash_off)
        }
    }

    /**
     * Rotates the image with basic matrix operations
     *
     * @param source The source image to be rotated
     * @param angle The angle it should be rotated at
     *
     * @see takePhoto
     *
     * @return Rotated Bitmap
     */

    private fun rotateImage(source: Bitmap, angle: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }

    /**
     * Takes the photo and writes it into private app storage
     * Also properly rotates the picture based on data from Fotoapparat.
     * The compression is 50%, saved in JPEG format.
     * Finally, gets passed into the analyse method
     * @see analyse
     */

    private fun takePhoto() {
        if (hasNoPermissions(Manifest.permission.CAMERA)) {
            requestPermission(REQUIRED_PERMISSIONS)
        }else{
            showLoading()
            for(b in buttons){
                b.hide()
            }
            val result = fotoapparat?.takePicture()
            val now = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time).filter { !it.isWhitespace() }

            val fname = "mushroomApp-camera-".plus(now.plus(".jpg"))

            result?.toBitmap()
                ?.whenAvailable { photo ->
                    photo
                        ?.let { it ->
                            val rotated: Bitmap = when (it.rotationDegrees) {
                                90 -> rotateImage(it.bitmap, 270)
                                180 -> rotateImage(it.bitmap, 180)
                                270 -> rotateImage(it.bitmap, 90)
                                0 -> it.bitmap
                                else -> it.bitmap
                            }

                            analyse(rotated, fname)

                            try {
                                val f = File(getExternalFilesDir("photos"), fname)
                                val fhandler = FileOutputStream(f).use { out ->
                                    rotated.compress(Bitmap.CompressFormat.JPEG, 50, out)
                                    out.close()
                                }
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                }
        }
    }

    /**
     * Method that inferes the result based on bitmap
     * and writes the Analysis to storage
     *
     * Finally, opens ItemDetail when inference is finished
     *
     * If inference results were not sufficient, modal is displayed
     *
     * @param bmap Bitmap, preferable rotated into portrait mode
     * @param fname the filename of saved image, for packaging to Analysis
     */
    private fun analyse(bmap: Bitmap, fname: String){
        var counter = 0
        var ans: MutableList<Analysis> = mutableListOf()

        val image = TensorImage.fromBitmap(bmap)
        val outputs = model?.process(image)

        val probability = outputs?.probabilityAsCategoryList?.apply {
            sortByDescending { it.score }
        }?.take(3)


        val topThree: MutableList<Mushroom> = mutableListOf()
        probability?.forEach{
                i ->
            if (i.score*100 <= 55){
                counter++
            }
            topThree.add(Mushroom(i.label.replace('-', '_'), i.score.toString(), "unknown", "desc","image", "", "", "", ""))

        }
        if (counter == 3){
            hideLoading()
            makeErrorDialog()
            for(b in buttons){
                b.show()
            }
            return
        }
        val arr = topThree.toTypedArray()

        val session = "historyPicsList"

        // get old list from json and append to it and store it into json again
        val jsonOldList = getSharedPreferences("history", MODE_PRIVATE).getString(session, null)
        val type: Type = object : TypeToken<MutableList<Analysis?>?>() {}.type
        try {
            ans = Gson().fromJson(jsonOldList, type)
        }catch (e: NullPointerException){
            println(e)
        }

        val an = Analysis(Calendar.getInstance().time, fname, arr)
        ans.add(an)

        val editor = getSharedPreferences("history", MODE_PRIVATE).edit()
        val connectionsJSONString = Gson().toJson(ans, type)
        editor.putString(session, connectionsJSONString);
        editor.apply()

        hideLoading()
        val intent = Intent(this, ItemDetail::class.java).apply {
            putExtra(EXTRA_MESSAGE, arr)
            putExtra(EXTRA_PIC, fname)
        }
        startActivity(intent)
    }
}


