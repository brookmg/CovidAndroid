package ethiopia.covid.android.data

import com.google.gson.annotations.SerializedName

/**
 * Created by BrookMG on 9/27/2019 in
 * inside the project .
 */
data class NewsItem(@SerializedName("_id") var id: Int, var content: String, var image: String,
                    var video: String, var author: String, var datetime: String, var views: String)