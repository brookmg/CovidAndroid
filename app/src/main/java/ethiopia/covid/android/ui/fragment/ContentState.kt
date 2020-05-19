package ethiopia.covid.android.ui.fragment

import android.view.View
import ethiopia.covid.android.R

/**
 * Created by BrookMG on 4/10/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
object ContentState {
    @JvmStatic
    fun changeProgressBarVisibility(workingFrom: View, show: Boolean) {
        workingFrom.findViewById<View>(R.id.main_progress_bar).visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    fun changeErrorDialogVisibility(workingFrom: View, show: Boolean) {
        workingFrom.findViewById<View>(R.id.error_indicator).visibility = if (show) View.VISIBLE else View.GONE
        workingFrom.findViewById<View>(R.id.refresh_button).visibility = if (show) View.VISIBLE else View.GONE
        workingFrom.findViewById<View>(R.id.error_text).visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    fun setRefreshButtonAction(workingFrom: View, listener: View.OnClickListener?) {
        workingFrom.findViewById<View>(R.id.refresh_button).setOnClickListener(listener)
    }
}