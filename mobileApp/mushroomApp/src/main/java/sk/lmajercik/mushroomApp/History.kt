package sk.lmajercik.mushroomApp

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import sk.lmajercik.mushroomApp.util.Analysis
import java.lang.NullPointerException
import java.lang.reflect.Type


class History : AppCompatActivity() {
    private var alert11: androidx.appcompat.app.AlertDialog?  = null
    private var mEmpty: View? = null
    private var recyclerView: View? = null

    private fun showEmpty() {
        if (mEmpty != null) {
            mEmpty!!.visibility = View.VISIBLE
            recyclerView!!.visibility = View.GONE
        }
    }

    private fun hideEmpty() {
        if (mEmpty != null) {
            mEmpty!!.visibility = View.GONE
            recyclerView!!.visibility = View.VISIBLE
        }
    }

    private fun makeDialog(){
        val builder1: MaterialAlertDialogBuilder = MaterialAlertDialogBuilder(this)

        builder1.setTitle("Tip")

        builder1.setMessage(R.string.historyTip)
        builder1.setCancelable(true)

        builder1.setPositiveButton(
            R.string.close_string,
            DialogInterface.OnClickListener { dialog, _ ->
                dialog.cancel() })

        alert11 = builder1.create()
        alert11?.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        var ans: MutableList<Analysis> = mutableListOf()
        val session = "historyPicsList"
        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        mEmpty = findViewById(R.id.customEmptyScreen)

        hideEmpty()

        // get old list from json and append to it and store it into json again
        val jsonOldList = getSharedPreferences("history", MODE_PRIVATE).getString(session, null)
        val type: Type = object : TypeToken<MutableList<Analysis?>?>() {}.type

        val warningPref = getSharedPreferences("historyWarning", MODE_PRIVATE).getBoolean("showHistoryWarning", true)

        try {
            ans = Gson().fromJson(jsonOldList, type)


            val reverse: MutableList<Analysis> = ans.reversed() as MutableList<Analysis>
            (recyclerView as RecyclerView?)?.adapter = HistoryItem(this, reverse)

            (recyclerView as RecyclerView?)?.setHasFixedSize(true)
            (recyclerView as RecyclerView?)?.setItemViewCacheSize(20)

            if(warningPref){
                makeDialog()
                val editor = getSharedPreferences("historyWarning", MODE_PRIVATE).edit()
                editor.putBoolean("showHistoryWarning", false)
                editor.apply()
            }

        }catch (e: NullPointerException){
            showEmpty()
        }
    }
}