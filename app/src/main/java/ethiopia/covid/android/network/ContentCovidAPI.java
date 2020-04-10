package ethiopia.covid.android.network;

import ethiopia.covid.android.data.FAQ;
import ethiopia.covid.android.data.ProtectiveMeasures;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by BrookMG on 4/10/2020 in ethiopia.covid.android.network
 * inside the project CoVidEt .
 */
public interface ContentCovidAPI {

    @GET("protective-measures") Call<ProtectiveMeasures> getProtectiveMeasures();
    @GET("faqs") Call<FAQ> getFrequentlyAskedQuestions();

}
