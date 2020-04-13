package ethiopia.covid.android.data;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by BrookMG on 4/12/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
public class QuestionnaireItem implements Serializable {

    public enum QuestionType { SINGLE_BLOCK_QUESTION , SINGLE_CHOICE_QUESTION , SINGLE_MULTIPLE_CHOICE_QUESTION }

    private QuestionType questionType;
    private String questionText;
    private List<QuestionItem> questionItems;

    public QuestionnaireItem(QuestionType questionType, String questionText, List<QuestionItem> questionItems) {
        this.questionType = questionType;
        this.questionText = questionText;
        this.questionItems = questionItems;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public List<QuestionItem> getQuestionItems() {
        return questionItems;
    }

    public void setQuestionItems(List<QuestionItem> questionItems) {
        this.questionItems = questionItems;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

}
