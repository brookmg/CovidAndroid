package ethiopia.covid.android.ui.fragment

import androidx.fragment.app.Fragment
import ethiopia.covid.android.ui.activity.MainActivity

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
open class BaseFragment : Fragment() {
    protected var canGoBack = false
    fun changeTheme() = (activity as? MainActivity?)?.changeTheme()

    open fun back() {}
    open operator fun next() {}
    fun canGoBack(): Boolean = canGoBack

    open fun onReselect() {}
}