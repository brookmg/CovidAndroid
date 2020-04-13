package ethiopia.covid.android.data;

import androidx.annotation.NonNull;

/**
 * Created by BrookMG on 4/11/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
public class QuestionItem {

    // single block question type
    private String questionText;
    private int questionIconResource;
    private boolean selectedQuestion = false;

    public QuestionItem(String questionText, int questionIconResource) {
        this.questionText = questionText;
        this.questionIconResource = questionIconResource;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public int getQuestionIconResource() {
        return questionIconResource;
    }

    public void setQuestionIconResource(int questionIconResource) {
        this.questionIconResource = questionIconResource;
    }

    public boolean isSelectedQuestion() {
        return selectedQuestion;
    }

    public void setSelectedQuestion(boolean selectedQuestion) {
        this.selectedQuestion = selectedQuestion;
    }

}
