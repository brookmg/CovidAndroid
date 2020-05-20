package ethiopia.covid.android.ui.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ethiopia.covid.android.R
import ethiopia.covid.android.data.ContactItem
import ethiopia.covid.android.databinding.RegionalContactRecyclerElementBinding
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
            TEXT_TYPE -> ViewHolder(
                    RegionalContactRecyclerElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> ViewHolder(
                    RegionalContactRecyclerElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TEXT_TYPE) (holder as ViewHolder).bind(contactItems[position])
    }

    override fun getItemViewType(position: Int): Int = TEXT_TYPE

    override fun getItemCount(): Int = contactItems.size

    internal class ViewHolder(private val regionalContactRecyclerElementBinding: RegionalContactRecyclerElementBinding)
        : RecyclerView.ViewHolder(regionalContactRecyclerElementBinding.root) {

        fun bind(item: ContactItem) {
            regionalContactRecyclerElementBinding.detailText.text = Html.fromHtml(item.regionPhoneNumber)
            if (getCurrentTheme(regionalContactRecyclerElementBinding.root.context) == 0) {
                // Light theme
                regionalContactRecyclerElementBinding.detailText.setTextColor(
                        ContextCompat.getColor(regionalContactRecyclerElementBinding.root.context, R.color.black_1))
                regionalContactRecyclerElementBinding.mainCardView.setCardBackgroundColor(
                        ContextCompat.getColor(regionalContactRecyclerElementBinding.root.context, R.color.white_0))
            } else {
                regionalContactRecyclerElementBinding.detailText.setTextColor(
                        ContextCompat.getColor(regionalContactRecyclerElementBinding.root.context, R.color.white_1))
                regionalContactRecyclerElementBinding.mainCardView.setCardBackgroundColor(
                        ContextCompat.getColor(regionalContactRecyclerElementBinding.root.context, R.color.black_2))
            }

            regionalContactRecyclerElementBinding.mainCardView.setOnClickListener {
                callPhoneNumber(regionalContactRecyclerElementBinding.root.context, item.regionPhoneNumber)
            }
            if (item.regionFlagImageLink == -1) {
                regionalContactRecyclerElementBinding.imageView.visibility = View.GONE
            } else {
                regionalContactRecyclerElementBinding.imageView.visibility = View.VISIBLE
                Glide.with(regionalContactRecyclerElementBinding.imageView).asBitmap()
                        .load(item.regionFlagImageLink).into(regionalContactRecyclerElementBinding.imageView)
            }
        }

    }

    companion object {
        private const val TEXT_TYPE = 0
    }

}