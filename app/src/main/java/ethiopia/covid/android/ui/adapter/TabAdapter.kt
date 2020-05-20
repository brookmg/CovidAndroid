package ethiopia.covid.android.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*

/**
 * Created by BrookMG on 20/6/2020 in ethiopia.covid.android.ui.adapter
 * inside the project CoVidEt .
 */
class TabAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val mFragments: MutableList<Fragment> = mutableListOf()
    private val mTabTitles: MutableList<String> = mutableListOf()

    fun addFragment(fragment: Fragment, tabTitle: String) {
        mFragments.add(fragment)
        mTabTitles.add(tabTitle)
    }

    fun setFragments(fragments: List<Fragment>) = mFragments.addAll(fragments)

    fun getmFragments(): List<Fragment> = mFragments

    override fun getPageTitle(position: Int): CharSequence? = mTabTitles[position]

    override fun getItem(position: Int): Fragment = mFragments[position]

    override fun getCount(): Int = mFragments.size
}