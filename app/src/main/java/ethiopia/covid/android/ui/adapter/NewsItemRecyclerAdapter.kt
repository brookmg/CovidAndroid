package ethiopia.covid.android.ui.adapter

import android.graphics.Color
import android.net.Uri
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.material.button.MaterialButton
import com.luseen.autolinklibrary.AutoLinkMode
import com.luseen.autolinklibrary.AutoLinkTextView
import ethiopia.covid.android.R
import ethiopia.covid.android.data.NewsItem
import ethiopia.covid.android.util.Utils.getImagesForContent
import ethiopia.covid.android.util.Utils.openUrlInCustomTab
import im.ene.toro.ToroPlayer
import im.ene.toro.ToroUtil
import im.ene.toro.exoplayer.ExoPlayerViewHelper
import im.ene.toro.media.PlaybackInfo
import im.ene.toro.widget.Container
import timber.log.Timber
import java.util.*

/**
 * Created by BrookMG on 3/7/2019 in ethiopia.covid.android.ui.adapters
 * inside the project CovidEt .
 */
class NewsItemRecyclerAdapter @JvmOverloads constructor(private val newsItems: MutableList<NewsItem>, private val onLastItemReachedListener: OnLastItemReachedListener? = null, private val onImageItemClicked: OnImageItemClicked? = null) : RecyclerView.Adapter<NewsItemRecyclerAdapter.ViewHolder>() {
    private val scrollSpeed = 5000
    private val handler = Handler()
    private val solidTimer: Timer? = null

    interface OnLastItemReachedListener {
        fun onLastItemReached()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(newsItems[position])
        if (position == newsItems.size - 1) {
            onLastItemReachedListener?.onLastItemReached()
        }
    }

    private fun getTelegramDeepLinkForMention(mention: String): String {
        val returnable = "tg:resolve?domain=" + mention.replace("@", "")
                .replace(" ", "").trim { it <= ' ' }
        Timber.e(returnable)
        return returnable
    }

    fun loadMoreOnBottom(news: List<NewsItem>?) {
        if (news == null) return
        val currentPosition = newsItems.size - 1
        newsItems.addAll(news)
        notifyItemRangeInserted(currentPosition + 1, news.size + currentPosition + 1)
    }

    fun populateNewsItems(news: List<NewsItem>?) {
        if (news == null) return
        newsItems.clear()
        newsItems.addAll(news)
        notifyItemRangeInserted(0, news.size)
    }

    fun idOfLastNews(): Int {
        return newsItems[newsItems.size - 1].id
    }

    override fun getItemCount(): Int {
        return newsItems.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), ToroPlayer {

        var imagesRecyclerView: RecyclerView = itemView.findViewById(R.id.image_recycler_view)
        var title: AppCompatTextView = itemView.findViewById(R.id.news_title)
        var content: AutoLinkTextView = itemView.findViewById(R.id.news_payload)
        var telegram: MaterialButton = itemView.findViewById(R.id.telegram)
        var playerView: PlayerView = itemView.findViewById(R.id.player_view)

        var helper: ExoPlayerViewHelper? = null
        private lateinit var mediaUri: Uri

        fun bind(item: NewsItem) {
            mediaUri = Uri.parse(item.video)
            title.text = String.format("%s...", item.content.substring(0, item.content.length / 4))

            if (item.image.isNotEmpty()) {
                imagesRecyclerView.layoutManager = ScrollingLinearLayoutManager(itemView.context,
                        LinearLayoutManager.HORIZONTAL, false, 450)
                imagesRecyclerView.adapter = ImageListRecyclerAdapter(
                        getImagesForContent(item.image), onImageItemClicked
                )
                imagesRecyclerView.visibility = View.VISIBLE
            } else imagesRecyclerView.visibility = View.GONE

            if (item.video.isNotEmpty()) {
                playerView.visibility = View.VISIBLE
            } else playerView.visibility = View.GONE

            content.addAutoLinkMode(AutoLinkMode.MODE_HASHTAG, AutoLinkMode.MODE_MENTION, AutoLinkMode.MODE_URL)
            content.setMentionModeColor(Color.parseColor("#35F984"))
            content.setHashtagModeColor(Color.parseColor("#EA4444"))
            content.setUrlModeColor(Color.parseColor("#CA59EF"))

            content.setAutoLinkOnClickListener { autoLinkMode: AutoLinkMode?, matchedText: String ->
                when (autoLinkMode) {
                    AutoLinkMode.MODE_URL -> openUrlInCustomTab(itemView.context, matchedText)
                    AutoLinkMode.MODE_MENTION -> openUrlInCustomTab(itemView.context, getTelegramDeepLinkForMention(matchedText))
                    else -> {}
                }
            }

            content.setAutoLinkText(item.content)
            telegram.setOnClickListener {
                openUrlInCustomTab(itemView.context, getTelegramDeepLinkForMention("@tikvahethiopia&post=" + item.id))
            }

        }

        override fun getPlayerView(): View {
            return playerView
        }

        override fun getCurrentPlaybackInfo(): PlaybackInfo {
            return helper?.latestPlaybackInfo ?: PlaybackInfo()
        }

        override fun initialize(container: Container, playbackInfo: PlaybackInfo) {
            if (helper == null) helper = ExoPlayerViewHelper(this, mediaUri)
            helper?.initialize(container, playbackInfo)

        }

        override fun release() {
            helper?.release()
            helper = null
        }

        override fun play() {
            helper?.play()
        }

        override fun pause() {
            helper?.pause()
        }

        override fun isPlaying(): Boolean {
            return helper?.isPlaying ?: false
        }

        override fun wantsToPlay(): Boolean {
            return ToroUtil.visibleAreaOffset(this, itemView.parent) >= 0.85
        }

        override fun getPlayerOrder(): Int {
            return adapterPosition
        }

    }

}