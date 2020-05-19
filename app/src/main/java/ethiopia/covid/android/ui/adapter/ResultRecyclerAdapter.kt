package ethiopia.covid.android.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import ethiopia.covid.android.R
import ethiopia.covid.android.data.QuestionItem
import ethiopia.covid.android.data.QuestionnaireItem

/**
 * Created by BrookMG on 4/12/2020 in ethiopia.covid.android.ui.adapter
 * inside the project CoVidEt .
 */
class ResultRecyclerAdapter(private val maps: Map<QuestionnaireItem, List<QuestionItem>?>?)
    : RecyclerView.Adapter<ResultRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.result_recycler_element,
                parent, false
            )
        )
    }

    private fun implode(list: List<String>): String {
        val returnable = StringBuilder()
        val size = list.size
        for (i in 0 until size) {
            returnable.append(list[i])
            if (i + 1 < size) returnable.append(", ")
        }
        return returnable.toString()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val qItem = maps?.keys?.toTypedArray()?.get(position)

        holder.mainQuestionTextView.text = qItem?.questionText

        val answers = mutableListOf<String>()
        for (questionItem in (maps?.get(qItem) ?: listOf())) answers.add(questionItem.questionText)
        holder.mainAnswerTextView.text = implode(answers)
    }

    override fun getItemCount(): Int = maps?.size ?: 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mainQuestionTextView: AppCompatTextView = itemView.findViewById(R.id.main_title)
        var mainAnswerTextView: AppCompatTextView = itemView.findViewById(R.id.main_answer)
    }

}