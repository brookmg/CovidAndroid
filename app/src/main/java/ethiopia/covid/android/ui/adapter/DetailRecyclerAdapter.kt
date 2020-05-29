package ethiopia.covid.android.ui.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import ethiopia.covid.android.R
import ethiopia.covid.android.data.DetailItem
import ethiopia.covid.android.databinding.DetailFaqRecyclerElementBinding
import ethiopia.covid.android.databinding.DetailRecyclerElementBinding
import ethiopia.covid.android.util.Utils.getCurrentTheme
import kotlin.math.max

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.adapter
 * inside the project CoVidEt .
 */
class DetailRecyclerAdapter(private val details: MutableList<DetailItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun addContent(items: List<DetailItem>?) {
        val previousPosition = 0.coerceAtLeast(details.size - 1)
        if (items != null) details.addAll(items)

        val nowPosition = max(0, details.size - 1)
        notifyItemRangeInserted(previousPosition, nowPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TEXT_TYPE -> ViewHolder(DetailRecyclerElementBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
            Q_ATYPE -> FAQViewHolder(DetailFaqRecyclerElementBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
            else -> ViewHolder(DetailRecyclerElementBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TEXT_TYPE) (holder as ViewHolder).bind(details[position]) else if (getItemViewType(position) == Q_ATYPE) (holder as FAQViewHolder).bind(details[position])
    }

    override fun getItemViewType(position: Int): Int =
        if (details[position].faqItem == null) TEXT_TYPE else Q_ATYPE

    override fun getItemCount(): Int = details.size

    internal class ViewHolder(private val detailRecyclerElementBinding: DetailRecyclerElementBinding)
        : RecyclerView.ViewHolder(detailRecyclerElementBinding.root) {

        fun bind(item: DetailItem) {
            detailRecyclerElementBinding.detailText.text = Html.fromHtml(item.title)
            detailRecyclerElementBinding.readMore.visibility = if (item.isHasMoreButton) View.VISIBLE else View.GONE

            if (getCurrentTheme(detailRecyclerElementBinding.imageView.context) == 0) {
                // Light theme
                detailRecyclerElementBinding.detailText.setTextColor(ContextCompat.getColor(detailRecyclerElementBinding.root.context, R.color.black_1))
                detailRecyclerElementBinding.mainCardView.setCardBackgroundColor(ContextCompat.getColor(detailRecyclerElementBinding.root.context, R.color.white_0))
            } else {
                detailRecyclerElementBinding.detailText.setTextColor(ContextCompat.getColor(detailRecyclerElementBinding.root.context, R.color.white_1))
                detailRecyclerElementBinding.mainCardView.setCardBackgroundColor(ContextCompat.getColor(detailRecyclerElementBinding.root.context, R.color.black_2))
            }
            if (item.imageLink == "" && item.imageResource == -1) {
                detailRecyclerElementBinding.imageView.visibility = View.GONE
            } else {
                detailRecyclerElementBinding.imageView.visibility = View.VISIBLE
                Glide.with(detailRecyclerElementBinding.imageView).asBitmap().load(
                        if (item.imageResource != -1) item.imageResource else item.imageLink
                ).addListener(object : RequestListener<Bitmap?> {
                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Bitmap?>, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    @SuppressLint("RestrictedApi")
                    override fun onResourceReady(resource: Bitmap?, model: Any, target: Target<Bitmap?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        if (resource == null) return false

                        Palette.from(resource).generate { palette: Palette? ->
                            val mutedColor = palette?.lightMutedSwatch
                            var vibrantSwatch = palette?.darkVibrantSwatch

                            if (vibrantSwatch == null) vibrantSwatch = palette?.dominantSwatch
                            if (mutedColor != null) detailRecyclerElementBinding.detailText.setTextColor(mutedColor.titleTextColor)
                            detailRecyclerElementBinding.mainCardView.setCardBackgroundColor(palette?.getLightMutedColor(Color.BLACK) ?: Color.BLACK)
                            detailRecyclerElementBinding.readMore.supportBackgroundTintList = ColorStateList.valueOf(palette?.getDarkVibrantColor(palette.getDominantColor(Color.BLUE)) ?: Color.BLUE)
                            if (vibrantSwatch != null) detailRecyclerElementBinding.readMore.setTextColor(vibrantSwatch.titleTextColor)
                        }
                        return false
                    }
                }).into(detailRecyclerElementBinding.imageView)
            }
        }

    }

    internal class FAQViewHolder(private val faqRecyclerElementBinding: DetailFaqRecyclerElementBinding)
        : RecyclerView.ViewHolder(faqRecyclerElementBinding.root) {

        fun bind(item: DetailItem) {
            faqRecyclerElementBinding.mainCardView.setOnClickListener {
                faqRecyclerElementBinding.expandableContainer.toggle(true)
            }
            faqRecyclerElementBinding.questionText.text = item.faqItem?.question
            faqRecyclerElementBinding.answerText.text = item.faqItem?.answer

            if (getCurrentTheme(itemView.context) == 0) {
                // Light theme
                faqRecyclerElementBinding.questionText.setTextColor(ContextCompat.getColor(itemView.context, R.color.black_1))
                faqRecyclerElementBinding.answerText.setTextColor(ContextCompat.getColor(itemView.context, R.color.black_1))
                faqRecyclerElementBinding.mainCardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.white_0))
            } else {
                faqRecyclerElementBinding.questionText.setTextColor(ContextCompat.getColor(itemView.context, R.color.white_1))
                faqRecyclerElementBinding.answerText.setTextColor(ContextCompat.getColor(itemView.context, R.color.white_1))
                faqRecyclerElementBinding.mainCardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.black_2))
            }
        }

    }

    companion object {
        private const val TEXT_TYPE = 0
        private const val Q_ATYPE = 1
    }

}