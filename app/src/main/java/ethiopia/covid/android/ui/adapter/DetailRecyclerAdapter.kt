package ethiopia.covid.android.ui.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.card.MaterialCardView
import ethiopia.covid.android.R
import ethiopia.covid.android.data.DetailItem
import ethiopia.covid.android.util.Utils.getCurrentTheme
import net.cachapa.expandablelayout.ExpandableLayout
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
            TEXT_TYPE -> ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.detail_recycler_element, parent, false))
            Q_ATYPE -> FAQViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.detail_faq_recycler_element, parent, false))
            else -> ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.detail_recycler_element, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TEXT_TYPE) (holder as ViewHolder).bind(details[position]) else if (getItemViewType(position) == Q_ATYPE) (holder as FAQViewHolder).bind(details[position])
    }

    override fun getItemViewType(position: Int): Int =
        if (details[position].faqItem == null) TEXT_TYPE else Q_ATYPE

    override fun getItemCount(): Int = details.size

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: AppCompatTextView = itemView.findViewById(R.id.detail_text)
        var imageView: AppCompatImageView = itemView.findViewById(R.id.image_view)
        var readMore: AppCompatButton = itemView.findViewById(R.id.read_more)
        var materialCardView: MaterialCardView = itemView.findViewById(R.id.main_card_view)

        fun bind(item: DetailItem) {
            textView.text = Html.fromHtml(item.title)
            readMore.visibility = if (item.isHasMoreButton) View.VISIBLE else View.GONE
            if (getCurrentTheme(itemView.context) == 0) {
                // Light theme
                textView.setTextColor(ContextCompat.getColor(itemView.context, R.color.black_1))
                materialCardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.white_0))
            } else {
                textView.setTextColor(ContextCompat.getColor(itemView.context, R.color.white_1))
                materialCardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.black_2))
            }
            if (item.imageLink == "" &&
                    item.imageResource == -1) {
                imageView.visibility = View.GONE
            } else {
                imageView.visibility = View.VISIBLE
                Glide.with(imageView).asBitmap().load(
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
                            if (mutedColor != null) textView.setTextColor(mutedColor.titleTextColor)
                            materialCardView.setCardBackgroundColor(palette?.getLightMutedColor(Color.BLACK) ?: Color.BLACK)
                            readMore.supportBackgroundTintList = ColorStateList.valueOf(palette?.getDarkVibrantColor(palette.getDominantColor(Color.BLUE)) ?: Color.BLUE)
                            if (vibrantSwatch != null) readMore.setTextColor(vibrantSwatch.titleTextColor)
                        }
                        return false
                    }
                }).into(imageView)
            }
        }

    }

    internal class FAQViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var questionText: AppCompatTextView = itemView.findViewById(R.id.question_text)
        var answerText: AppCompatTextView = itemView.findViewById(R.id.answer_text)
        var materialCardView: MaterialCardView = itemView.findViewById(R.id.main_card_view)
        var expandableLayout: ExpandableLayout = itemView.findViewById(R.id.expandable_container)

        fun bind(item: DetailItem) {
            materialCardView.setOnClickListener { expandableLayout.toggle(true) }
            questionText.text = item.faqItem?.question
            answerText.text = item.faqItem?.answer

            if (getCurrentTheme(itemView.context) == 0) {
                // Light theme
                questionText.setTextColor(ContextCompat.getColor(itemView.context, R.color.black_1))
                answerText.setTextColor(ContextCompat.getColor(itemView.context, R.color.black_1))
                materialCardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.white_0))
            } else {
                questionText.setTextColor(ContextCompat.getColor(itemView.context, R.color.white_1))
                answerText.setTextColor(ContextCompat.getColor(itemView.context, R.color.white_1))
                materialCardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.black_2))
            }
        }

    }

    companion object {
        private const val TEXT_TYPE = 0
        private const val Q_ATYPE = 1
    }

}