package ethiopia.covid.android.network;

import java.util.List;

import ethiopia.covid.android.data.NewsItem;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by BrookMG on 4/23/2020 in ethiopia.covid.android.network
 * inside the project CoVidEt .
 */
public interface NewsSourceAPI {

    @GET("latest")
    public Call<List<NewsItem>> getLatest();

    @GET("before/{itemId}")
    public Call<List<NewsItem>> getBeforeItem(@Path("itemId") int itemId);

}
