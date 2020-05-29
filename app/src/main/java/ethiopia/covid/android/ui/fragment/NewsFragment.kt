package ethiopia.covid.android.ui.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowInsets
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stfalcon.imageviewer.StfalconImageViewer
import com.stfalcon.imageviewer.loader.ImageLoader
import ethiopia.covid.android.App.Companion.instance
import ethiopia.covid.android.R
import ethiopia.covid.android.data.NewsItem
import ethiopia.covid.android.databinding.ContentStateLayoutBinding
import ethiopia.covid.android.databinding.NewsFragmentBinding
import ethiopia.covid.android.network.API.OnItemReady
import ethiopia.covid.android.ui.activity.MainActivity
import ethiopia.covid.android.ui.adapter.NewsItemRecyclerAdapter
import ethiopia.covid.android.ui.adapter.NewsItemRecyclerAdapter.OnLastItemReachedListener
import ethiopia.covid.android.ui.adapter.OnImageItemClicked
import ethiopia.covid.android.ui.fragment.ContentState.changeErrorDialogVisibility
import ethiopia.covid.android.ui.fragment.ContentState.changeProgressBarVisibility
import ethiopia.covid.android.ui.fragment.ContentState.setRefreshButtonAction
import ethiopia.covid.android.util.Constant.PREFERENCE_LATEST_NEWS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.ibrahimsn.lib.Badge
import me.ibrahimsn.lib.BadgeType
import timber.log.Timber
import java.util.*

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
class NewsFragment : BaseFragment() {
    private var _newsFragmentBinding: NewsFragmentBinding? = null
    private val newsFragmentBinding: NewsFragmentBinding
        get() = _newsFragmentBinding!!

    private var adapter: NewsItemRecyclerAdapter? = null

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private fun handleWindowInsets(recyclerView: RecyclerView?) {
        recyclerView?.setOnApplyWindowInsetsListener { _: View?, insets: WindowInsets ->
            val marginParams = recyclerView.layoutParams as MarginLayoutParams
            marginParams.setMargins(0, insets.systemWindowInsetTop, 0, 0)
            recyclerView.layoutParams = marginParams
            insets
        }
    }

    private fun onLoadMoreNews() {
        val currentLastNewsId = adapter?.idOfLastNews()

        if (currentLastNewsId != null)
        instance.mainAPI.getLatestNews(currentLastNewsId, object : OnItemReady<List<NewsItem>?> {
            override fun onItem(item: List<NewsItem>?, err: String?) {
                if (err?.isNotEmpty() == true) Timber.e(err)
                adapter?.loadMoreOnBottom(item)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            newsFragmentBinding.recyclerView.requestApplyInsets()
            handleWindowInsets(newsFragmentBinding.recyclerView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _newsFragmentBinding = null
        adapter = null
    }

    override fun onReselect() {
        super.onReselect()
        // Go to the top of the recycler view
        newsFragmentBinding.recyclerView.smoothScrollToPosition(0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _newsFragmentBinding = NewsFragmentBinding.inflate(layoutInflater)

        newsFragmentBinding.recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        setRefreshButtonAction(newsFragmentBinding.contentState, View.OnClickListener { renderNews(newsFragmentBinding.contentState) })
        renderNews(newsFragmentBinding.contentState)
        return newsFragmentBinding.root
    }



    private fun renderNews(mainView: ContentStateLayoutBinding) {
        adapter = NewsItemRecyclerAdapter(ArrayList(), object : OnLastItemReachedListener {
            override fun onLastItemReached() {
                onLoadMoreNews()
            }
        }, object : OnImageItemClicked {
            override fun onImageClicked(imageView: ImageView, clickedImageUri: List<String>) {
                if (activity != null)
                    StfalconImageViewer.Builder(activity, clickedImageUri,
                        ImageLoader { imageViewX: ImageView?, image: String? ->
                            run {
                                if (imageViewX != null)
                                    Glide.with(imageViewX).load(image).into(imageViewX)
                            }
                        })
                        .withTransitionFrom(imageView)
                        .show(true)
            }
        })

        changeProgressBarVisibility(mainView, true)
        changeErrorDialogVisibility(mainView, false)

        instance.mainAPI.getLatestNews(object : OnItemReady<List<NewsItem>?> {
            override fun onItem(item: List<NewsItem>?, err: String?) {
                if (item == null || item.isEmpty()) {
                    changeProgressBarVisibility(mainView, false)
                    changeErrorDialogVisibility(mainView, true)
                    return
                }

                CoroutineScope(Dispatchers.Default).launch {
                    val previousLatestNews = getDefaultSharedPreferences(instance)
                            .getInt(PREFERENCE_LATEST_NEWS, 0)

                    // If the news is not found in the current incoming list ...
                    // That means the user missed more than 10 news
                    var currentNewsPosition = 11
                    for (i in item.indices) {
                        if (item[i].id == previousLatestNews) {
                            currentNewsPosition = i
                            break
                        }
                    }

                    getDefaultSharedPreferences(instance)
                            .edit().putInt(PREFERENCE_LATEST_NEWS, item[0].id)
                            .apply()

                    withContext(Dispatchers.Main) {
                        showBadge(currentNewsPosition)
                        if (adapter != null) adapter?.populateNewsItems(item)
                        changeProgressBarVisibility(mainView, false)
                    }
                }
            }
        })

        newsFragmentBinding.recyclerView.adapter = adapter
    }

    private fun showBadge(numberOfNewNews: Int) {
        if (numberOfNewNews <= 0) return

        (activity as? MainActivity)?.setBadge(2, Badge(
                20f,
                numberOfNewNews.coerceAtMost(10).toString() + if (numberOfNewNews > 10) "+" else "",
                ContextCompat.getColor(activity as MainActivity, R.color.yellow_0),
                Color.BLACK,
                8f, BadgeType.BOX))

        Handler().postDelayed({ (activity as? MainActivity)?.removeBadge(2) }, 10000)
    }

    companion object {
        fun newInstance(): NewsFragment {
            val args = Bundle()
            val fragment = NewsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}