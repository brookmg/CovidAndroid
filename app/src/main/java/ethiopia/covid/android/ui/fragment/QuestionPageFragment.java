package ethiopia.covid.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ethiopia.covid.android.R;
import ethiopia.covid.android.data.QuestionnaireItem;
import ethiopia.covid.android.data.QuestionItem;
import ethiopia.covid.android.ui.adapter.CheckBoxQuestionRecyclerAdapter;
import ethiopia.covid.android.ui.adapter.OnQuestionItemSelected;
import ethiopia.covid.android.ui.adapter.SingleChoiceQuestionRecyclerAdapter;

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
public class QuestionPageFragment extends BaseFragment {

    private RecyclerView mainRecyclerView;
    private TextView mainTextView;
    private ConstraintLayout mainContainer;
    private QuestionnaireItem.QuestionType type;
    private List<QuestionItem> questionItems;
    private OnQuestionItemSelected onQuestionItemSelected;
    private String questionTitle;

    public QuestionPageFragment(
            String questionTitle,
            QuestionnaireItem.QuestionType type,
            List<QuestionItem> questionItems,
            OnQuestionItemSelected onQuestionItemSelected
    ) {
        this.questionTitle = questionTitle;
        this.type = type;
        this.questionItems = questionItems;
        this.onQuestionItemSelected = onQuestionItemSelected;
    }

    public static QuestionPageFragment newInstance(
            String questionTitle,
            QuestionnaireItem.QuestionType type,
            List<QuestionItem> items,
            OnQuestionItemSelected onQuestionItemSelected
    ) {
        Bundle args = new Bundle();
        QuestionPageFragment fragment = new QuestionPageFragment(questionTitle, type, items, onQuestionItemSelected);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.question_page_fragment, container, false);
        mainRecyclerView = mainView.findViewById(R.id.recycler_view);
        mainTextView = mainView.findViewById(R.id.textView_1);
        mainContainer = mainView.findViewById(R.id.main_container);

        mainTextView.setText(questionTitle);

        switch (type) {
            case SINGLE_CHOICE_QUESTION:
            case SINGLE_MULTIPLE_CHOICE_QUESTION:
                mainRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity() , RecyclerView.VERTICAL , false));
                mainRecyclerView.setAdapter(new CheckBoxQuestionRecyclerAdapter(questionItems, onQuestionItemSelected));
                break;
            case SINGLE_BLOCK_QUESTION:
                mainRecyclerView.setLayoutManager(new GridLayoutManager(getActivity() , 2));
                mainRecyclerView.setAdapter(new SingleChoiceQuestionRecyclerAdapter(questionItems, onQuestionItemSelected));
                break;
        }

        return mainView;
    }

}
