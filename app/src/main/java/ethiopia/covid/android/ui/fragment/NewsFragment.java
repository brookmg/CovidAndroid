package ethiopia.covid.android.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.stfalcon.imageviewer.StfalconImageViewer;

import java.util.ArrayList;
import java.util.Collections;

import ethiopia.covid.android.App;
import ethiopia.covid.android.R;
import ethiopia.covid.android.ui.adapter.NewsItemRecyclerAdapter;
import ethiopia.covid.android.util.Utils;
import timber.log.Timber;

import static ethiopia.covid.android.ui.fragment.ContentState.changeErrorDialogVisibility;
import static ethiopia.covid.android.ui.fragment.ContentState.changeProgressBarVisibility;
import static ethiopia.covid.android.ui.fragment.ContentState.setRefreshButtonAction;

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
public class NewsFragment extends BaseFragment {

    private RecyclerView mainNewsRecycler;
    private NewsItemRecyclerAdapter adapter;

    public static NewsFragment newInstance() {
        Bundle args = new Bundle();
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    private void handleWindowInsets(RecyclerView recyclerView) {
        recyclerView.setOnApplyWindowInsetsListener((v1, insets) -> {
            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams();
            marginParams.setMargins( 0, insets.getSystemWindowInsetTop(), 0, 0);
            recyclerView.setLayoutParams(marginParams);

            return insets;
        });
    }

    private void onLoadMoreNews() {
        int currentLastNewsId = adapter.idOfLastNews();
        App.getInstance().getMainAPI().getLatestNews(currentLastNewsId , (item, err) -> {
            if (!err.isEmpty()) Timber.e(err);
            adapter.loadMoreOnBottom(item);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            mainNewsRecycler.requestApplyInsets();
            handleWindowInsets(mainNewsRecycler);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mainNewsRecycler = null;
        adapter = null;
    }

    @Override
    public void onReselect() {
        super.onReselect();
        // Go to the top of the recycler view
        mainNewsRecycler.smoothScrollToPosition(0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.news_fragment, container, false);
        mainNewsRecycler = mainView.findViewById(R.id.recycler_view);

        mainNewsRecycler.setLayoutManager(new LinearLayoutManager(getActivity() , RecyclerView.VERTICAL , false));

        setRefreshButtonAction(mainView , v -> renderNews(mainView));
        renderNews(mainView);

        return mainView;
    }

    private void renderNews(View mainView) {
        adapter = new NewsItemRecyclerAdapter(new ArrayList<>(), this::onLoadMoreNews , (mainImageView, clickedImageUri) -> {
            if (getActivity() != null)
            new StfalconImageViewer.Builder<>(getActivity(), clickedImageUri,
                    (imageView, image) -> Glide.with(imageView).load(image).into(imageView))
                    .withTransitionFrom(mainImageView)
                    .show(true);
        });

        changeProgressBarVisibility(mainView , true);
        changeErrorDialogVisibility(mainView, false);

        App.getInstance().getMainAPI().getLatestNews((item, err) -> {
            if (item == null) {
                changeProgressBarVisibility(mainView , false);
                changeErrorDialogVisibility(mainView, true);
            }

            if (adapter != null) adapter.populateNewsItems(item);
            changeProgressBarVisibility(mainView , false);
        });

        mainNewsRecycler.setAdapter(adapter);
    }


}
