package ethiopia.covid.android.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ethiopia.covid.android.App.Companion.instance
import ethiopia.covid.android.R
import ethiopia.covid.android.data.DetailItem
import ethiopia.covid.android.data.FAQ
import ethiopia.covid.android.network.API.OnItemReady
import ethiopia.covid.android.ui.adapter.DetailRecyclerAdapter
import ethiopia.covid.android.ui.fragment.ContentState.changeErrorDialogVisibility
import ethiopia.covid.android.ui.fragment.ContentState.changeProgressBarVisibility
import ethiopia.covid.android.ui.fragment.ContentState.setRefreshButtonAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
class DetailFragment : BaseFragment() {
    private var recyclerView: RecyclerView? = null
    private var adapter: DetailRecyclerAdapter? = null

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private fun handleWindowInsets(recyclerView: RecyclerView?) {
        recyclerView?.setOnApplyWindowInsetsListener { _: View?, insets: WindowInsets ->
            val marginParams = recyclerView.layoutParams as MarginLayoutParams
            marginParams.setMargins(0, insets.systemWindowInsetTop, 0, 0)
            recyclerView.layoutParams = marginParams
            insets
        }
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            recyclerView?.requestApplyInsets()
            handleWindowInsets(recyclerView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView = null
        adapter = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val mainView = inflater.inflate(R.layout.detail_fragment, container, false)
        recyclerView = mainView.findViewById(R.id.recycler_view)
        recyclerView?.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        setRefreshButtonAction(mainView, View.OnClickListener { v: View? -> renderPage(mainView) })
        renderPage(mainView)
        return mainView
    }

    private fun renderPage(mainView: View) {
        adapter = DetailRecyclerAdapter(ArrayList())
        val details: MutableList<DetailItem> = ArrayList()

        try {
            adapter?.setHasStableIds(true)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        changeProgressBarVisibility(mainView, true)
        changeErrorDialogVisibility(mainView, false)

        instance.mainAPI.getFrequentlyAskedQuestions(object : OnItemReady<FAQ?> {
            override fun onItem(item: FAQ?, err: String?) {
                CoroutineScope(Dispatchers.Main).launch {
                    if (item?.data?.isNotEmpty() == true) {
                        withContext(Dispatchers.Default) {
                            for (content in item.data) details.add(DetailItem(content))
                        }
                    } else {
                        changeProgressBarVisibility(mainView, false)
                        changeErrorDialogVisibility(mainView, true)
                    }

                    adapter?.addContent(details)
                    changeProgressBarVisibility(mainView, false)
                }
            }
        })

        recyclerView?.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance(): DetailFragment {
            val args = Bundle()
            val fragment = DetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}