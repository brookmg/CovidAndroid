package ethiopia.covid.android.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ethiopia.covid.android.App;
import ethiopia.covid.android.R;
import ethiopia.covid.android.data.Case;
import ethiopia.covid.android.data.CovidStatItem;
import ethiopia.covid.android.data.PatientItem;
import ethiopia.covid.android.data.StatRecyclerItem;
import ethiopia.covid.android.data.WorldCovid;
import ethiopia.covid.android.network.API;
import ethiopia.covid.android.ui.adapter.StatRecyclerAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ethiopia.covid.android.ui.fragment.ContentState.changeErrorDialogVisibility;
import static ethiopia.covid.android.ui.fragment.ContentState.changeProgressBarVisibility;
import static ethiopia.covid.android.ui.fragment.ContentState.setRefreshButtonAction;
import static ethiopia.covid.android.util.Utils.getCurrentTheme;

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
public class StatFragment extends BaseFragment {

    private AppBarLayout appBarLayout;
    private RecyclerView recyclerView;
    private AppCompatImageButton themeButton;
    private StatRecyclerAdapter statRecyclerAdapter;

    public static StatFragment newInstance() {
        Bundle args = new Bundle();
        StatFragment fragment = new StatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private void handleWindowInsets(AppBarLayout appBarLayout) {
        appBarLayout.setOnApplyWindowInsetsListener((v1, insets) -> {
            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) appBarLayout.getLayoutParams();
            marginParams.setMargins( 0, insets.getSystemWindowInsetTop(), 0, 0);
            appBarLayout.setLayoutParams(marginParams);

            return insets;
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            appBarLayout.requestApplyInsets();
            handleWindowInsets(appBarLayout);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.stat_fragment, container, false);
        appBarLayout = mainView.findViewById(R.id.appbar_layout);
        recyclerView = mainView.findViewById(R.id.stat_recycler_view);
        themeButton = mainView.findViewById(R.id.theme_btn);

        themeButton.setImageDrawable(
                ContextCompat.getDrawable(
                        getActivity(),
                        getCurrentTheme(getActivity()) == 0 ? R.drawable.ic_night_theme : R.drawable.ic_light_theme
                )
        );

        themeButton.setOnClickListener(v -> changeTheme());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity() , RecyclerView.VERTICAL , false) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                try {
                    super.onLayoutChildren(recycler, state);
                } catch (IndexOutOfBoundsException exception) {
                    exception.printStackTrace();
                }
            }
        });

        setRefreshButtonAction(mainView, v -> renderPage(mainView));
        renderPage(mainView);

        return mainView;
    }

    private void renderPage(View mainView) {
        changeProgressBarVisibility(mainView , true);
        changeErrorDialogVisibility(mainView, false);
        recyclerView.setVisibility(View.GONE);

        App.getInstance().getMainAPI().getStatRecyclerContents((recyclerItems, err) -> {
            if (!err.isEmpty() || recyclerItems == null || recyclerItems.isEmpty() ) {
                changeProgressBarVisibility(mainView, false);
                changeErrorDialogVisibility(mainView, true);
            }

            statRecyclerAdapter = new StatRecyclerAdapter(recyclerItems);
            recyclerView.setAdapter(statRecyclerAdapter);
            recyclerView.setVisibility(View.VISIBLE);
            changeProgressBarVisibility(mainView , false);
        });
    }

}
