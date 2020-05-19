package ethiopia.covid.android.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.checkbox.MaterialCheckBox
import ethiopia.covid.android.R
import ethiopia.covid.android.data.QuestionItem

/**
 * Created by BrookMG on 4/12/2020 in ethiopia.covid.android.ui.adapter
 * inside the project CoVidEt .
 */
class CheckBoxQuestionRecyclerAdapter(private val questionItems: List<QuestionItem>, private val singleCheckBoxOnly: Boolean, private val onQuestionCheckBoxClicked: OnQuestionItemSelected?) : RecyclerView.Adapter<CheckBoxQuestionRecyclerAdapter.ViewHolder>() {
    private val itemAlreadyChecked = -1

    constructor(questionItems: List<QuestionItem>, onQuestionCheckBoxClicked: OnQuestionItemSelected?)
            : this(questionItems, false, onQuestionCheckBoxClicked)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.question_page_two_recycler_element, parent, false))

    private fun numberOfSelectedItems(): Int {
        var returnable = 0
        for (item in questionItems) if (item.isSelectedQuestion) returnable++
        return returnable
    }

    private fun singleSelectItem(holder: ViewHolder, position: Int, selection: Boolean) {
        for (i in questionItems.indices) {
            if (i == position) questionItems[position].isSelectedQuestion = selection else questionItems[i].isSelectedQuestion = false
            holder.itemView.post { notifyItemChanged(i) }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mainCheckBox.text = questionItems[position].questionText
        holder.mainCheckBox.setOnCheckedChangeListener(null)
        holder.mainCheckBox.isChecked = questionItems[position].isSelectedQuestion
        holder.mainCheckBox.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (singleCheckBoxOnly) {
                singleSelectItem(holder, holder.adapterPosition, isChecked)
            } else {
                questionItems[holder.adapterPosition].isSelectedQuestion = isChecked
            }
            onQuestionCheckBoxClicked?.onItemSelected(
                    questionItems[holder.adapterPosition],
                    holder.adapterPosition
            )
        }
    }

    override fun getItemCount(): Int = questionItems.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mainCheckBox : MaterialCheckBox = itemView.findViewById(R.id.main_check_box)

    }

}