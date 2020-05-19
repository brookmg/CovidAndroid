package ethiopia.covid.android.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ethiopia.covid.android.R

/**
 * Created by BrookMG on 3/7/2019 in ethiopia.covid.android.ui.adapters
 * inside the project CovidEt .
 */
class ImageListRecyclerAdapter
    constructor(
        private val images: List<String>,
        private val onImageItemClicked: OnImageItemClicked? = null
    ) : RecyclerView.Adapter<ImageListRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
            )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.imageView).load(images[position]).into(holder.imageView)
        holder.itemView.setOnClickListener {
            onImageItemClicked?.onImageClicked(
                    holder.imageView,
                    images
            )
        }
    }

    override fun getItemCount(): Int = images.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: AppCompatImageView = itemView.findViewById(R.id.image_item)
    }

}