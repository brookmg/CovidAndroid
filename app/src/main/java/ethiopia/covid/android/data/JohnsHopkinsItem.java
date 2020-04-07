package ethiopia.covid.android.data;

import java.util.List;
import java.util.Map;

/**
 * Created by BrookMG on 4/7/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
public class JohnsHopkinsItem {

    /*
        {
          "country": "Ethiopia",
          "provinces": [
            "mainland"
          ],
          "timeline": {
            "cases": {
              "3/8/20": 0,
              "3/9/20": 0,
              "3/10/20": 0,
              "3/11/20": 0,
              "3/12/20": 0,
              "3/13/20": 1,
              ...
            },
            "deaths": {
              "3/8/20": 0,
              "3/9/20": 0,
              "3/10/20": 0,
              "3/11/20": 0,
              "3/12/20": 0,
              "3/13/20": 0,
              ...
            },
            "recovered": {
              "3/8/20": 0,
              "3/9/20": 0,
              "3/10/20": 0,
              "3/11/20": 0,
              "3/12/20": 0,
              "3/13/20": 0,
              ...
            }
          }
        }
     */

    public static class TimeLine {
        private Map<String, Integer> cases;
        private Map<String, Integer> deaths;
        private Map<String, Integer> recovered;

        public TimeLine(Map<String, Integer> cases, Map<String, Integer> deaths, Map<String, Integer> recovered) {
            this.cases = cases;
            this.deaths = deaths;
            this.recovered = recovered;
        }

        public Map<String, Integer> getCases() {
            return cases;
        }

        public void setCases(Map<String, Integer> cases) {
            this.cases = cases;
        }

        public Map<String, Integer> getDeaths() {
            return deaths;
        }

        public void setDeaths(Map<String, Integer> deaths) {
            this.deaths = deaths;
        }

        public Map<String, Integer> getRecovered() {
            return recovered;
        }

        public void setRecovered(Map<String, Integer> recovered) {
            this.recovered = recovered;
        }
    }

    private String country;
    private List<String> provinces;
    private TimeLine timeline;

    public JohnsHopkinsItem(String country, List<String> provinces, TimeLine timeline) {
        this.country = country;
        this.provinces = provinces;
        this.timeline = timeline;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<String> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<String> provinces) {
        this.provinces = provinces;
    }

    public TimeLine getTimeline() {
        return timeline;
    }

    public void setTimeline(TimeLine timeline) {
        this.timeline = timeline;
    }
}
