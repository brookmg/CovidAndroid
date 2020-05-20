package ethiopia.covid.android.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ethiopia.covid.android.R
import ethiopia.covid.android.data.QuestionItem
import ethiopia.covid.android.data.QuestionnaireItem.QuestionType
import ethiopia.covid.android.databinding.QuestionPageFragmentBinding
import ethiopia.covid.android.ui.adapter.CheckBoxQuestionRecyclerAdapter
import ethiopia.covid.android.ui.adapter.OnQuestionItemSelected
import ethiopia.covid.android.ui.adapter.SingleChoiceQuestionRecyclerAdapter

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
class QuestionPageFragment(
        private val questionTitle: String?,
        private val type: QuestionType?,
        private val questionItems: List<QuestionItem>,
        private val onQuestionItemSelected: OnQuestionItemSelected?
) : BaseFragment() {

    private lateinit var questionPageFragmentBinding: QuestionPageFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        questionPageFragmentBinding = QuestionPageFragmentBinding.inflate(layoutInflater)
        questionPageFragmentBinding.textView1.text = questionTitle

        when (type) {
            QuestionType.SINGLE_CHOICE_QUESTION -> {
                questionPageFragmentBinding.recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                questionPageFragmentBinding.recyclerView.adapter = CheckBoxQuestionRecyclerAdapter(questionItems, true, onQuestionItemSelected)
            }
            QuestionType.SINGLE_MULTIPLE_CHOICE_QUESTION -> {
                questionPageFragmentBinding.recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                questionPageFragmentBinding.recyclerView.adapter = CheckBoxQuestionRecyclerAdapter(questionItems, onQuestionItemSelected)
            }
            QuestionType.SINGLE_BLOCK_QUESTION -> {
                questionPageFragmentBinding.recyclerView.layoutManager = GridLayoutManager(activity, 2)
                questionPageFragmentBinding.recyclerView.adapter = SingleChoiceQuestionRecyclerAdapter(questionItems, onQuestionItemSelected)
            }
        }
        return questionPageFragmentBinding.root
    }

    companion object {
        fun newInstance(
                questionTitle: String?,
                type: QuestionType?,
                items: List<QuestionItem>,
                onQuestionItemSelected: OnQuestionItemSelected?
        ): QuestionPageFragment {
            val args = Bundle()
            val fragment = QuestionPageFragment(questionTitle, type, items, onQuestionItemSelected)
            fragment.arguments = args
            return fragment
        }
    }

}