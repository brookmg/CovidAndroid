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

    public Case(int total, int stable, int critical, int deceased) {
        this.total = total;
        this.stable = stable;
        this.critical = critical;
        this.deceased = deceased;
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
}
