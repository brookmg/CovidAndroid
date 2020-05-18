package ethiopia.covid.android.network

import ethiopia.covid.android.data.Case
import ethiopia.covid.android.data.Patients
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by BrookMG on 3/25/2020 in ethiopia.covid.android.network
 * inside the project CoVidEt .
 */
interface PMOCovidAPI {
    @GET("cases")
    suspend fun getCases(): List<Case>

    @GET("patients?limit=400&page=0")
    suspend fun getPatients(): Patients
}