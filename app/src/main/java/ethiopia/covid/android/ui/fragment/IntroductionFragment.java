package ethiopia.covid.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ethiopia.covid.android.R;

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
public class IntroductionFragment extends BaseFragment {

    private View mainView;
    private ScrollView mainScrollView;

    public static IntroductionFragment newInstance() {
        Bundle args = new Bundle();
        IntroductionFragment fragment = new IntroductionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void changeProgressState(boolean state) {
        ContentState.changeProgressBarVisibility(mainView , state);
        mainScrollView.setVisibility( state ? View.GONE : View.VISIBLE);
    }

    public void showError(boolean error) {
        ContentState.changeErrorDialogVisibility(mainView , error);
        mainScrollView.setVisibility( error ? View.GONE : View.VISIBLE);
    }

    public void setRefreshButtonAction(View.OnClickListener listener) {
        ContentState.setRefreshButtonAction(mainView , listener);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.introduction_fragment, container, false);
        mainScrollView = mainView.findViewById(R.id.main_scroll_view);
        changeProgressState(true);
        return mainView;
    }

}
