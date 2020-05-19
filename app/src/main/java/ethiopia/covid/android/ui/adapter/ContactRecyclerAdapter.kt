package ethiopia.covid.android.ui.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import ethiopia.covid.android.R
import ethiopia.covid.android.data.ContactItem
import ethiopia.covid.android.util.Utils.callPhoneNumber
import ethiopia.covid.android.util.Utils.getCurrentTheme
import kotlin.math.max

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.adapter
 * inside the project CoVidEt .
 */
class ContactRecyclerAdapter(private val contactItems: MutableList<ContactItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun addContent(items: List<ContactItem>?) {
        val previousPosition = max(0, contactItems.size - 1)
        if (items != null) contactItems.addAll(items)
        val nowPosition = max(0, contactItems.size - 1)
        notifyItemRangeInserted(previousPosition, nowPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TEXT_TYPE -> ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.regional_contact_recycler_element, parent, false))
            else -> ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.regional_contact_recycler_element, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TEXT_TYPE) (holder as ViewHolder).bind(contactItems[position])
    }

    override fun getItemViewType(position: Int): Int = TEXT_TYPE

    override fun getItemCount(): Int = contactItems.size

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var textView: AppCompatTextView = itemView.findViewById(R.id.detail_text)
        var imageView: AppCompatImageView = itemView.findViewById(R.id.image_view)
        var materialCardView: MaterialCardView = itemView.findViewById(R.id.main_card_view)

        fun bind(item: ContactItem) {
            textView.text = Html.fromHtml(item.regionPhoneNumber)
            if (getCurrentTheme(itemView.context) == 0) {
                // Light theme
                textView.setTextColor(ContextCompat.getColor(itemView.context, R.color.black_1))
                materialCardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.white_0))
            } else {
                textView.setTextColor(ContextCompat.getColor(itemView.context, R.color.white_1))
                materialCardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.black_2))
            }
            materialCardView.setOnClickListener { callPhoneNumber(itemView.context, item.regionPhoneNumber) }
            if (item.regionFlagImageLink == -1) {
                imageView.visibility = View.GONE
            } else {
                imageView.visibility = View.VISIBLE
                Glide.with(imageView).asBitmap().load(item.regionFlagImageLink).into(imageView)
            }
        }

    }

    companion object {
        private const val TEXT_TYPE = 0
    }

}