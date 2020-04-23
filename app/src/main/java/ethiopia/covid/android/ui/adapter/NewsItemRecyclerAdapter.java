package ethiopia.covid.android.ui.adapter;

import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.luseen.autolinklibrary.AutoLinkMode;
import com.luseen.autolinklibrary.AutoLinkOnClickListener;
import com.luseen.autolinklibrary.AutoLinkTextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ethiopia.covid.android.R;
import ethiopia.covid.android.data.NewsItem;
import ethiopia.covid.android.util.Utils;
import timber.log.Timber;

import static com.luseen.autolinklibrary.AutoLinkMode.MODE_HASHTAG;
import static com.luseen.autolinklibrary.AutoLinkMode.MODE_MENTION;
import static com.luseen.autolinklibrary.AutoLinkMode.MODE_URL;
import static ethiopia.covid.android.util.Utils.openUrlInCustomTab;

/**
 * Created by BrookMG on 3/7/2019 in ethiopia.covid.android.ui.adapters
 * inside the project CovidEt .
 */
public class NewsItemRecyclerAdapter extends RecyclerView.Adapter<NewsItemRecyclerAdapter.ViewHolder> {

    private final int scrollSpeed = 5_000;
    private final Handler handler = new Handler();
    private Timer solidTimer;
    private List<NewsItem> newsItems;
    private OnImageItemClicked onImageItemClicked;

    public interface OnLastItemReachedListener {
        void OnLastItemReached();
    }

    private OnLastItemReachedListener onLastItemReachedListener;

    public NewsItemRecyclerAdapter(List<NewsItem> newsItems) {
        this(newsItems , null);
    }

    public NewsItemRecyclerAdapter(List<NewsItem> newsItems, OnLastItemReachedListener onLastItemReachedListener) {
        this(newsItems , onLastItemReachedListener, null);
    }

    public NewsItemRecyclerAdapter(List<NewsItem> newsItems, OnLastItemReachedListener onLastItemReachedListener, OnImageItemClicked onImageItemClicked) {
        this.newsItems = newsItems;
        this.onLastItemReachedListener = onLastItemReachedListener;
        this.onImageItemClicked = onImageItemClicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(String.format("%s...", newsItems.get(position).getContent().substring(0, newsItems.get(position).getContent().length() / 4)));

        if (newsItems.get(position).getImage() != null && !newsItems.get(position).getImage().isEmpty()) {
            holder.imagesRecyclerView.setLayoutManager(new ScrollingLinearLayoutManager(holder.itemView.getContext() ,
                    LinearLayoutManager.HORIZONTAL , false , 450));
            holder.imagesRecyclerView.setAdapter(new ImageListRecyclerAdapter(
                    Utils.getImagesForContent(newsItems.get(position).getImage()), onImageItemClicked
            ));

            holder.imagesRecyclerView.setVisibility(View.VISIBLE);
        } else holder.imagesRecyclerView.setVisibility(View.GONE);

        holder.content.addAutoLinkMode(
                MODE_HASHTAG,
                MODE_MENTION,
                MODE_URL
        );

        holder.content.setMentionModeColor(Color.parseColor("#35F984"));
        holder.content.setHashtagModeColor(Color.parseColor("#EA4444"));
        holder.content.setUrlModeColor(Color.parseColor("#CA59EF"));
        holder.content.setAutoLinkOnClickListener((autoLinkMode, matchedText) -> {
            switch (autoLinkMode) {
                case MODE_URL:
                    openUrlInCustomTab(holder.itemView.getContext() , matchedText);
                    break;
                case MODE_MENTION:
                    openUrlInCustomTab(holder.itemView.getContext() , getTelegramDeepLinkForMention(matchedText));
                    break;
            }
        });
        holder.content.setAutoLinkText(newsItems.get(position).getContent());

        if (position == newsItems.size() - 1) {
            if (onLastItemReachedListener != null)
                onLastItemReachedListener.OnLastItemReached();
        }
    }

    private String getTelegramDeepLinkForMention(String mention) {
        String returnable = "tg:resolve?domain=" + mention.replace("@" , "")
                .replace(" " , "");
        Timber.e(returnable);
        return returnable;
    }

    // TODO: 10/9/2019 ADD A NEW METHOD THAT WILL HANDLE HASHTAG CLICKS ... THEY SHOULD BE REDIRECTED TO NEW SCREEN WITH SEARCH

    public void loadMoreOnBottom(List<NewsItem> news) {
        int currentPosition = newsItems.size() - 1;
        newsItems.addAll(news);
        notifyItemRangeInserted(currentPosition+1 , news.size() + currentPosition +1);
    }

    public void populateNewsItems(List<NewsItem> news) {
        newsItems.clear();
        newsItems.addAll(news);
        notifyItemRangeInserted(0 , news.size());
    }

    public int idOfLastNews() {
        return newsItems.get(newsItems.size() - 1).getId();
    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerView imagesRecyclerView;
        AppCompatTextView title;
        AutoLinkTextView content;
        MaterialButton telegram;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagesRecyclerView = itemView.findViewById(R.id.image_recycler_view);
            title = itemView.findViewById(R.id.news_title);
            content = itemView.findViewById(R.id.news_payload);
            telegram = itemView.findViewById(R.id.telegram);
        }

    }

}
