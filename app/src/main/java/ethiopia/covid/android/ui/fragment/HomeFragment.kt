package ethiopia.covid.android.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import ethiopia.covid.android.R
import ethiopia.covid.android.ui.adapter.TabAdapter
import me.ibrahimsn.lib.Badge
import me.ibrahimsn.lib.SmoothBottomBar

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
class HomeFragment : BaseFragment() {
    private lateinit var tabAdapter: TabAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var smoothBottomBar: SmoothBottomBar

    fun showBadge(position: Int, badge: Badge?) {
        smoothBottomBar.setBadge(position, badge!!)
    }

    fun removeBadge(position: Int) {
        smoothBottomBar.removeBadge(position)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val mainView = inflater.inflate(R.layout.home_fragment, container, false)
        viewPager = mainView.findViewById(R.id.main_view_pager)
        smoothBottomBar = mainView.findViewById(R.id.bottomBar)
        tabAdapter = TabAdapter(childFragmentManager)
        tabAdapter.addFragment(StatFragment.newInstance(), getString(R.string.statistics_menu_title))
        tabAdapter.addFragment(DetailFragment.newInstance(), getString(R.string.covid19_menu_title))
        tabAdapter.addFragment(NewsFragment.newInstance(), getString(R.string.news_menu_title))
        tabAdapter.addFragment(ContactFragment.newInstance(), getString(R.string.contact_menu_title))
        viewPager.adapter = tabAdapter
        viewPager.offscreenPageLimit = 3
        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                smoothBottomBar.setActiveItem(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        smoothBottomBar.onItemSelected = { i: Int -> viewPager.currentItem = i }
        smoothBottomBar.onItemReselected = { i: Int -> (tabAdapter.getItem(i) as? BaseFragment)?.onReselect() }
        ViewCompat.setElevation(smoothBottomBar, 16f)
        return mainView
    }

    companion object {
        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}