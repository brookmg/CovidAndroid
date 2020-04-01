package ethiopia.covid.android.data;

import java.util.List;

/**
 * Created by BrookMG on 3/24/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
public class StatRecyclerItem {

    private int type = 0;  // 0 -> Table , 1 -> Pie , 2 -> Status card

    // Table stuff
    private List<CovidStatItem> tableItems;
    private List<String> headers;
    private int fixedHeaderCount = 0;

    // todo: Pie stuff


    // Status Card
    private String country;
    private int totalInfected;
    private int totalDeath;
    private int totalRecovered;

    // Patient table stuff
    private List<PatientItem> patientItems;

    public StatRecyclerItem(String country, int totalInfected, int totalDeath, int totalRecovered) {
        this.type = 2;
        this.country = country;
        this.totalInfected = totalInfected;
        this.totalDeath = totalDeath;
        this.totalRecovered = totalRecovered;
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
}
