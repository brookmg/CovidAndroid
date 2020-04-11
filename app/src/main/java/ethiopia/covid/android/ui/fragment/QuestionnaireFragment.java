package ethiopia.covid.android.ui.fragment;

import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ethiopia.covid.android.R;
import ethiopia.covid.android.ui.adapter.TabAdapter;
import ethiopia.covid.android.ui.widget.YekomeViewPager;
import ethiopia.covid.android.util.Utils;

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
public class QuestionnaireFragment extends BaseFragment {

    private ProgressBar pageChangeProgressBar;
    private YekomeViewPager mainViewPager;
    private FloatingActionButton nextButton;
    private TabAdapter tabAdapter;
    private BottomAppBar bottomAppBar;

    public static QuestionnaireFragment newInstance() {
        Bundle args = new Bundle();
        QuestionnaireFragment fragment = new QuestionnaireFragment();
        fragment.setArguments(args);
        return fragment;
    }

    void back() {
        mainViewPager.setCurrentItem(
                Math.max(mainViewPager.getCurrentItem() - 1 , 0), true
        );
    }

    void next() {
        mainViewPager.setCurrentItem(
                Math.min(mainViewPager.getCurrentItem() + 1 , tabAdapter.getCount()), true
        );
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
            mainViewPager.requestApplyInsets();
            handleWindowInsets(mainViewPager);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.questionnaire_fragment, container, false);
        Utils.setCurrentTheme(getActivity() , 0);

        mainViewPager = mainView.findViewById(R.id.main_view_pager);
        pageChangeProgressBar = mainView.findViewById(R.id.current_page_progress_bar);
        nextButton = mainView.findViewById(R.id.next_fab);
        bottomAppBar = mainView.findViewById(R.id.bottom_bar);

        tabAdapter = new TabAdapter(getChildFragmentManager());
        tabAdapter.addFragment(IntroductionFragment.newInstance(), getString(R.string.news_menu_title));
        tabAdapter.addFragment(BlankFragment.newInstance(), getString(R.string.news_menu_title));
        tabAdapter.addFragment(BlankFragment.newInstance(), getString(R.string.news_menu_title));
        tabAdapter.addFragment(BlankFragment.newInstance(), getString(R.string.news_menu_title));

        mainViewPager.setAdapter(tabAdapter);
        mainViewPager.setPagingEnabled(false);
        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(pageChangeProgressBar.getProgress(), ((float)(position + 1) / (float)tabAdapter.getCount()) * 100f);
                valueAnimator.addUpdateListener(animation -> pageChangeProgressBar.setProgress(
                        Math.round((float) animation.getAnimatedValue())
                ));
                valueAnimator.start();
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        nextButton.setOnClickListener(v -> next());
        bottomAppBar.setNavigationOnClickListener(v -> back());

        return mainView;
    }

}
