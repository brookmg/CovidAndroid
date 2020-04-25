package ethiopia.covid.android.network;

import java.util.List;

import ethiopia.covid.android.data.NewsItem;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by BrookMG on 4/23/2020 in ethiopia.covid.android.network
 * inside the project CoVidEt .
 */
public interface NewsSourceAPI {

    @GET("latest?limit=10")
    public Call<List<NewsItem>> getLatest();

    @GET("before?limit=10")
    public Call<List<NewsItem>> getBeforeItem(@Query("before") int itemId);

}
