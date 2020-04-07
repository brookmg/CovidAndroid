package ethiopia.covid.android.data;

/**
 * Created by BrookMG on 4/6/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
public class Region {

    private String regionName;
    private String regionCode;
    private int numberOfInfected;
    private int numberOfDeaths;

    public Region(String regionName, String regionCode, int numberOfInfected, int numberOfDeaths) {
        this.regionName = regionName;
        this.regionCode = regionCode;
        this.numberOfInfected = numberOfInfected;
        this.numberOfDeaths = numberOfDeaths;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public int getNumberOfInfected() {
        return numberOfInfected;
    }

    public void setNumberOfInfected(int numberOfInfected) {
        this.numberOfInfected = numberOfInfected;
    }

    public int getNumberOfDeaths() {
        return numberOfDeaths;
    }

    public void setNumberOfDeaths(int numberOfDeaths) {
        this.numberOfDeaths = numberOfDeaths;
    }
}
