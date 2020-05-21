package ethiopia.covid.android.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import ethiopia.covid.android.R
import ethiopia.covid.android.data.ContactItem
import ethiopia.covid.android.databinding.ContactFragmentBinding
import ethiopia.covid.android.ui.activity.MainActivity
import ethiopia.covid.android.ui.adapter.ContactRecyclerAdapter
import ethiopia.covid.android.util.Constant.TAG_QUESTIONNAIRE

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
class ContactFragment : BaseFragment() {
    private lateinit var contactFragmentBinding: ContactFragmentBinding

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private fun handleWindowInsets(view: View?) {
        view?.setOnApplyWindowInsetsListener { _: View?, insets: WindowInsets ->
            val marginParams = view.layoutParams as MarginLayoutParams
            marginParams.setMargins(0, insets.systemWindowInsetTop, 0, 0)
            view.layoutParams = marginParams
            insets
        }
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            contactFragmentBinding.Holder.requestApplyInsets()
            handleWindowInsets(contactFragmentBinding.Holder)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        contactFragmentBinding = ContactFragmentBinding.inflate(layoutInflater)
//        constraintLayout = mainView.findViewById(R.id._holder)
//        regionalPhoneNumbers = mainView.findViewById(R.id.regional_phone_numbers_recycler_view)
//
        contactFragmentBinding.questionnaireButton.setOnClickListener {
            (activity as? MainActivity?)
                    ?.changeFragment(TAG_QUESTIONNAIRE, Bundle(), null)
        }

        val contactRecyclerAdapter = ContactRecyclerAdapter(mutableListOf(
                ContactItem(R.drawable.tigray, "6244"),
                ContactItem(R.drawable.afar, "6220"),
                ContactItem(R.drawable.amhara, "6981"),
                ContactItem(R.drawable.oromia, "6955"),
                ContactItem(R.drawable.somali, "6599"),
                ContactItem(R.drawable.benishangul, "6016"),
                ContactItem(R.drawable.south, "6929"),
                ContactItem(R.drawable.harrar, "6864"),
                ContactItem(R.drawable.gambella, "6184"),
                ContactItem(R.drawable.dire, "6407")
        ))

        contactFragmentBinding.regionalPhoneNumbersRecyclerView.let {
            it.isNestedScrollingEnabled = false
            it.layoutManager = GridLayoutManager(activity, 2)
            it.adapter = contactRecyclerAdapter
        }

        return contactFragmentBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(): ContactFragment {
            val args = Bundle()
            val fragment = ContactFragment()
            fragment.arguments = args
            return fragment
        }
    }
}