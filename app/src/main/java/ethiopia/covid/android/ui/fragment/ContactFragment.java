package ethiopia.covid.android.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import ethiopia.covid.android.R;
import ethiopia.covid.android.ui.activity.MainActivity;

import static ethiopia.covid.android.util.Constant.TAG_QUESTIONNAIRE;

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
public class ContactFragment extends BaseFragment {

    private ConstraintLayout constraintLayout;

    public static ContactFragment newInstance() {
        Bundle args = new Bundle();
        ContactFragment fragment = new ContactFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private void handleWindowInsets(View view) {
        view.setOnApplyWindowInsetsListener((v1, insets) -> {
            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            marginParams.setMargins( 0, insets.getSystemWindowInsetTop(), 0, 0);
            view.setLayoutParams(marginParams);

            return insets;
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            constraintLayout.requestApplyInsets();
            handleWindowInsets(constraintLayout);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.contact_fragment, container, false);

        constraintLayout = mainView.findViewById(R.id._holder);
        mainView.findViewById(R.id.questionnaire_button).setOnClickListener(v -> {
            if (getActivity() != null) ((MainActivity) getActivity())
                    .changeFragment(TAG_QUESTIONNAIRE , new Bundle(), null);
        });

        return mainView;
    }

}
