package ethiopia.covid.android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import ethiopia.covid.android.data.QuestionItem
import ethiopia.covid.android.databinding.QuestionPageTwoRecyclerElementBinding

/**
 * Created by BrookMG on 4/12/2020 in ethiopia.covid.android.ui.adapter
 * inside the project CoVidEt .
 */
class CheckBoxQuestionRecyclerAdapter(
        private val questionItems: List<QuestionItem>,
        private val singleCheckBoxOnly: Boolean,
        private val onQuestionCheckBoxClicked: OnQuestionItemSelected?
) : RecyclerView.Adapter<CheckBoxQuestionRecyclerAdapter.ViewHolder>() {

    constructor(questionItems: List<QuestionItem>, onQuestionCheckBoxClicked: OnQuestionItemSelected?)
            : this(questionItems, false, onQuestionCheckBoxClicked)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(
                    QuestionPageTwoRecyclerElementBinding.inflate(
                            LayoutInflater.from(parent.context), parent, false
                    ), this
            )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(questionItems, questionItems[position], singleCheckBoxOnly, onQuestionCheckBoxClicked)
    }

    override fun getItemCount(): Int = questionItems.size

    class ViewHolder(
            private val questionPageTwoRecyclerElementBinding: QuestionPageTwoRecyclerElementBinding,
            private val recyclerAdapter: CheckBoxQuestionRecyclerAdapter
    ) : RecyclerView.ViewHolder(questionPageTwoRecyclerElementBinding.root) {

        private fun singleSelectItem(questionItems: List<QuestionItem>, position: Int, selection: Boolean) {
            for (i in questionItems.indices) {
                if (i == position) questionItems[position].isSelectedQuestion = selection
                else questionItems[i].isSelectedQuestion = false
                questionPageTwoRecyclerElementBinding.root.post {
                    recyclerAdapter.notifyItemChanged(i)
                }
            }
        }

        fun bind(questionItems: List<QuestionItem>, questionItem: QuestionItem, singleCheckBoxOnly: Boolean,
                 onQuestionCheckBoxClicked: OnQuestionItemSelected?) {
            questionPageTwoRecyclerElementBinding.let {
                it.mainCheckBox.text = questionItem.questionText
                it.mainCheckBox.setOnCheckedChangeListener(null)
                it.mainCheckBox.isChecked = questionItem.isSelectedQuestion

                it.mainCheckBox.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                    if (singleCheckBoxOnly) {
                        singleSelectItem(questionItems, adapterPosition, isChecked)
                    } else {
                        questionItems[adapterPosition].isSelectedQuestion = isChecked
                    }

                    onQuestionCheckBoxClicked?.onItemSelected(
                            questionItems[adapterPosition],
                            adapterPosition
                    )
                }
            }
        }

    }

}