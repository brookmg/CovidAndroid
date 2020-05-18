package ethiopia.covid.android.network

import ethiopia.covid.android.data.JohnsHopkinsItem
import ethiopia.covid.android.data.WorldCovid
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by BrookMG on 3/25/2020 in ethiopia.covid.android.network
 * inside the project CoVidEt .
 */
interface WorldCovidAPI {
    @GET("v2/countries")
    suspend fun getListOfStat(): List<WorldCovid>

    @GET("v2/historical/{country}?limit=30")
    suspend fun getCountryHistoricalData(@Path("country") country: String): JohnsHopkinsItem
}