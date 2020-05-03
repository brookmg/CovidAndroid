package ethiopia.covid.android.data;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by BrookMG on 3/25/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
public class WorldCovid {

    /*
            {
                "country": "China",
                "countryInfo": {
                  "_id": 156,
                  "iso2": "CN",
                  "iso3": "CHN",
                  "lat": 35,
                  "long": 105,
                  "flag": "https://raw.githubusercontent.com/NovelCOVID/API/master/assets/flags/cn.png"
                },
                "cases": 81669,
                "todayCases": 30,
                "deaths": 3329,
                "todayDeaths": 3,
                "recovered": 76964,
                "active": 1376,
                "critical": 295,
                "casesPerOneMillion": 57,
                "deathsPerOneMillion": 2,
                "updated": 1586097475837
            }
     */

    public static class CountryInfo {
        private int _id;
        private String iso2;
        private String iso3;
        private double lat;
        private @SerializedName("long") double lon;
        private String flag;

        public CountryInfo(int _id, String iso2, String iso3, double lat, double lon, String flag) {
            this._id = _id;
            this.iso2 = iso2;
            this.iso3 = iso3;
            this.lat = lat;
            this.lon = lon;
            this.flag = flag;
        }

        public int get_id() {
            return _id;
        }

        public void set_id(int _id) {
            this._id = _id;
        }

        public String getIso2() {
            return iso2;
        }

        public void setIso2(String iso2) {
            this.iso2 = iso2;
        }

        public String getIso3() {
            return iso3;
        }

        public void setIso3(String iso3) {
            this.iso3 = iso3;
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

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }
    }

    private String country;
    private CountryInfo countryInfo;
    private int cases;
    private int todayCases;
    private int deaths;
    private int todayDeaths;
    private int recovered;
    private int active;
    private int critical;
    private double casesPerOneMillion;
    private double deathsPerOnMillion;
    private long updated;

    public WorldCovid(String country, int cases, int todayCases, int deaths, int todayDeaths, int recovered, int active, int critical, double casesPerOneMillion, double deathsPerOnMillion, long updated) {
        this.country = country;
        this.cases = cases;
        this.todayCases = todayCases;
        this.deaths = deaths;
        this.todayDeaths = todayDeaths;
        this.recovered = recovered;
        this.active = active;
        this.critical = critical;
        this.casesPerOneMillion = casesPerOneMillion;
        this.deathsPerOnMillion = deathsPerOnMillion;
        this.updated = updated;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public CountryInfo getCountryInfo() {
        return countryInfo;
    }

    public void setCountryInfo(CountryInfo countryInfo) {
        this.countryInfo = countryInfo;
    }

    public int getCases() {
        return cases;
    }

    public void setCases(int cases) {
        this.cases = cases;
    }

    public int getTodayCases() {
        return todayCases;
    }

    public void setTodayCases(int todayCases) {
        this.todayCases = todayCases;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getTodayDeaths() {
        return todayDeaths;
    }

    public void setTodayDeaths(int todayDeaths) {
        this.todayDeaths = todayDeaths;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getCritical() {
        return critical;
    }

    public void setCritical(int critical) {
        this.critical = critical;
    }

    public double getCasesPerOneMillion() {
        return casesPerOneMillion;
    }

    public void setCasesPerOneMillion(double casesPerOneMillion) {
        this.casesPerOneMillion = casesPerOneMillion;
    }

    public double getDeathsPerOnMillion() {
        return deathsPerOnMillion;
    }

    public void setDeathsPerOnMillion(double deathsPerOnMillion) {
        this.deathsPerOnMillion = deathsPerOnMillion;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }
}
