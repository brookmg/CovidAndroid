package ethiopia.covid.android.network;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.UiThread;
import androidx.core.content.ContextCompat;

import com.chuckerteam.chucker.api.ChuckerInterceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ethiopia.covid.android.App;
import ethiopia.covid.android.BuildConfig;
import ethiopia.covid.android.R;
import ethiopia.covid.android.data.Case;
import ethiopia.covid.android.data.CovidStatItem;
import ethiopia.covid.android.data.JohnsHopkinsItem;
import ethiopia.covid.android.data.LineChartItem;
import ethiopia.covid.android.data.PatientItem;
import ethiopia.covid.android.data.Patients;
import ethiopia.covid.android.data.Region;
import ethiopia.covid.android.data.StatRecyclerItem;
import ethiopia.covid.android.data.WorldCovid;
import ethiopia.covid.android.util.Constant;
import ethiopia.covid.android.util.Utils;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by BrookMG on 3/25/2020 in ethiopia.covid.android.network
 * inside the project CoVidEt .
 */
public class API {
    private PMOCovidAPI pmoCovidAPI;
    private WorldCovidAPI worldCovidAPI;
    private static ExecutorService executors;

    public API() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new ChuckerInterceptor(App.getInstance()))
                .build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.pmo.gov.et/v1/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Retrofit retrofitWorld = new Retrofit.Builder().baseUrl("https://corona.lmao.ninja/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        pmoCovidAPI = retrofit.create(PMOCovidAPI.class);
        worldCovidAPI = retrofitWorld.create(WorldCovidAPI.class);
        executors = Executors.newFixedThreadPool(5);
    }

    public interface OnItemReady<T> { void onItem(T item, String err); }

    @UiThread
    public void getCases(OnItemReady<Case> onCaseReady) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        executors.execute(() -> {
            try {
                Response<List<Case>> response = pmoCovidAPI.getCases().execute();
                if (response.body() != null && response.body().size() > 0) {
                    mainHandler.post(() -> onCaseReady.onItem(response.body().get(0), ""));
                } else {
                    mainHandler.post(() -> {
                        try {
                            onCaseReady.onItem(null, response.errorBody() != null ? response.errorBody().string() : "Unknown error");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            } catch (IOException e) {
                mainHandler.post(() -> onCaseReady.onItem(null , e.toString()));
                e.printStackTrace();
            }
        });
    }

    @UiThread
    public void getPatients(OnItemReady<Patients> onCaseReady) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        executors.execute(() -> {
            try {
                Response<Patients> response = pmoCovidAPI.getPatients().execute();
                if (response.body() != null && response.body().getResults().size() > 0) {
                    mainHandler.post(() -> onCaseReady.onItem(response.body(), ""));
                } else {
                    mainHandler.post(() -> {
                        try {
                            onCaseReady.onItem(null, response.errorBody() != null ? response.errorBody().string() : "Unknown error");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            } catch (IOException e) {
                mainHandler.post(() -> onCaseReady.onItem(null , e.toString()));
                e.printStackTrace();
            }
        });
    }
    
    @UiThread
    public void getWorldStat(OnItemReady<List<WorldCovid>> onItemReady) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        executors.execute(() -> {
            try {
                Response<List<WorldCovid>> response = worldCovidAPI.getListOfStat().execute();
                if (response.body() != null) {
                    mainHandler.post(() -> onItemReady.onItem(response.body(), ""));
                } else {
                    mainHandler.post(() -> {
                        try {
                            onItemReady.onItem(null, response.errorBody() != null ? response.errorBody().string() : "Unknown error");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            } catch (IOException e) {
                mainHandler.post(() -> onItemReady.onItem(null , e.toString()));
                e.printStackTrace();
            }
        });
    }

    @UiThread
    public void getStatRecyclerContents(OnItemReady<List<StatRecyclerItem>> onItemReady) {

        conjure(() -> {
            List<StatRecyclerItem> returnable = new ArrayList<>();

            try {
                Case caseItem = getPmoCovidAPI().getCases().execute().body().get(0);
                returnable.add(new StatRecyclerItem("Ethiopia" , caseItem.getTotal(),
                        caseItem.getDeceased(), caseItem.getRecovered(), caseItem.getTested()));
            } catch (Exception ignored) {}

            try {

                Map<String, Region> regions = new HashMap<>();
                Patients patients = getPmoCovidAPI().getPatients().execute().body();

                for (PatientItem item : patients != null ?
                        patients.getResults() : new ArrayList<PatientItem>()) {
                    if (regions.containsKey(item.getLocation())) {
                        regions.get(item.getLocation()).setNumberOfInfected(
                                regions.get(item.getLocation()).getNumberOfInfected() + 1
                        );
                    } else {
                        String location = item.getLocation();
                        regions.put(item.getLocation() ,
                                new Region(
                                    item.getLocation(),
                                    Constant.regionNameWithCodeMap.get(location),
                                    1,
                                    item.getStatus().equals("Deceased") ? 1 : 0
                                )
                        );
                    }
                }

                List<Integer> values = new ArrayList<>();
                List<String> regionCodes = new ArrayList<>();

                for (Entry<String, Region> item : regions.entrySet()) {
                    values.add(item.getValue().getNumberOfInfected());
                    regionCodes.add(item.getValue().getRegionName());
                }

                returnable.add(
                        new StatRecyclerItem(
                                App.getInstance().getString(R.string.regions_affected) , values, regionCodes, Utils.generateColors(values.size())
                        )
                );

                returnable.add(
                        new StatRecyclerItem(
                                Arrays.asList("ID" , "Name", "Location", "Age", "Gender" , "Nationality", "RecentTravel", "Status"),
                                1,
                                patients.getResults()
                        )
                );
            } catch (Exception ignored) {

            }

            try {
                Response<JohnsHopkinsItem> response = getWorldCovidAPI().getCountryHistoricalData("et").execute();
                JohnsHopkinsItem johnsHopkinsItem = response.body();
                List<Integer> caseNumbers = new ArrayList<>();
                List<Integer> deathNumbers = new ArrayList<>();
                List<Integer> recoveryNumbers = new ArrayList<>();
                List<String> caseDate = new ArrayList<>();

                for (Entry<String, Integer> item : johnsHopkinsItem.getTimeline().getCases().entrySet()) {
                    caseDate.add(item.getKey());
                    caseNumbers.add(item.getValue());
                }

                for (Entry<String, Integer> item : johnsHopkinsItem.getTimeline().getDeaths().entrySet()) {
                    deathNumbers.add(item.getValue());
                }

                for (Entry<String, Integer> item : johnsHopkinsItem.getTimeline().getRecovered().entrySet()) {
                    recoveryNumbers.add(item.getValue());
                }

                returnable.add(
                        new StatRecyclerItem(
                                "Ethiopia Covid Distribution",
                                Arrays.asList(
                                        new LineChartItem(
                                                caseNumbers,
                                                "Cases",
                                                ContextCompat.getColor(App.getInstance() , R.color.purple_0),
                                                ContextCompat.getColor(App.getInstance() , R.color.purple_1)
                                        ),
                                        new LineChartItem(
                                                deathNumbers,
                                                "Deaths",
                                                ContextCompat.getColor(App.getInstance() , R.color.red_0),
                                                ContextCompat.getColor(App.getInstance() , R.color.red_1)
                                        ),
                                        new LineChartItem(
                                                recoveryNumbers,
                                                "Recovery",
                                                ContextCompat.getColor(App.getInstance() , R.color.green_0),
                                                ContextCompat.getColor(App.getInstance() , R.color.green_1)
                                        )
                                ),
                                caseDate
                        )
                );

            } catch (Exception ignored) {
                Log.e("Graph" , ignored.getMessage());
            }

            try {
                Response<List<WorldCovid>> worldStatResponse = getWorldCovidAPI().getListOfStat().execute();
                List<WorldCovid> worldStat = worldStatResponse.body();
                List<CovidStatItem> worldStatItems = new ArrayList<>();

                for (WorldCovid location : worldStat != null ? worldStat : new ArrayList<WorldCovid>()) {
                    worldStatItems.add(
                            new CovidStatItem(
                                    location.getCountry().replace(" ", ""),
                                    location.getCases(),
                                    location.getActive(),
                                    location.getDeaths(),
                                    location.getRecovered(),
                                    location.getCritical(),
                                    location.getCasesPerOneMillion(),
                                    location.getDeathsPerOnMillion()
                            )
                    );
                }

                returnable.add(new StatRecyclerItem(
                        0,
                        worldStatItems,
                        Arrays.asList("Country" , "Infected", "Active", "Death", "Recovered" , "Critical", "Case/1M", "Death/1M"),
                        1
                ));
            } catch (Exception err) {
                Log.e("NETWORK" , err.getMessage());
            }

            return returnable;
        }, (content, err) -> onItemReady.onItem(content, err != null ? err.toString() : ""));

    }

    public interface ConjureBackground<T> { T blockToRunInBackground(); }
    public interface ConjureForeground<T> { void blockToRunOnMainThread(T content, Throwable err); }

    public static <T> void conjure(ConjureBackground<T> onBackground, ConjureForeground<T> onForeground) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        executors.execute(() -> {
            try {
                T returnable = onBackground.blockToRunInBackground();
                mainHandler.post(() -> onForeground.blockToRunOnMainThread(returnable, null));
            } catch (Exception e) {
                mainHandler.post(() -> onForeground.blockToRunOnMainThread(null, e));
                e.printStackTrace();
            }
        });
    }

    public PMOCovidAPI getPmoCovidAPI() {
        return pmoCovidAPI;
    }

    public WorldCovidAPI getWorldCovidAPI() {
        return worldCovidAPI;
    }
}