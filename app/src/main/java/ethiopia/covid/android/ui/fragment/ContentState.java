package ethiopia.covid.android.ui.fragment;

import android.view.View;

import ethiopia.covid.android.R;

/**
 * Created by BrookMG on 4/10/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
public class ContentState {

    static void changeProgressBarVisibility(View workingFrom, boolean show) {
        workingFrom.findViewById(R.id.main_progress_bar).setVisibility( show ? View.VISIBLE : View.GONE);
    }

    static void changeErrorDialogVisibility(View workingFrom, boolean show) {
        workingFrom.findViewById(R.id.error_indicator).setVisibility( show ? View.VISIBLE : View.GONE);
        workingFrom.findViewById(R.id.refresh_button).setVisibility( show ? View.VISIBLE : View.GONE);
        workingFrom.findViewById(R.id.error_text).setVisibility( show ? View.VISIBLE : View.GONE);
    }

    static void setRefreshButtonAction(View workingFrom , View.OnClickListener listener) {
        workingFrom.findViewById(R.id.refresh_button).setOnClickListener(listener);
    }

}
