package ethiopia.covid.android.network;

import java.util.List;

import ethiopia.covid.android.data.Case;
import ethiopia.covid.android.data.Patients;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by BrookMG on 3/25/2020 in ethiopia.covid.android.network
inside the project CoVidEt .
 */
public interface PMOCovidAPI {

    @GET("cases") public Call<List<Case>> getCases();
    @GET("patients?limit=100&page=0") public Call<Patients> getPatients();

}