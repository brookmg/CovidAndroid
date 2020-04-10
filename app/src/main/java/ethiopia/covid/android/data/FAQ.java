package ethiopia.covid.android.data;

import java.util.List;

/**
 * Created by BrookMG on 4/10/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
public class FAQ {

    public static class QuestionItem {
        private String question;
        private String answer;

        public QuestionItem(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }

    private String source;
    private List<QuestionItem> data;

    public FAQ(String source, List<QuestionItem> data) {
        this.source = source;
        this.data = data;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<QuestionItem> getData() {
        return data;
    }

    public void setData(List<QuestionItem> data) {
        this.data = data;
    }
}
