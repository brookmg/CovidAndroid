package ethiopia.covid.android.data;

import java.util.Date;
import java.util.List;

/**
 * Created by BrookMG on 3/25/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
public class Patients {

    private int count; // 12
    private String next; // "https://api.pmo.gov.et/v1/patients/?limit=10&offset=10",
    private String previous; // null
    private List<PatientItem> results;

    public Patients(int count, String next, String previous, List<PatientItem> results) {
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<PatientItem> getResults() {
        return results;
    }

    public void setResults(List<PatientItem> results) {
        this.results = results;
    }
}
