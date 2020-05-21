package ethiopia.covid.android.ui.fragment

import android.view.View
import ethiopia.covid.android.R
import ethiopia.covid.android.databinding.ContentStateLayoutBinding

/**
 * Created by BrookMG on 4/10/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
object ContentState {
    @JvmStatic
    fun changeProgressBarVisibility(workingFrom: ContentStateLayoutBinding, show: Boolean) {
        workingFrom.mainProgressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    fun changeErrorDialogVisibility(workingFrom: ContentStateLayoutBinding, show: Boolean) {
        workingFrom.errorIndicator.visibility = if (show) View.VISIBLE else View.GONE
        workingFrom.refreshButton.visibility = if (show) View.VISIBLE else View.GONE
        workingFrom.errorText.visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    fun setRefreshButtonAction(workingFrom: ContentStateLayoutBinding, listener: View.OnClickListener?) {
        workingFrom.refreshButton.setOnClickListener(listener)
    }
}