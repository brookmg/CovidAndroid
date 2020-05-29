package ethiopia.covid.android.ui.adapter

import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luseen.autolinklibrary.AutoLinkMode
import ethiopia.covid.android.data.NewsItem
import ethiopia.covid.android.databinding.NewsItemBinding
import ethiopia.covid.android.util.Utils.getImagesForContent
import ethiopia.covid.android.util.Utils.openUrlInCustomTab
import im.ene.toro.ToroPlayer
import im.ene.toro.ToroUtil
import im.ene.toro.exoplayer.ExoPlayerViewHelper
import im.ene.toro.media.PlaybackInfo
import im.ene.toro.widget.Container
import timber.log.Timber

/**
 * Created by BrookMG on 3/7/2019 in ethiopia.covid.android.ui.adapters
 * inside the project CovidEt .
 */
class NewsItemRecyclerAdapter @JvmOverloads constructor(
        private val newsItems: MutableList<NewsItem>,
        private val onLastItemReachedListener: OnLastItemReachedListener? = null,
        private val onImageItemClicked: OnImageItemClicked? = null
) : RecyclerView.Adapter<NewsItemRecyclerAdapter.ViewHolder>() {

    interface OnLastItemReachedListener {
        fun onLastItemReached()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    inner class ViewHolder(private val newsItemBinding: NewsItemBinding)
        : RecyclerView.ViewHolder(newsItemBinding.root), ToroPlayer {

        var helper: ExoPlayerViewHelper? = null
        private lateinit var mediaUri: Uri

        fun bind(item: NewsItem) {
            mediaUri = Uri.parse(item.video)
            newsItemBinding.newsTitle.text = String.format("%s...", item.content.substring(0, item.content.length / 4))

            if (item.image.isNotEmpty()) {
                newsItemBinding.imageRecyclerView.layoutManager = ScrollingLinearLayoutManager(
                        newsItemBinding.root.context,
                        LinearLayoutManager.HORIZONTAL, false, 450)
                newsItemBinding.imageRecyclerView.adapter = ImageListRecyclerAdapter(
                        getImagesForContent(item.image), onImageItemClicked
                )
                newsItemBinding.imageRecyclerView.visibility = View.VISIBLE
            } else newsItemBinding.imageRecyclerView.visibility = View.GONE

            if (item.video.isNotEmpty()) {
                newsItemBinding.playerView.visibility = View.VISIBLE
            } else newsItemBinding.playerView.visibility = View.GONE

            newsItemBinding.newsPayload.addAutoLinkMode(AutoLinkMode.MODE_HASHTAG, AutoLinkMode.MODE_MENTION, AutoLinkMode.MODE_URL)
            newsItemBinding.newsPayload.setMentionModeColor(Color.parseColor("#35F984"))
            newsItemBinding.newsPayload.setHashtagModeColor(Color.parseColor("#EA4444"))
            newsItemBinding.newsPayload.setUrlModeColor(Color.parseColor("#CA59EF"))

            newsItemBinding.newsPayload.setAutoLinkOnClickListener { autoLinkMode: AutoLinkMode?, matchedText: String ->
                when (autoLinkMode) {
                    AutoLinkMode.MODE_URL -> openUrlInCustomTab(newsItemBinding.root.context, matchedText)
                    AutoLinkMode.MODE_MENTION -> openUrlInCustomTab(newsItemBinding.root.context, getTelegramDeepLinkForMention(matchedText))
                    else -> {}
                }
            }

            newsItemBinding.newsPayload.setAutoLinkText(item.content)
            newsItemBinding.telegram.setOnClickListener {
                openUrlInCustomTab(newsItemBinding.root.context,
                        getTelegramDeepLinkForMention("@tikvahethiopia&post=" + item.id))
            }

        }

        override fun getPlayerView(): View = newsItemBinding.playerView

        override fun getCurrentPlaybackInfo(): PlaybackInfo = helper?.latestPlaybackInfo ?: PlaybackInfo()

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

        override fun isPlaying(): Boolean = helper?.isPlaying ?: false

        override fun wantsToPlay(): Boolean = ToroUtil.visibleAreaOffset(this, newsItemBinding.root.parent) >= 0.85

        override fun getPlayerOrder(): Int = adapterPosition

    }

}