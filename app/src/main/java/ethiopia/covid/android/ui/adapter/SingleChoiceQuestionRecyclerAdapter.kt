package ethiopia.covid.android.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import ethiopia.covid.android.R
import ethiopia.covid.android.data.QuestionItem

/**
 * Created by BrookMG on 4/11/2020 in ethiopia.covid.android.ui.adapter
 * inside the project CoVidEt .
 */
class SingleChoiceQuestionRecyclerAdapter(private val questionItems: List<QuestionItem>, private val onQuestionItemClicked: OnQuestionItemSelected?) : RecyclerView.Adapter<SingleChoiceQuestionRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.question_page_one_recycler_element, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.choiceLabel.text = questionItems[position].questionText
        if (questionItems[position].questionIconResource > 0) Glide.with(holder.choiceImage).load(questionItems[position].questionIconResource).into(holder.choiceImage) else Glide.with(holder.choiceImage).load(questionItems[position].questionIconLink).into(holder.choiceImage)
        if (onQuestionItemClicked != null) {
            holder.mainCardView.setOnClickListener {
                onQuestionItemClicked.onItemSelected(
                        questionItems[holder.adapterPosition],
                        holder.adapterPosition
                )
            }
        }
    }

    override fun getItemCount(): Int = questionItems.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val choiceLabel: AppCompatTextView = itemView.findViewById(R.id.question_text)
        val mainCardView: MaterialCardView = itemView.findViewById(R.id.main_card_view)
        val choiceImage: AppCompatImageView = itemView.findViewById(R.id.main_icon)
    }

}