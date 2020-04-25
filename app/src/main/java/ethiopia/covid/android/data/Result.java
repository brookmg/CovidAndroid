package ethiopia.covid.android.data;

import java.util.List;
import java.util.Map;

/**
 * Created by BrookMG on 4/25/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
public class Result {
    private Map<QuestionnaireItem, List<QuestionItem>> questionItems;
    private double longitude;
    private double latitude;
    private double accuracy;

    public Result(Map<QuestionnaireItem, List<QuestionItem>> questionItems, double longitude, double latitude, double accuracy) {
        this.questionItems = questionItems;
        this.longitude = longitude;
        this.latitude = latitude;
        this.accuracy = accuracy;
    }

    public Map<QuestionnaireItem, List<QuestionItem>> getQuestionItems() {
        return questionItems;
    }

    public void setQuestionItems(Map<QuestionnaireItem, List<QuestionItem>> questionItems) {
        this.questionItems = questionItems;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }
}
