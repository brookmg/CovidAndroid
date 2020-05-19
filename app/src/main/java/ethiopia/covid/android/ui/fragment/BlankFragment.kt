package ethiopia.covid.android.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ethiopia.covid.android.R

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
class BlankFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.blank_fragment, container, false)
    }

    companion object {
        fun newInstance(): BlankFragment {
            val args = Bundle()
            val fragment = BlankFragment()
            fragment.arguments = args
            return fragment
        }
    }
}