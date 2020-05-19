package ethiopia.covid.android.network

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.annotation.UiThread
import androidx.core.content.ContextCompat
import com.chuckerteam.chucker.api.ChuckerInterceptor
import ethiopia.covid.android.App.Companion.instance
import ethiopia.covid.android.R
import ethiopia.covid.android.data.*
import ethiopia.covid.android.util.Constant
import ethiopia.covid.android.util.Utils.generateColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by BrookMG on 3/25/2020 in ethiopia.covid.android.network
 * inside the project CoVidEt .
 */
@Suppress("unused")
class API {
    private val pmoCovidAPI: PMOCovidAPI
    private val worldCovidAPI: WorldCovidAPI
    private val contentCovidAPI: ContentCovidAPI
    private val newsSourceAPI: NewsSourceAPI

    interface OnItemReady<T> {
        fun onItem(item: T, err: String?)
    }

    @UiThread
    fun getCases(onCaseReady: OnItemReady<Case?>) {
        val mainHandler = Handler(Looper.getMainLooper())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = pmoCovidAPI.getCases()
                if (response.isNotEmpty()) mainHandler.post { onCaseReady.onItem(response[0], "") }
                else {
                    mainHandler.post {
                        try {
                            onCaseReady.onItem(null, "Response was empty")
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            } catch (e: IOException) {
                mainHandler.post { onCaseReady.onItem(null, e.toString()) }
                e.printStackTrace()
            }
        }
    }

    @UiThread
    fun getPatients(onCaseReady: OnItemReady<Patients?>) {
        val mainHandler = Handler(Looper.getMainLooper())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = pmoCovidAPI.getPatients()
                if (response.results.isNotEmpty()) {
                    mainHandler.post { onCaseReady.onItem(response, "") }
                } else {
                    mainHandler.post {
                        try {
                            onCaseReady.onItem(null, "Response was empty")
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            } catch (e: IOException) {
                mainHandler.post { onCaseReady.onItem(null, e.toString()) }
                e.printStackTrace()
            }
        }
    }

    @UiThread
    fun getWorldStat(onItemReady: OnItemReady<List<WorldCovid>?>) {
        val mainHandler = Handler(Looper.getMainLooper())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = worldCovidAPI.getListOfStat()
                mainHandler.post { onItemReady.onItem(response, "") }
            } catch (e: IOException) {
                mainHandler.post { onItemReady.onItem(null, e.toString()) }
                e.printStackTrace()
            }
        }
    }

