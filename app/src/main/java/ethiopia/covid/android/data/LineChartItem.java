package ethiopia.covid.android.data;

import java.util.List;

/**
 * Created by BrookMG on 4/7/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
public class LineChartItem {

    private List<Integer> values;
    private String chartLabel;
    private Integer lineColor;
    private Integer circleColor;

    public LineChartItem(List<Integer> values, String chartLabel, Integer lineColor, Integer circleColor) {
        this.values = values;
        this.chartLabel = chartLabel;
        this.lineColor = lineColor;
        this.circleColor = circleColor;
    }

    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }

    public String getChartLabel() {
        return chartLabel;
    }

    public void setChartLabel(String chartLabel) {
        this.chartLabel = chartLabel;
    }

    public Integer getLineColor() {
        return lineColor;
    }

    public void setLineColor(Integer lineColor) {
        this.lineColor = lineColor;
    }

    public Integer getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(Integer circleColor) {
        this.circleColor = circleColor;
    }
}
