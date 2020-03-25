package ethiopia.covid.android.network;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.UiThread;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ethiopia.covid.android.data.Case;
import ethiopia.covid.android.data.Patients;
import ethiopia.covid.android.data.WorldCovid;
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
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.pmo.gov.et/v1/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Retrofit retrofitWorld = new Retrofit.Builder().baseUrl("https://www.bing.com/covid/")
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
    public void getWorldStat(OnItemReady<WorldCovid> onItemReady) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        executors.execute(() -> {
            try {
                Response<WorldCovid> response = worldCovidAPI.getListOfStat().execute();
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

    public PMOCovidAPI getPmoCovidAPI() {
        return pmoCovidAPI;
    }

    public WorldCovidAPI getWorldCovidAPI() {
        return worldCovidAPI;
    }
}