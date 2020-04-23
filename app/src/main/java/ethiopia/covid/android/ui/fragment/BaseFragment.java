package ethiopia.covid.android.ui.fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import ethiopia.covid.android.ui.activity.MainActivity;

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
public class BaseFragment extends Fragment {

    protected boolean canGoBack;

    void changeTheme() {
        if (getActivity() != null) ((MainActivity) getActivity()).changeTheme();
    }

    public void back() {}
    public void next() {}
    public boolean canGoBack() { return canGoBack; }

    public void onReselect() {}

}
