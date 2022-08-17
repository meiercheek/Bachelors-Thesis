package sk.lmajercik.mushroomApp.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import sk.lmajercik.mushroomApp.R
import java.io.File
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode

object Helper {

    fun getEdiblity(source: String, ctx: Context): String{
        return when (source) {
            "true" -> ctx.resources.getString(R.string.edible)
            "false" -> ctx.resources.getString(R.string.inedible)
            "inedible-cook" -> ctx.resources.getString(R.string.inediblecook)
            "poison-cook" -> ctx.resources.getString(R.string.poisoncook)
            "poison" -> ctx.resources.getString(R.string.poison)

            else -> {
                "error"
            }
        }
    }


    private fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    fun getShrooms(context: Context):List<Mushroom> {
        val jsonFileString = getJsonDataFromAsset(context, "mushrooms.json")
        //Log.i("data", jsonFileString)
        val gson = Gson()
        val listShroomType = object : TypeToken<List<Mushroom>>() {}.type
        return gson.fromJson(jsonFileString, listShroomType)
    }

    fun makeMatch(context: Context, rawMatch: String): String {
        val matchasfloat = rawMatch.toDouble() * 100
        val decimal = BigDecimal( matchasfloat).setScale(2, RoundingMode.HALF_EVEN)
        return context.getString(R.string.match).plus(" ").plus(decimal.toString().plus("%"))
    }

}