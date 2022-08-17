package sk.lmajercik.mushroomApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class About : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        findViewById<TextView>(R.id.gpuSupport).apply {
            if(GPU_SUPPORT){
                text = getString(R.string.gpuSupport)
            }
            else{
                text = getString(R.string.gpuFail)
            }

        }
    }

}