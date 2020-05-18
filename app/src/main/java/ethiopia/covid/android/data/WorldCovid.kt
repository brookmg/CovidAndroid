package ethiopia.covid.android.data

import com.google.gson.annotations.SerializedName

/**
 * Created by BrookMG on 3/25/2020 in ethiopia.covid.android.data
 * inside the project CoVidEt .
 */
data class WorldCovid(var country: String, var cases: Int, var todayCases: Int,
                 var deaths: Int, var todayDeaths: Int, var recovered: Int,
                 var active: Int, var critical: Int, var casesPerOneMillion: Double,
                 var deathsPerOnMillion: Double, var updated: Long,
                 var countryInfo: CountryInfo? = null) {
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
    data class CountryInfo(
            var _id: Int,
            var iso2: String,
            var iso3: String,
            var lat: Double,
            @field:SerializedName("long") var lon: Double,
            var flag: String)

}