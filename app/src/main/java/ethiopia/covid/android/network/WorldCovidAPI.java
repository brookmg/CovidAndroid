package ethiopia.covid.android.network;

import ethiopia.covid.android.data.WorldCovid;
import ethiopia.covid.android.util.Secrets;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by BrookMG on 3/25/2020 in ethiopia.covid.android.network
inside the project CoVidEt .
 */
public interface WorldCovidAPI {

    @GET("data?IG=" + Secrets.BING_DATA_API_KEY) public Call<WorldCovid> getListOfStat();

}