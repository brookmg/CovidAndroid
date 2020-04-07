package ethiopia.covid.android.data;

import java.util.List;

/**
 * Created by BrookMG on 3/24/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
public class StatRecyclerItem {

    private int type = 0;  // 0 -> Table , 1 -> Pie , 2 -> Status card, 3 -> Patients , 4 -> Line

    // Table stuff
    private List<CovidStatItem> tableItems;
    private List<String> headers;
    private int fixedHeaderCount = 0;

    private String pieCardTitle;
    private List<Integer> pieValues;
    private List<String> pieLabels;
    private List<Integer> pieColors;

    private String lineCardTitle;
    private List<LineChartItem> lineChartItems;
    private List<String> lineLabels;

    // Status Card
    private String country;
    private int totalInfected;
    private int totalDeath;
    private int totalRecovered;
    private int totalTested;

    // Patient table stuff
    private List<PatientItem> patientItems;

    public StatRecyclerItem(String lineCardTitle, List<LineChartItem> lineChartItems, List<String> lineLabels) {
        this.type = 4;
        this.lineCardTitle = lineCardTitle;
        this.lineChartItems = lineChartItems;
        this.lineLabels = lineLabels;
    }

    public StatRecyclerItem(String pieCardTitle, List<Integer> pieValues, List<String> pieLabels, List<Integer> pieColors) {
        this.type = 1;
        this.pieCardTitle = pieCardTitle;
        this.pieValues = pieValues;
        this.pieLabels = pieLabels;
        this.pieColors = pieColors;
    }

    public StatRecyclerItem(String country, int totalInfected, int totalDeath, int totalRecovered, int totalTested) {
        this.type = 2;
        this.country = country;
        this.totalInfected = totalInfected;
        this.totalDeath = totalDeath;
        this.totalRecovered = totalRecovered;
        this.totalTested = totalTested;
    }

    public StatRecyclerItem(List<String> headers, int fixedHeaderCount, List<PatientItem> patientItems) {
        this.type = 3;
        this.headers = headers;
        this.fixedHeaderCount = fixedHeaderCount;
        this.patientItems = patientItems;
    }

    public StatRecyclerItem(List<CovidStatItem> tableItems, List<String> headers) {
        this(0, tableItems, headers, 0);
    }

    public StatRecyclerItem(int type, List<CovidStatItem> tableItems, List<String> headers) {
        this(type, tableItems, headers, 0);
    }

    public StatRecyclerItem(int type, List<CovidStatItem> tableItems, List<String> headers, int fixedHeaderCount) {
        this.type = type;
        this.tableItems = tableItems;
        this.headers = headers;
        this.fixedHeaderCount = fixedHeaderCount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<CovidStatItem> getTableItems() {
        return tableItems;
    }

    public void setTableItems(List<CovidStatItem> tableItems) {
        this.tableItems = tableItems;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public int getFixedHeaderCount() {
        return fixedHeaderCount;
    }

    public void setFixedHeaderCount(int fixedHeaderCount) {
        this.fixedHeaderCount = fixedHeaderCount;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getTotalInfected() {
        return totalInfected;
    }

    public void setTotalInfected(int totalInfected) {
        this.totalInfected = totalInfected;
    }

    public int getTotalDeath() {
        return totalDeath;
    }

    public void setTotalDeath(int totalDeath) {
        this.totalDeath = totalDeath;
    }

    public int getTotalRecovered() {
        return totalRecovered;
    }

    public void setTotalRecovered(int totalRecovered) {
        this.totalRecovered = totalRecovered;
    }

    public List<PatientItem> getPatientItems() {
        return patientItems;
    }

    public void setPatientItems(List<PatientItem> patientItems) {
        this.patientItems = patientItems;
    }

    public int getTotalTested() {
        return totalTested;
    }

    public void setTotalTested(int totalTested) {
        this.totalTested = totalTested;
    }

    public List<Integer> getPieValues() {
        return pieValues;
    }

    public void setPieValues(List<Integer> pieValues) {
        this.pieValues = pieValues;
    }

    public List<String> getPieLabels() {
        return pieLabels;
    }

    public void setPieLabels(List<String> pieLabels) {
        this.pieLabels = pieLabels;
    }

    public List<Integer> getPieColors() {
        return pieColors;
    }

    public void setPieColors(List<Integer> pieColors) {
        this.pieColors = pieColors;
    }

    public String getPieCardTitle() {
        return pieCardTitle;
    }

    public void setPieCardTitle(String pieCardTitle) {
        this.pieCardTitle = pieCardTitle;
    }

    public String getLineCardTitle() {
        return lineCardTitle;
    }

    public void setLineCardTitle(String lineCardTitle) {
        this.lineCardTitle = lineCardTitle;
    }

    public List<LineChartItem> getLineChartItems() {
        return lineChartItems;
    }

    public void setLineChartItems(List<LineChartItem> lineChartItems) {
        this.lineChartItems = lineChartItems;
    }

    public List<String> getLineLabels() {
        return lineLabels;
    }

    public void setLineLabels(List<String> lineLabels) {
        this.lineLabels = lineLabels;
    }
}
