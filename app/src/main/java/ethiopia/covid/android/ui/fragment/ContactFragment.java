package ethiopia.covid.android.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;

import ethiopia.covid.android.R;
import ethiopia.covid.android.data.ContactItem;
import ethiopia.covid.android.ui.activity.MainActivity;
import ethiopia.covid.android.ui.adapter.ContactRecyclerAdapter;

import static ethiopia.covid.android.util.Constant.TAG_QUESTIONNAIRE;

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
public class ContactFragment extends BaseFragment {

    private LinearLayout constraintLayout;
    private RecyclerView regionalPhoneNumbers;

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
        regionalPhoneNumbers = mainView.findViewById(R.id.regional_phone_numbers_recycler_view);

        mainView.findViewById(R.id.questionnaire_button).setOnClickListener(v -> {
            if (getActivity() != null) ((MainActivity) getActivity())
                    .changeFragment(TAG_QUESTIONNAIRE , new Bundle(), null);
        });

        ContactRecyclerAdapter contactRecyclerAdapter = new ContactRecyclerAdapter(Arrays.asList(
                new ContactItem( R.drawable.tigray , "6244"),
                new ContactItem( R.drawable.afar , "6220"),
                new ContactItem( R.drawable.amhara , "6981"),
                new ContactItem( R.drawable.oromia , "6955"),
                new ContactItem( R.drawable.somali , "6599"),
                new ContactItem( R.drawable.benishangul , "6016"),
                new ContactItem( R.drawable.south , "6929"),
                new ContactItem( R.drawable.harrar , "6864"),
                new ContactItem( R.drawable.gambella , "6184"),
                new ContactItem( R.drawable.dire , "6407")
        ));

        regionalPhoneNumbers.setNestedScrollingEnabled(false);
        regionalPhoneNumbers.setLayoutManager(new GridLayoutManager(getActivity() , 2));
        regionalPhoneNumbers.setAdapter(contactRecyclerAdapter);

        return mainView;
    }

}
