package ethiopia.covid.android.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import ethiopia.covid.android.R;
import ethiopia.covid.android.data.QuestionItem;

/**
 * Created by BrookMG on 4/11/2020 in ethiopia.covid.android.ui.adapter
 * inside the project CoVidEt .
 */
public class SingleChoiceQuestionRecyclerAdapter extends RecyclerView.Adapter<SingleChoiceQuestionRecyclerAdapter.ViewHolder> {

    private List<QuestionItem> questionItems;
    private OnQuestionItemSelected onQuestionItemClicked;

    public SingleChoiceQuestionRecyclerAdapter(List<QuestionItem> questionItems, OnQuestionItemSelected onQuestionItemClicked) {
        this.questionItems = questionItems;
        this.onQuestionItemClicked = onQuestionItemClicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.question_page_one_recycler_element, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.choiceLabel.setText(questionItems.get(position).getQuestionText());
        Glide.with(holder.choiceImage).load(questionItems.get(position).getQuestionIconResource()).into(holder.choiceImage);

        if (onQuestionItemClicked != null) {
            holder.mainCardView.setOnClickListener(
                    v -> onQuestionItemClicked.onItemSelected(
                            questionItems.get(holder.getAdapterPosition()),
                            holder.getAdapterPosition()
                    )
            );
        }
    }

    @Override
    public int getItemCount() {
        return questionItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView choiceLabel;
        private MaterialCardView mainCardView;
        private AppCompatImageView choiceImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            choiceImage = itemView.findViewById(R.id.main_icon);
            mainCardView = itemView.findViewById(R.id.main_card_view);
            choiceLabel = itemView.findViewById(R.id.question_text);
        }

    }

}
