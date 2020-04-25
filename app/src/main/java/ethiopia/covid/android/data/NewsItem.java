package ethiopia.covid.android.data;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by BrookMG on 9/27/2019 in
 * inside the project .
 */
public class NewsItem {

    @SerializedName("_id")
    private Integer id;
    private String content;
    private String image;
    private String video;
    private String author;
    private String datetime;
    private String views;

    public NewsItem(Integer id, String content, String image, String video, String author, String datetime, String views) {
        this.id = id;
        this.content = content;
        this.image = image;
        this.video = video;
        this.author = author;
        this.datetime = datetime;
        this.views = views;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