    @UiThread
    fun getProtectiveMeasures(onItemReady: OnItemReady<ProtectiveMeasures?>) {
        val mainHandler = Handler(Looper.getMainLooper())
        executors.execute {
            try {
                val response = contentCovidAPI.getProtectiveMeasures().execute()
                if (response.body() != null) {
                    val measures = response.body()
                    val contents = measures?.content

                    contents?.removeAt(0)
                    contents?.removeAt(contents.size - 1)
                    if (contents != null) measures.content = contents

                    mainHandler.post { onItemReady.onItem(measures, "") }
                } else {
                    mainHandler.post {
                        try {
                            onItemReady.onItem(null, response.errorBody()?.string() ?: "Unknown error")
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            } catch (e: IOException) {
                mainHandler.post { onItemReady.onItem(null, e.toString()) }
                e.printStackTrace()
            }
        }
    }

    @UiThread
    fun getFrequentlyAskedQuestions(onItemReady: OnItemReady<FAQ?>) {
        val mainHandler = Handler(Looper.getMainLooper())
        executors.execute {
            try {
                val response = contentCovidAPI.getFrequentlyAskedQuestions().execute()
                if (response.body() != null) {
                    mainHandler.post { onItemReady.onItem(response.body(), "") }
                } else {
                    mainHandler.post {
                        try {
                            onItemReady.onItem(null, response.errorBody()?.string() ?: "Unknown error")
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            } catch (e: IOException) {
                mainHandler.post { onItemReady.onItem(null, e.toString()) }
                e.printStackTrace()
            }
        }
    }

    private suspend fun blockToRunInBackground(activity: WeakReference<Context>): List<StatRecyclerItem> {
        return withContext(Dispatchers.IO) {
            val returnable: MutableList<StatRecyclerItem> = ArrayList()

            try {
                val (_, _, _, deceased, tested, confirmed, etRecovered) = pmoCovidAPI.getCases()[0]
                returnable.add(StatRecyclerItem(
                        activity.get()?.getString(R.string.ethiopia) ?: "",
                        confirmed, deceased,
                        etRecovered, tested)
                )
            } catch (ignored: Exception) { Timber.e(ignored) }

            try {
                val regions: MutableMap<String, Region> = HashMap()
                val patients = pmoCovidAPI.getPatients()

                for ((_, _, location, _, _, _, _, status) in patients.results) {
                    if (regions.containsKey(location)) {
                        regions[location]?.numberOfInfected = regions[location]?.numberOfInfected?.inc() ?: 0
                    } else {
                        regions[location] = Region(
                                when {
                                    location.contains("Southern") -> "SNNPR"
                                    location.contains("Benishangul") -> "Benishangul"
                                    else -> location
                                },
                                Constant.regionNameWithCodeMap[location] ?: "un",
                                1,
                                if (status == "Deceased") 1 else 0
                        )
                    }
                }

                val values: MutableList<Int> = ArrayList()
                val regionCodes: MutableList<String> = ArrayList()
                for ((_, value) in regions) {
                    values.add(value.numberOfInfected)
                    regionCodes.add(value.regionName)
                }

                returnable.add(
                        StatRecyclerItem(
                                activity.get()?.getString(R.string.regions_affected),
                                values, regionCodes, generateColors(values.size)
                        )
                )

                returnable.add(
                        StatRecyclerItem(
                                listOf(
                                        activity.get()?.getString(R.string.id) ?: "",
                                        activity.get()?.getString(R.string.name) ?: "",
                                        activity.get()?.getString(R.string.location) ?: "",
                                        activity.get()?.getString(R.string.age) ?: "",
                                        activity.get()?.getString(R.string.gender) ?: "",
                                        activity.get()?.getString(R.string.nationality) ?: "",
                                        activity.get()?.getString(R.string.recent_travel) ?: "",
                                        activity.get()?.getString(R.string.status) ?: ""
                                ),
                                1,
                                patients.results
                        )
                )
            } catch (ignored: Exception) { Timber.e(ignored) }

            try {
                val johnsHopkinsItem = worldCovidAPI.getCountryHistoricalData("et")
                val caseNumbers: MutableList<Int> = ArrayList()
                val deathNumbers: MutableList<Int> = ArrayList()
                val recoveryNumbers: MutableList<Int> = ArrayList()
                val caseDate: MutableList<String> = ArrayList()

                for ((key, value) in johnsHopkinsItem.timeline.cases) {
                    caseDate.add(key)
                    caseNumbers.add(value)
                }

                for ((_, value) in johnsHopkinsItem.timeline.deaths) {
                    deathNumbers.add(value)
                }

                for ((_, value) in johnsHopkinsItem.timeline.recovered) {
                    recoveryNumbers.add(value)
                }

                returnable.add(
                        StatRecyclerItem(
                                activity.get()?.getString(R.string.et_covid_dist),
                                listOf(
                                        LineChartItem(
                                                caseNumbers,
                                                activity.get()?.getString(R.string.cases)
                                                        ?: "Cases",
                                                ContextCompat.getColor(instance, R.color.purple_0),
                                                ContextCompat.getColor(instance, R.color.purple_1)
                                        ),
                                        LineChartItem(
                                                deathNumbers,
                                                activity.get()?.getString(R.string.deaths)
                                                        ?: "Deaths",
                                                ContextCompat.getColor(instance, R.color.red_0),
                                                ContextCompat.getColor(instance, R.color.red_1)
                                        ),
                                        LineChartItem(
                                                recoveryNumbers,
                                                activity.get()?.getString(R.string.recovery)
                                                        ?: "Recovery",
                                                ContextCompat.getColor(instance, R.color.green_0),
                                                ContextCompat.getColor(instance, R.color.green_1)
                                        )
                                ),
                                caseDate
                        )
                )
            } catch (ignored: Exception) { Timber.e(ignored) }

            try {
                val worldStat = worldCovidAPI.getListOfStat()
                val worldStatItems: MutableList<CovidStatItem?> = ArrayList()
                for (
                (country, cases, _, deaths, _, recovered, active, critical, casesPerOneMillion, deathsPerOnMillion)
                in worldStat
                ) {
                    worldStatItems.add(
                            CovidStatItem(
                                    country.replace(" ", ""),
                                    cases,
                                    active,
                                    deaths,
                                    recovered,
                                    critical,
                                    casesPerOneMillion,
                                    deathsPerOnMillion
                            )
                    )
                }

                returnable.add(StatRecyclerItem(
                        0,
                        worldStatItems,
                        listOf(
                                activity.get()?.getString(R.string.country) ?: "",
                                activity.get()?.getString(R.string.infected) ?: "",
                                activity.get()?.getString(R.string.active) ?: "",
                                activity.get()?.getString(R.string.death) ?: "",
                                activity.get()?.getString(R.string.recovery) ?: "",
                                activity.get()?.getString(R.string.critical) ?: "",
                                activity.get()?.getString(R.string.case_per_mil) ?: "",
                                activity.get()?.getString(R.string.death_per_mil) ?: ""
                        ),
                        1
                ))
            } catch (ignored: Exception) { Timber.e(ignored) }

            returnable
        }
    }

    @UiThread
    fun getStatRecyclerContents(activity: WeakReference<Context>, onItemReady: OnItemReady<List<StatRecyclerItem?>?>) {
        CoroutineScope(Dispatchers.Main).launch {
            val list = blockToRunInBackground(activity)
            onItemReady.onItem(list, "")
        }
    }

    @UiThread
    fun getLatestNews(onItemReady: OnItemReady<List<NewsItem>?>) {
        val mainHandler = Handler(Looper.getMainLooper())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = newsSourceAPI.getLatest()
                mainHandler.post { onItemReady.onItem(response, "") }
            } catch (e: IOException) {
                mainHandler.post { onItemReady.onItem(null, e.toString()) }
                e.printStackTrace()
            }
        }
    }

    @UiThread
    fun getLatestNews(beforeItemId: Int, onItemReady: OnItemReady<List<NewsItem>?>) {
        val mainHandler = Handler(Looper.getMainLooper())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = newsSourceAPI.getBeforeItem(beforeItemId)
                mainHandler.post { onItemReady.onItem(response, "") }
            } catch (e: IOException) {
                mainHandler.post { onItemReady.onItem(null, e.toString()) }
                e.printStackTrace()
            }
        }
    }

    companion object {
        private lateinit var executors: ExecutorService

        interface ConjureBackground<T> {
            fun blockToRunInBackground(): T
        }

        interface ConjureForeground<T> {
            fun blockToRunOnMainThread(content: T, err: Throwable?)
        }

        @JvmStatic
        fun <T> conjure(onBackground: ConjureBackground<T>, onForeground: ConjureForeground<T?>) {
            val mainHandler = Handler(Looper.getMainLooper())
            executors.execute {
                try {
                    val returnable = onBackground.blockToRunInBackground()
                    mainHandler.post { onForeground.blockToRunOnMainThread(returnable, null) }
                } catch (e: java.lang.Exception) {
                    mainHandler.post { onForeground.blockToRunOnMainThread(null, e) }
                    e.printStackTrace()
                }
            }
        }
    }

    init {
        val okHttpClient = OkHttpClient.Builder()
                .addNetworkInterceptor(ChuckerInterceptor(instance))
                .build()
        val retrofit = Retrofit.Builder().baseUrl("https://api.pmo.gov.et/v1/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val retrofitWorld = Retrofit.Builder().baseUrl("https://corona.lmao.ninja/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val retrofitContent = Retrofit.Builder().baseUrl("https://covid19-news.herokuapp.com/api/covid19/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val retrofitNews = Retrofit.Builder().baseUrl("https://covidn.herokuapp.com/tikvah/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        pmoCovidAPI = retrofit.create(PMOCovidAPI::class.java)
        worldCovidAPI = retrofitWorld.create(WorldCovidAPI::class.java)
        contentCovidAPI = retrofitContent.create(ContentCovidAPI::class.java)
        newsSourceAPI = retrofitNews.create(NewsSourceAPI::class.java)
        executors = Executors.newFixedThreadPool(5)
    }
}