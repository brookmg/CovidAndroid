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
    private String questionIconLink;

    public QuestionItem(String questionText, int questionIconResource) {
        this.questionText = questionText;
        this.questionIconResource = questionIconResource;
    }

    public QuestionItem(String questionText, String questionIconLink) {
        this.questionText = questionText;
        this.questionIconLink = questionIconLink;
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

    public String getQuestionIconLink() {
        return questionIconLink;
    }

    public void setQuestionIconLink(String questionIconLink) {
        this.questionIconLink = questionIconLink;
    }
}
