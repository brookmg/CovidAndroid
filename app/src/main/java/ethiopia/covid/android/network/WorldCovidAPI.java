package ethiopia.covid.android.network;

import java.util.List;

import ethiopia.covid.android.data.JohnsHopkinsItem;
import ethiopia.covid.android.data.WorldCovid;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by BrookMG on 3/25/2020 in ethiopia.covid.android.network
inside the project CoVidEt .
 */
public interface WorldCovidAPI {

    @GET("v2/countries") public Call<List<WorldCovid>> getListOfStat();
    @GET("v2/historical/{country}?limit=30") public Call<JohnsHopkinsItem> getCountryHistoricalData(@Path("country") String country);

}