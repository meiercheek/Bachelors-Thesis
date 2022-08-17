package sk.lmajercik.mushroomApp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import sk.lmajercik.mushroomApp.util.Analysis
import sk.lmajercik.mushroomApp.util.Helper
import sk.lmajercik.mushroomApp.util.Mushroom
import java.io.File
import java.text.DateFormat
import java.util.*


class HistoryItem(
    private val context: Context,

    private val dataset: MutableList<Analysis>
) : RecyclerView.Adapter<HistoryItem.ItemViewHolder>() {


    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.item_title)
        val imgView: ImageView = view.findViewById(R.id.item_image)
        val timeView: TextView = view.findViewById(R.id.item_desc)
        val matchView: TextView = view.findViewById(R.id.item_match)
        val edibleView: TextView = view.findViewById(R.id.item_edible)
        val card: MaterialCardView = view.findViewById(R.id.card_history)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        val (time, picture, shrooms) = item
        val resourceId = context.resources.getIdentifier(
            shrooms[0].id?.lowercase(Locale.getDefault()).plus("_name"), "string",
            context.packageName
        )

        holder.textView.text = context.resources.getString(resourceId)
        holder.matchView.text = shrooms[0].match?.let { Helper.makeMatch(context, it) }

        var edibility: String = ""
        val jsonshrooms: List<Mushroom> = Helper.getShrooms(context)
        jsonshrooms.forEachIndexed { _, shroom ->
            if(shrooms[0].id == shroom.id){
                edibility = shroom.edibility.toString()
            }
        }

        edibility = Helper.getEdiblity(edibility, context)

        holder.edibleView.text = edibility
        holder.timeView.text = DateFormat.getDateTimeInstance().format(time)
        //val image: Bitmap? = ImageGetter.getTakenPicture(context,picture)
        val imgFile = File(context.getExternalFilesDir("photos"), picture)


        Glide.with(context)
            .load(imgFile.absolutePath)
            .placeholder(R.drawable.ic_baseline_photo_24)
            .error(R.drawable.ic_baseline_broken_image_24)
            .into(holder.imgView)


        holder.card.setOnClickListener {
            val intent = Intent(context, ItemDetail::class.java).apply {
                putExtra(EXTRA_MESSAGE, shrooms)
                putExtra(EXTRA_PIC, picture)
            }
            context.startActivity(intent)
        }

        holder.setIsRecyclable(true)
    }


    override fun getItemCount() = dataset.size
}