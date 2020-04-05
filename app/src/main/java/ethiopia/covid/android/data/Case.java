package ethiopia.covid.android.data;

/**
 * Created by BrookMG on 3/25/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
public class Case {

    private int total;
    private int stable;
    private int critical;
    private int deceased;
    private int tested;
    private int confirmed;
    private int recovered;
    private int no_longer_in_ethiopia;
    private int false_positives;

    public Case(int total, int stable, int critical, int deceased, int tested, int confirmed, int recovered, int no_longer_in_ethiopia, int false_positives) {
        this.total = total;
        this.stable = stable;
        this.critical = critical;
        this.deceased = deceased;
        this.tested = tested;
        this.confirmed = confirmed;
        this.recovered = recovered;
        this.no_longer_in_ethiopia = no_longer_in_ethiopia;
        this.false_positives = false_positives;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStable() {
        return stable;
    }

    public void setStable(int stable) {
        this.stable = stable;
    }

    public int getCritical() {
        return critical;
    }

    public void setCritical(int critical) {
        this.critical = critical;
    }

    public int getDeceased() {
        return deceased;
    }

    public void setDeceased(int deceased) {
        this.deceased = deceased;
    }

    public int getTested() {
        return tested;
    }

    public void setTested(int tested) {
        this.tested = tested;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public int getNo_longer_in_ethiopia() {
        return no_longer_in_ethiopia;
    }

    public void setNo_longer_in_ethiopia(int no_longer_in_ethiopia) {
        this.no_longer_in_ethiopia = no_longer_in_ethiopia;
    }

    public int getFalse_positives() {
        return false_positives;
    }

    public void setFalse_positives(int false_positives) {
        this.false_positives = false_positives;
    }
}
