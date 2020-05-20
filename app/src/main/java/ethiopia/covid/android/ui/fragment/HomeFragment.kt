package ethiopia.covid.android.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import ethiopia.covid.android.R
import ethiopia.covid.android.databinding.HomeFragmentBinding
import ethiopia.covid.android.ui.adapter.TabAdapter
import me.ibrahimsn.lib.Badge

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
class HomeFragment : BaseFragment() {
    private lateinit var tabAdapter: TabAdapter
    private lateinit var homeFragmentBinding: HomeFragmentBinding

    fun showBadge(position: Int, badge: Badge?) {
        homeFragmentBinding.bottomBar.setBadge(position, badge!!)
    }

    fun removeBadge(position: Int) {
        homeFragmentBinding.bottomBar.removeBadge(position)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        homeFragmentBinding = HomeFragmentBinding.inflate(layoutInflater)

        tabAdapter = TabAdapter(childFragmentManager)
        tabAdapter.addFragment(StatFragment.newInstance(), getString(R.string.statistics_menu_title))
        tabAdapter.addFragment(DetailFragment.newInstance(), getString(R.string.covid19_menu_title))
        tabAdapter.addFragment(NewsFragment.newInstance(), getString(R.string.news_menu_title))
        tabAdapter.addFragment(ContactFragment.newInstance(), getString(R.string.contact_menu_title))

        homeFragmentBinding.mainViewPager.adapter = tabAdapter
        homeFragmentBinding.mainViewPager.offscreenPageLimit = 3
        homeFragmentBinding.mainViewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                homeFragmentBinding.bottomBar.setActiveItem(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        homeFragmentBinding.bottomBar.onItemSelected = { i: Int -> homeFragmentBinding.mainViewPager.currentItem = i }
        homeFragmentBinding.bottomBar.onItemReselected = { i: Int -> (tabAdapter.getItem(i) as? BaseFragment)?.onReselect() }
        ViewCompat.setElevation(homeFragmentBinding.bottomBar, 16f)
        return homeFragmentBinding.root
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