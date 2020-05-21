package ethiopia.covid.android.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.google.android.material.appbar.AppBarLayout
import ethiopia.covid.android.App.Companion.instance
import ethiopia.covid.android.R
import ethiopia.covid.android.data.StatRecyclerItem
import ethiopia.covid.android.databinding.ContentStateLayoutBinding
import ethiopia.covid.android.databinding.StatFragmentBinding
import ethiopia.covid.android.network.API.OnItemReady
import ethiopia.covid.android.ui.activity.MainActivity
import ethiopia.covid.android.ui.adapter.StatRecyclerAdapter
import ethiopia.covid.android.ui.fragment.ContentState.changeErrorDialogVisibility
import ethiopia.covid.android.ui.fragment.ContentState.changeProgressBarVisibility
import ethiopia.covid.android.ui.fragment.ContentState.setRefreshButtonAction
import ethiopia.covid.android.util.Constant.TAG_HEAT_MAP
import ethiopia.covid.android.util.Utils.getCurrentTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference
import kotlin.math.roundToInt

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
class StatFragment : BaseFragment() {

    private lateinit var statFragmentBinding: StatFragmentBinding

    private var statRecyclerAdapter: StatRecyclerAdapter? = null
    private var recyclerViewY = 0

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private fun handleWindowInsets(appBarLayout: AppBarLayout?) {
        appBarLayout?.setOnApplyWindowInsetsListener { _: View?, insets: WindowInsets ->
            val marginParams = appBarLayout.layoutParams as MarginLayoutParams
            marginParams.setMargins(0, insets.systemWindowInsetTop, 0, 0)
            appBarLayout.layoutParams = marginParams
            insets
        }
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            statFragmentBinding.appbarLayout.requestApplyInsets()
            handleWindowInsets(statFragmentBinding.appbarLayout)
        }
    }

    private fun computeRecyclerViewScrollForAppbarElevation(yDiff: Int) {
        recyclerViewY += yDiff //not reliable, but it's one way to find scroll position to compute the elevation for the elevation
        ViewCompat.setElevation(statFragmentBinding.appbarLayout, (recyclerViewY * 0.8f).coerceAtMost(19f).roundToInt().toFloat())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        statFragmentBinding = StatFragmentBinding.inflate(layoutInflater)
        statFragmentBinding.themeBtn.setImageDrawable(
                ContextCompat.getDrawable(
                    requireActivity(),
                    if (getCurrentTheme(activity) == 0) R.drawable.ic_night_theme else R.drawable.ic_light_theme
                )
        )

        statFragmentBinding.themeBtn.setOnClickListener { changeTheme() }
        statFragmentBinding.langBtn.setOnClickListener { (activity as? MainActivity?)?.showLanguageDialog() }
        statFragmentBinding.statRecyclerView.layoutManager = object : LinearLayoutManager(activity, RecyclerView.VERTICAL, false) {
            override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
                try {
                    super.onLayoutChildren(recycler, state)
                } catch (exception: IndexOutOfBoundsException) {
                    exception.printStackTrace()
                }
            }
        }

        statFragmentBinding.statRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (activity is MainActivity) {
                    computeRecyclerViewScrollForAppbarElevation(dy)
                }
            }
        })

        setRefreshButtonAction(statFragmentBinding.contentState, View.OnClickListener { renderPage(statFragmentBinding.contentState) })
        renderPage(statFragmentBinding.contentState)
        return statFragmentBinding.root
    }

    private fun renderPage(mainView: ContentStateLayoutBinding) {
        changeProgressBarVisibility(mainView, true)
        changeErrorDialogVisibility(mainView, false)
        statFragmentBinding.statRecyclerView.visibility = View.GONE

        instance.mainAPI.getStatRecyclerContents(
                WeakReference(requireActivity()),
                object : OnItemReady<MutableList<StatRecyclerItem>> {
                    override fun onItem(item: MutableList<StatRecyclerItem>, err: String?) {
                        if (err?.isNotEmpty() == true || item.isEmpty()) {
                            changeProgressBarVisibility(mainView, false)
                            changeErrorDialogVisibility(mainView, true)
                            return
                        }

                        CoroutineScope(Dispatchers.Default).launch {
                            item.add(
                                2,
                                StatRecyclerItem(
                                    "Preview heat map for the world countries.",
                                    "Preview",
                                    View.OnClickListener {
                                        (activity as? MainActivity)?.changeFragment(TAG_HEAT_MAP, Bundle(), null)
                                    }
                                )
                            )

                            statRecyclerAdapter = StatRecyclerAdapter(item)
                            withContext(Dispatchers.Main) {
                                statFragmentBinding.statRecyclerView.adapter = statRecyclerAdapter
                                statFragmentBinding.statRecyclerView.visibility = View.VISIBLE
                                changeProgressBarVisibility(mainView, false)
                            }
                        }

                    }
                })
    }

    companion object {
        fun newInstance(): StatFragment {
            val args = Bundle()
            val fragment = StatFragment()
            fragment.arguments = args
            return fragment
        }
    }
}