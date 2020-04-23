package ethiopia.covid.android.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.luseen.autolinklibrary.AutoLinkMode;
import com.luseen.autolinklibrary.AutoLinkOnClickListener;
import com.luseen.autolinklibrary.AutoLinkTextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ethiopia.covid.android.App;
import ethiopia.covid.android.R;
import ethiopia.covid.android.data.NewsItem;
import ethiopia.covid.android.util.Utils;
import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.exoplayer.ExoPlayerViewHelper;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;
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
        holder.bind(newsItems.get(position));

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

    public void loadMoreOnBottom(List<NewsItem> news) {
        if (news == null) return;

        int currentPosition = newsItems.size() - 1;
        newsItems.addAll(news);
        notifyItemRangeInserted(currentPosition+1 , news.size() + currentPosition +1);
    }

    public void populateNewsItems(List<NewsItem> news) {
        if (news == null) return;

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

    class ViewHolder extends RecyclerView.ViewHolder implements ToroPlayer {

        RecyclerView imagesRecyclerView;
        AppCompatTextView title;
        AutoLinkTextView content;
        MaterialButton telegram;
        PlayerView playerView;
        @Nullable ExoPlayerViewHelper helper;
        @Nullable private Uri mediaUri;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.player_view);
            imagesRecyclerView = itemView.findViewById(R.id.image_recycler_view);
            title = itemView.findViewById(R.id.news_title);
            content = itemView.findViewById(R.id.news_payload);
            telegram = itemView.findViewById(R.id.telegram);
        }

        void bind(@NonNull NewsItem item) {
            mediaUri = Uri.parse(item.getVideo());

            title.setText(String.format("%s...", item.getContent().substring(0, item.getContent().length() / 4)));

            if (item.getImage() != null && !item.getImage().isEmpty()) {
                imagesRecyclerView.setLayoutManager(new ScrollingLinearLayoutManager(itemView.getContext() ,
                        LinearLayoutManager.HORIZONTAL , false , 450));
                imagesRecyclerView.setAdapter(new ImageListRecyclerAdapter(
                        Utils.getImagesForContent(item.getImage()), onImageItemClicked
                ));

                imagesRecyclerView.setVisibility(View.VISIBLE);
            } else imagesRecyclerView.setVisibility(View.GONE);

            if (item.getVideo() != null && !item.getVideo().isEmpty()) {
                playerView.setVisibility(View.VISIBLE);
            } else playerView.setVisibility(View.GONE);

            content.addAutoLinkMode(
                    MODE_HASHTAG,
                    MODE_MENTION,
                    MODE_URL
            );

            content.setMentionModeColor(Color.parseColor("#35F984"));
            content.setHashtagModeColor(Color.parseColor("#EA4444"));
            content.setUrlModeColor(Color.parseColor("#CA59EF"));
            content.setAutoLinkOnClickListener((autoLinkMode, matchedText) -> {
                switch (autoLinkMode) {
                    case MODE_URL:
                        openUrlInCustomTab(itemView.getContext() , matchedText);
                        break;
                    case MODE_MENTION:
                        openUrlInCustomTab(itemView.getContext() , getTelegramDeepLinkForMention(matchedText));
                        break;
                }
            });
            content.setAutoLinkText(item.getContent());
        }

        @NonNull
        @Override
        public View getPlayerView() {
            return playerView;
        }

        @NonNull @Override public PlaybackInfo getCurrentPlaybackInfo() {
            return helper != null ? helper.getLatestPlaybackInfo() : new PlaybackInfo();
        }

        @Override
        public void initialize(@NonNull Container container, @Nullable PlaybackInfo playbackInfo) {
            if (helper == null && mediaUri != null) helper = new ExoPlayerViewHelper(this, mediaUri);
            if (playbackInfo != null && helper != null) {
                helper.initialize(container, playbackInfo);
            }
        }

        @Override public void release() {
            if (helper != null) {
                helper.release();
                helper = null;
            }
        }

        @Override public void play() {
            if (helper != null) helper.play();
        }

        @Override public void pause() {
            if (helper != null) helper.pause();
        }

        @Override public boolean isPlaying() {
            return helper != null && helper.isPlaying();
        }

        @Override public boolean wantsToPlay() {
            return ToroUtil.visibleAreaOffset(this, itemView.getParent()) >= 0.85;
        }

        @Override public int getPlayerOrder() {
            return getAdapterPosition();
        }

    }

}
