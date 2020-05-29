package ethiopia.covid.android.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ethiopia.covid.android.data.QuestionItem
import ethiopia.covid.android.data.QuestionnaireItem
import ethiopia.covid.android.databinding.ResultRecyclerElementBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by BrookMG on 4/12/2020 in ethiopia.covid.android.ui.adapter
 * inside the project CoVidEt .
 */
class ResultRecyclerAdapter(private val maps: Map<QuestionnaireItem, List<QuestionItem>?>?)
    : RecyclerView.Adapter<ResultRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ResultRecyclerElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val qItem = maps?.keys?.toTypedArray()?.get(position)
        val answers = mutableListOf<String>()
        for (questionItem in (maps?.get(qItem) ?: listOf())) answers.add(questionItem.questionText)

        if (qItem != null) holder.bind(qItem , answers)
    }

    override fun getItemCount(): Int = maps?.size ?: 0

    class ViewHolder(private val resultRecyclerElementBinding: ResultRecyclerElementBinding)
        : RecyclerView.ViewHolder(resultRecyclerElementBinding.root) {

        private suspend fun implode(list: List<String>): String {
            return withContext(Dispatchers.Default) {
                val returnable = StringBuilder()
                val size = list.size
                for (i in list.indices) {
                    returnable.append(list[i])
                    if (i + 1 < size) returnable.append(", ")
                }
                returnable.toString()
            }
        }

        fun bind(questionItem: QuestionnaireItem, answers: MutableList<String>) {
            CoroutineScope(Dispatchers.Main).launch {
                resultRecyclerElementBinding.mainTitle.text = questionItem.questionText
                resultRecyclerElementBinding.mainAnswer.text = implode(answers)
            }
        }
    }

}