package ethiopia.covid.android.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ethiopia.covid.android.R
import ethiopia.covid.android.databinding.BlankFragmentBinding

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
class BlankFragment : BaseFragment() {
    private lateinit var blankFragmentBinding: BlankFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        blankFragmentBinding = BlankFragmentBinding.inflate(layoutInflater)
        return blankFragmentBinding.root
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