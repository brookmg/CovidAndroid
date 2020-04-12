package ethiopia.covid.android.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ethiopia.covid.android.R;
import ethiopia.covid.android.data.QuestionItem;
import ethiopia.covid.android.data.QuestionnaireItem;

/**
 * Created by BrookMG on 4/12/2020 in ethiopia.covid.android.ui.adapter
 * inside the project CoVidEt .
 */
public class ResultRecyclerAdapter extends RecyclerView.Adapter<ResultRecyclerAdapter.ViewHolder> {

    private Map<QuestionnaireItem , List<QuestionItem>> maps;

    public ResultRecyclerAdapter(Map<QuestionnaireItem, List<QuestionItem>> maps) {
        this.maps = maps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.result_recycler_element,
                        parent, false
                )
        );
    }

    private String implode(List<String> list) {
        StringBuilder returnable = new StringBuilder();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            returnable.append(list.get(i));
            if (i + 1 < size) returnable.append(", ");
        }
        return returnable.toString();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuestionnaireItem qItem = maps.keySet().toArray(new QuestionnaireItem[0])[position];
        holder.mainQuestionTextView.setText(qItem.getQuestionText());
        ArrayList<String> answers = new ArrayList<>();

        for (QuestionItem questionItem :
                Objects.requireNonNull(maps.get(qItem) != null && maps != null
                        ? maps.get(qItem) : new ArrayList<QuestionItem>()))
            answers.add(questionItem.getQuestionText());

        holder.mainAnswerTextView.setText(implode(answers));
    }

    @Override
    public int getItemCount() {
        return maps.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView mainQuestionTextView , mainAnswerTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mainAnswerTextView = itemView.findViewById(R.id.main_answer);
            mainQuestionTextView = itemView.findViewById(R.id.main_title);
        }
    }

}
