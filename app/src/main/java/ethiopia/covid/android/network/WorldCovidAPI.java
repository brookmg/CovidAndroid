package ethiopia.covid.android.network;

import ethiopia.covid.android.data.WorldCovid;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by BrookMG on 3/25/2020 in ethiopia.covid.android.network
inside the project CoVidEt .
 */
public interface WorldCovidAPI {

    @GET("data") public Call<WorldCovid> getListOfStat(@Query("IG") String apiKey);

}