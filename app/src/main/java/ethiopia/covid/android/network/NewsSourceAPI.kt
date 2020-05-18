package ethiopia.covid.android.network

import ethiopia.covid.android.data.NewsItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by BrookMG on 4/23/2020 in ethiopia.covid.android.network
 * inside the project CoVidEt .
 */
interface NewsSourceAPI {
    @GET("latest?limit=10")
    suspend fun getLatest(): List<NewsItem>

    @GET("before?limit=10")
    suspend fun getBeforeItem(@Query("before") itemId: Int): List<NewsItem>
}