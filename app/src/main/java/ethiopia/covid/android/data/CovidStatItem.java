package ethiopia.covid.android.data;

/**
 * Created by BrookMG on 3/24/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
public class CovidStatItem {

    private String identifier;

    private int infected;
    private int active;
    private int death;
    private int recovered;
    private int critical;
    private int minor;
    private int suspected;

    public CovidStatItem(String identifier, int infected, int active, int death, int recovered, int critical, int minor, int suspected) {
        this.identifier = identifier;
        this.infected = infected;
        this.active = active;
        this.death = death;
        this.recovered = recovered;
        this.critical = critical;
        this.minor = minor;
        this.suspected = suspected;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getInfected() {
        return infected;
    }

    public void setInfected(int infected) {
        this.infected = infected;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getDeath() {
        return death;
    }

    public void setDeath(int death) {
        this.death = death;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public int getCritical() {
        return critical;
    }

    public void setCritical(int critical) {
        this.critical = critical;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getSuspected() {
        return suspected;
    }

    public void setSuspected(int suspected) {
        this.suspected = suspected;
    }
}
