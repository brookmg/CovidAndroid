package ethiopia.covid.android.ui.fragment;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import ethiopia.covid.android.R;
import ethiopia.covid.android.data.QuestionItem;
import ethiopia.covid.android.data.QuestionnaireItem;
import ethiopia.covid.android.ui.adapter.ResultRecyclerAdapter;

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
public class ResultFragment extends BaseFragment {

    private Map<QuestionnaireItem, List<QuestionItem>> questionItems;
    private RecyclerView recyclerView;

    private ResultFragment(Map<QuestionnaireItem, List<QuestionItem>> questionItems) {
        this.questionItems = questionItems;
    }

    static ResultFragment newInstance(Map<QuestionnaireItem, List<QuestionItem>> items) {
        Bundle args = new Bundle();
        ResultFragment fragment = new ResultFragment(items);
        fragment.setArguments(args);
        return fragment;
    }

    void setMap(Map<QuestionnaireItem, List<QuestionItem>> items) {
        questionItems = items;
        recyclerView.setAdapter(new ResultRecyclerAdapter(questionItems));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.result_fragment, container, false);
        recyclerView = mainView.findViewById(R.id.result_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity() , RecyclerView.VERTICAL , false));
        recyclerView.setAdapter(new ResultRecyclerAdapter(questionItems));
        return mainView;
    }

}
