package ethiopia.covid.android.data;

import java.util.Date;
import java.util.List;

/**
 * Created by BrookMG on 3/25/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
public class WorldCovid {

    /*
            "id": "maldives",
            "displayName": "Maldives",
            "areas": [],
            "totalConfirmed": 13,
            "totalDeaths": null,
            "totalRecovered": 3,
            "lastUpdated": "2020-03-25T16:32:44.415Z",
            "lat": 1.9123070240020752,
            "long": 73.54009246826172,
            "parentId": "world"
     */

    private String id;
    private String displayName;
    private List<WorldCovid> areas;
    private int totalConfirmed;
    private int totalDeaths;
    private int totalRecovered;
    private Date lastUpdated;
    private double lat;
    private double lon;
    private String parentId;

    public WorldCovid(String id, String displayName, List<WorldCovid> areas, int totalConfirmed, int totalDeaths, int totalRecovered, Date lastUpdated, double lat, double lon, String parentId) {
        this.id = id;
        this.displayName = displayName;
        this.areas = areas;
        this.totalConfirmed = totalConfirmed;
        this.totalDeaths = totalDeaths;
        this.totalRecovered = totalRecovered;
        this.lastUpdated = lastUpdated;
        this.lat = lat;
        this.lon = lon;
        this.parentId = parentId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<WorldCovid> getAreas() {
        return areas;
    }

    public void setAreas(List<WorldCovid> areas) {
        this.areas = areas;
    }

    public int getTotalConfirmed() {
        return totalConfirmed;
    }

    public void setTotalConfirmed(int totalConfirmed) {
        this.totalConfirmed = totalConfirmed;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public void setTotalDeaths(int totalDeaths) {
        this.totalDeaths = totalDeaths;
    }

    public int getTotalRecovered() {
        return totalRecovered;
    }

    public void setTotalRecovered(int totalRecovered) {
        this.totalRecovered = totalRecovered;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
