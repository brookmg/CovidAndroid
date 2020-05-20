package ethiopia.covid.android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import ethiopia.covid.android.R
import ethiopia.covid.android.data.QuestionItem
import ethiopia.covid.android.databinding.QuestionPageOneRecyclerElementBinding

/**
 * Created by BrookMG on 4/11/2020 in ethiopia.covid.android.ui.adapter
 * inside the project CoVidEt .
 */
class SingleChoiceQuestionRecyclerAdapter(private val questionItems: List<QuestionItem>,
                                          private val onQuestionItemClicked: OnQuestionItemSelected?)
    : RecyclerView.Adapter<SingleChoiceQuestionRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            QuestionPageOneRecyclerElementBinding.inflate(LayoutInflater.from(parent.context),parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(questionItems[position] , onQuestionItemClicked);
    }

    override fun getItemCount(): Int = questionItems.size

    class ViewHolder( private val questionPageOneRecyclerElementBinding: QuestionPageOneRecyclerElementBinding )
        : RecyclerView.ViewHolder(questionPageOneRecyclerElementBinding.root) {

        fun bind(questionItem: QuestionItem, onQuestionItemClicked: OnQuestionItemSelected?) {
            questionPageOneRecyclerElementBinding.questionText.text = questionItem.questionText
            if (questionItem.questionIconResource > 0) Glide.with(questionPageOneRecyclerElementBinding.mainIcon)
                    .load(questionItem.questionIconResource).into(questionPageOneRecyclerElementBinding.mainIcon)
            else Glide.with(questionPageOneRecyclerElementBinding.mainIcon).load(questionItem.questionIconLink)
                    .into(questionPageOneRecyclerElementBinding.mainIcon)
            if (onQuestionItemClicked != null) {
                questionPageOneRecyclerElementBinding.mainCardView.setOnClickListener {
                    onQuestionItemClicked.onItemSelected(
                            questionItem,
                            adapterPosition
                    )
                }
            }
        }
    }

}