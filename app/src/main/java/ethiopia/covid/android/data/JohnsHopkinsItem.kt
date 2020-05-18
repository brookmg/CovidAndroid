package ethiopia.covid.android.data

/**
 * Created by BrookMG on 4/7/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
data class JohnsHopkinsItem(var country: String, var provinces: List<String>, var timeline: TimeLine) {
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
    data class TimeLine(var cases: Map<String, Int>, var deaths: Map<String, Int>, var recovered: Map<String, Int>)

}