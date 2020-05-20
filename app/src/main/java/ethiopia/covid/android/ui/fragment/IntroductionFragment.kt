package ethiopia.covid.android.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ethiopia.covid.android.databinding.IntroductionFragmentBinding
import ethiopia.covid.android.ui.fragment.ContentState.changeErrorDialogVisibility
import ethiopia.covid.android.ui.fragment.ContentState.changeProgressBarVisibility
import ethiopia.covid.android.ui.fragment.ContentState.setRefreshButtonAction

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
@Suppress("unused")
class IntroductionFragment : BaseFragment() {
    private lateinit var introductionFragmentBinding: IntroductionFragmentBinding

    fun changeProgressState(state: Boolean) {
        changeProgressBarVisibility(introductionFragmentBinding.contentState, state)
        introductionFragmentBinding.mainScrollView.visibility = if (state) View.GONE else View.VISIBLE
    }

    fun showError(error: Boolean) {
        changeErrorDialogVisibility(introductionFragmentBinding.contentState, error)
        introductionFragmentBinding.mainScrollView.visibility = if (error) View.GONE else View.VISIBLE
    }

    fun setRefreshButtonAction(listener: View.OnClickListener?) {
        setRefreshButtonAction(introductionFragmentBinding.contentState, listener)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        introductionFragmentBinding = IntroductionFragmentBinding.inflate(layoutInflater)
        changeProgressState(true)
        return introductionFragmentBinding.root
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