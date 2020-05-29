package ethiopia.covid.android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ethiopia.covid.android.databinding.ImageItemBinding

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
                ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(images[position])

    override fun getItemCount(): Int = images.size

    inner class ViewHolder(private val imageItemBinding: ImageItemBinding)
        : RecyclerView.ViewHolder(imageItemBinding.root) {

        fun bind(item: String) {
            Glide.with(imageItemBinding.imageItem).load(item).into(imageItemBinding.imageItem)
            imageItemBinding.imageItem.setOnClickListener {
                onImageItemClicked?.onImageClicked(imageItemBinding.imageItem, images)
            }
        }

    }

}