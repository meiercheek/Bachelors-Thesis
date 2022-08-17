package sk.lmajercik.mushroomApp


import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.switchmaterial.SwitchMaterial
import sk.lmajercik.mushroomApp.util.Helper
import sk.lmajercik.mushroomApp.util.Mushroom
import java.io.File
import java.util.*


class ItemDetail : AppCompatActivity() {

    private var imageView: ImageView? = null
    private var takenBitmap: Bitmap? = null
    private var imgFile: File? = null
    private var alert11: androidx.appcompat.app.AlertDialog?  = null
    private var author: String? = null
    private var licenseLink: String? = null
    private var imageLink: String? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId

        if (id == R.id.license) {
            makeDialog(author, imageLink, licenseLink)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun makeDialog(author: String?, imageLink: String?, licenseLink: String? ){
        val builder1: MaterialAlertDialogBuilder = MaterialAlertDialogBuilder(this)

        builder1.setTitle(R.string.license)

        var licenseName: String? = null

        if(licenseLink == "https://creativecommons.org/licenses/by-sa/3.0/deed.en"){
            licenseName = "CC BY-SA 3.0"
        }
        else if(licenseLink == "https://creativecommons.org/licenses/by-sa/4.0/deed.en"){
            licenseName = "CC BY-SA 4.0"
        }
        else{
            licenseName = "Public domain"
        }

        val builtText = "Photo: ".plus(author).plus(" / ")
            .plus(licenseName).plus("\n\n").plus(getString(R.string.articleLicense))


        builder1.setMessage(builtText)
        builder1.setCancelable(true)

        builder1.setNeutralButton(R.string.view_image) { dialog, _ ->
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(imageLink))
            startActivity(browserIntent)
        }

        builder1.setPositiveButton(
            R.string.close_string,
            DialogInterface.OnClickListener { dialog, _ ->
                dialog.cancel()
            })

        if(licenseName != "Public domain"){
            builder1.setNegativeButton(
                R.string.view_license,
                DialogInterface.OnClickListener { dialog, _ ->
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(licenseLink))
                    startActivity(browserIntent)
                })

        }



        alert11 = builder1.create()
        alert11?.show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        imageView = findViewById(R.id.imageView)

        val message = intent.getParcelableArrayExtra(EXTRA_MESSAGE)
        val picture = intent.getStringExtra(EXTRA_PIC)

        imgFile = File(this.getExternalFilesDir("photos"), picture!!)

        val top_result: Mushroom = message?.get(0) as Mushroom
        val second_result: Mushroom = message?.get(1) as Mushroom
        val third_result: Mushroom = message?.get(2) as Mushroom

        var edibility: String? = null
        var name: String? = null
        var desc: String? = null
        var pictureId: Int? = null


        val self = this

        val shrooms: List<Mushroom> = Helper.getShrooms(this)

        shrooms.forEachIndexed { _, shroom ->
            if(top_result.id == shroom.id){
                edibility = shroom.edibility
                name = shroom.id
                author = shroom.author
                licenseLink = shroom.license
                imageLink = shroom.wikiLink

                pictureId = resources.getIdentifier(
                    name?.lowercase(Locale.getDefault()), "drawable",
                    packageName
                )

             }
        }
        Glide.with(this)
            .load(pictureId!!)
            .placeholder(R.drawable.ic_baseline_photo_24)
            .error(R.drawable.ic_baseline_broken_image_24)
            .into(imageView!!)

        findViewById<TextView>(R.id.shroom_name).apply {
            val resourceId = resources.getIdentifier(
                name?.lowercase(Locale.getDefault()).plus("_name"), "string",
                packageName
            )
            //println()
            text = resources.getString(resourceId)

        }

        findViewById<TextView>(R.id.shroom_match).apply {
            text = top_result.match?.let { Helper.makeMatch(self, it) }
        }

        findViewById<TextView>(R.id.shroom_edibility).apply {
            text = edibility?.let { Helper.getEdiblity(it, context) }
        }

        findViewById<TextView>(R.id.shroom_desc).apply {
            val resourceId = resources.getIdentifier(
                name?.lowercase(Locale.getDefault()).plus("_desc"), "string",
                packageName
            )
            //println()
            text = resources.getString(resourceId)
        }
        val secondMatchText = findViewById<TextView>(R.id.second_match).apply {
            text = second_result.match?.let { Helper.makeMatch(self, it) }
        }
        val thirdMatchText = findViewById<TextView>(R.id.third_match).apply {
            text = third_result.match?.let { Helper.makeMatch(self, it) }
        }

        val second_button = findViewById<Button>(R.id.shroom_candidate2).apply {
            val resourceId = resources.getIdentifier(
                second_result.id?.lowercase(Locale.getDefault()).plus("_name"), "string",
                packageName
            )
            text = resources.getString(resourceId)
        }

        val third_button = findViewById<Button>(R.id.shroom_candidate3).apply {
            val resourceId = resources.getIdentifier(
                third_result.id?.lowercase(Locale.getDefault()).plus("_name"), "string",
                packageName
            )
            //println()
            text = resources.getString(resourceId)
        }
        var counterMatches = 0

        if(second_result.match!!.toFloat() < 0.1){
            second_button.visibility = View.GONE
            secondMatchText.visibility = View.GONE
            counterMatches++
        }

        if(third_result.match!!.toFloat() < 0.1){
            third_button.visibility = View.GONE
            thirdMatchText.visibility = View.GONE
            counterMatches++
        }


        val otherResultsText = findViewById<TextView>(R.id.textView5)

        if(counterMatches == 2){
            otherResultsText.visibility = View.GONE
        }
        if(counterMatches == 1){
            otherResultsText.text = resources.getString(R.string.onlyOneOtherResult)
        }


        val toggle_button = findViewById<SwitchMaterial>(R.id.switch1)
        toggle_button.text = resources.getString(R.string.reference)

        toggle_button.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                toggle_button.text = resources.getString(R.string.taken)
                //licenseButton.hide()
                Glide.with(this)
                    .load(imgFile!!.absolutePath)
                    .placeholder(R.drawable.ic_baseline_photo_24)
                    .error(R.drawable.ic_baseline_broken_image_24)
                    .into(imageView!!)
            } else {
                toggle_button.text = resources.getString(R.string.reference)
                //licenseButton.show()
                Glide.with(this)
                    .load(pictureId!!)
                    .placeholder(R.drawable.ic_baseline_photo_24)
                    .error(R.drawable.ic_baseline_broken_image_24)
                    .into(imageView!!)

            }
        }

        second_button.setOnClickListener {

            message[0] = second_result
            message[1] = top_result

            val intent = Intent(this, ItemDetail::class.java).apply {
                putExtra(EXTRA_MESSAGE, message)
                putExtra(EXTRA_PIC, intent.getStringExtra(EXTRA_PIC))
            }
            startActivity(intent)
        }

        third_button.setOnClickListener {

            message[0] = third_result
            message[2] = top_result

            val intent = Intent(this, ItemDetail::class.java).apply {
                putExtra(EXTRA_MESSAGE, message)
                putExtra(EXTRA_PIC, intent.getStringExtra(EXTRA_PIC))
            }
            startActivity(intent)
        }
    }
}