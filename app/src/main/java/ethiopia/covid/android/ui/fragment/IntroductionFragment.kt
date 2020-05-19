package ethiopia.covid.android.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import ethiopia.covid.android.R
import ethiopia.covid.android.ui.fragment.ContentState.changeErrorDialogVisibility
import ethiopia.covid.android.ui.fragment.ContentState.changeProgressBarVisibility
import ethiopia.covid.android.ui.fragment.ContentState.setRefreshButtonAction

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
@Suppress("unused")
class IntroductionFragment : BaseFragment() {
    private lateinit var mainView: View
    private lateinit var mainScrollView: ScrollView

    fun changeProgressState(state: Boolean) {
        changeProgressBarVisibility(mainView, state)
        mainScrollView.visibility = if (state) View.GONE else View.VISIBLE
    }

    fun showError(error: Boolean) {
        changeErrorDialogVisibility(mainView, error)
        mainScrollView.visibility = if (error) View.GONE else View.VISIBLE
    }

    fun setRefreshButtonAction(listener: View.OnClickListener?) {
        setRefreshButtonAction(mainView, listener)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mainView = inflater.inflate(R.layout.introduction_fragment, container, false)
        mainScrollView = mainView.findViewById(R.id.main_scroll_view)
        changeProgressState(true)
        return mainView
    }

    companion object {
        @JvmStatic
        fun newInstance(): IntroductionFragment {
            val args = Bundle()
            val fragment = IntroductionFragment()
            fragment.arguments = args
            return fragment
        }
    }
}