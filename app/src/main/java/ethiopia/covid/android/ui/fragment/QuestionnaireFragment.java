package ethiopia.covid.android.ui.fragment;

import android.animation.ValueAnimator;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ethiopia.covid.android.R;
import ethiopia.covid.android.data.QuestionnaireItem;
import ethiopia.covid.android.data.QuestionItem;
import ethiopia.covid.android.ui.activity.MainActivity;
import ethiopia.covid.android.ui.adapter.OnQuestionItemSelected;
import ethiopia.covid.android.ui.adapter.TabAdapter;
import ethiopia.covid.android.ui.widget.YekomeViewPager;
import ethiopia.covid.android.util.Utils;
import mumayank.com.airlocationlibrary.AirLocation;

import static ethiopia.covid.android.network.API.conjure;

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
public class QuestionnaireFragment extends BaseFragment {

    private ProgressBar pageChangeProgressBar;
    private YekomeViewPager mainViewPager;
    private FloatingActionButton nextButton;
    private AppCompatImageView exitAllButton;
    private TabAdapter tabAdapter;
    private List<QuestionnaireItem> questionnaireItems;
    private BottomAppBar bottomAppBar;

    private IntroductionFragment introductionFragment = IntroductionFragment.newInstance();
    private ResultFragment resultFragment = ResultFragment.newInstance(new HashMap<>());
    private Location currentLocation;

    private SparseArray<ArrayList<QuestionItem>> questionState = new SparseArray<>();
    private AirLocation.Callbacks locationCallbacks = new AirLocation.Callbacks() {
        @Override
        public void onSuccess(@NotNull Location location) {
            currentLocation = location;
        }

        @Override
        public void onFailed(@NotNull AirLocation.LocationFailedEnum locationFailedEnum) {
            Log.e("LOCATION" , locationFailedEnum.name());
        }
    };

    private QuestionnaireFragment(List<QuestionnaireItem> questionnaireItems) {
        questionnaireItems.removeAll(Collections.singleton(null));
        for (QuestionnaireItem item : questionnaireItems) item.getQuestionItems().removeAll(Collections.singleton(null));

        this.questionnaireItems = questionnaireItems;
    }

    public static QuestionnaireFragment newInstance(List<QuestionnaireItem> items) {
        Bundle args = new Bundle();
        QuestionnaireFragment fragment = new QuestionnaireFragment(items);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void back() {
        mainViewPager.setCurrentItem(
                Math.max(mainViewPager.getCurrentItem() - 1 , 0), true
        );

        nextButton.show();
        bottomAppBar.performShow();
        canGoBack = mainViewPager.getCurrentItem() != 0;
    }

    @Override
    public void next() {
        if (mainViewPager.getCurrentItem()+1 == tabAdapter.getCount() -1) {
            Map<QuestionnaireItem, List<QuestionItem>> questionnaireItemListMap = new HashMap<>();
            for (int i = 0; i < questionnaireItems.size(); i++) {
                questionnaireItemListMap.put(
                        questionnaireItems.get(i),
                        questionState.get(i)
                );
            }

            resultFragment.setMap(questionnaireItemListMap);
            resultFragment.setCurrentLocation(currentLocation);

            mainViewPager.setCurrentItem(
                    Math.min(mainViewPager.getCurrentItem() + 1 , tabAdapter.getCount()), true
            );

            nextButton.hide();
            bottomAppBar.performHide();
        }

        mainViewPager.setCurrentItem(
                Math.min(mainViewPager.getCurrentItem() + 1 , tabAdapter.getCount()), true
        );

        canGoBack = mainViewPager.getCurrentItem() != 0;
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
            handleWindowInsets(exitAllButton);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.questionnaire_fragment, container, false);

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).requestLocationIndividually(locationCallbacks);
        }

        mainViewPager = mainView.findViewById(R.id.main_view_pager);
        pageChangeProgressBar = mainView.findViewById(R.id.current_page_progress_bar);
        nextButton = mainView.findViewById(R.id.next_fab);
        bottomAppBar = mainView.findViewById(R.id.bottom_bar);
        exitAllButton = mainView.findViewById(R.id.exit_out);

        tabAdapter = new TabAdapter(getChildFragmentManager());
        tabAdapter.addFragment(introductionFragment , getString(R.string.news_menu_title));

        conjure(() -> {
            for (QuestionnaireItem questionnaire : questionnaireItems) {
                OnQuestionItemSelected questionItemSelected = (item, position) -> {
                    int questionPositionInQuestionnaire = questionnaireItems.indexOf(questionnaire);
                    if (questionnaire.getQuestionType() == QuestionnaireItem.QuestionType.SINGLE_BLOCK_QUESTION) {
                        questionState.put(questionPositionInQuestionnaire, new ArrayList<>(Collections.singletonList(item)));
                        next();
                    } else if (questionnaire.getQuestionType() == QuestionnaireItem.QuestionType.SINGLE_CHOICE_QUESTION) {
                        if (    // If the user unselected an item ... ( clicked it twice in a row ) ... then put empty list
                                questionState.get(questionPositionInQuestionnaire) != null &&
                                questionState.get(questionPositionInQuestionnaire).contains(item)
                        ) questionState.put(questionPositionInQuestionnaire , new ArrayList<>());
                        else questionState.put(questionPositionInQuestionnaire, new ArrayList<>(Collections.singletonList(item)));
                    } else if (questionnaire.getQuestionType() == QuestionnaireItem.QuestionType.SINGLE_MULTIPLE_CHOICE_QUESTION) {
                        ArrayList<QuestionItem> items = questionState.get(questionPositionInQuestionnaire);
                        if (items != null) {
                            if (items.contains(item)) items.remove(item);
                            else items.add(item);
                            questionState.put(questionPositionInQuestionnaire , items);
                        } else questionState.put(questionPositionInQuestionnaire, new ArrayList<>(Collections.singletonList(item)));
                    }
                };

                tabAdapter.addFragment(QuestionPageFragment.newInstance(
                        questionnaire.getQuestionText(),
                        questionnaire.getQuestionType(),
                        questionnaire.getQuestionItems(),
                        questionItemSelected
                ), "Question");
                tabAdapter.notifyDataSetChanged();
            }
            return true;
        }, (content, err) -> {
            tabAdapter.addFragment(resultFragment , "Result");
            tabAdapter.notifyDataSetChanged();
            introductionFragment.changeProgressState(false);

            mainViewPager.setOffscreenPageLimit(tabAdapter.getCount());
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(pageChangeProgressBar.getProgress(), (1f / (float) tabAdapter.getCount()) * 100f);
            valueAnimator.addUpdateListener(animation -> pageChangeProgressBar.setProgress(Math.round((float) animation.getAnimatedValue())));
            valueAnimator.start();

            nextButton.setOnClickListener(v -> next());
            bottomAppBar.setNavigationOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) ((MainActivity) getActivity()).callBackOnParentFragment();
            });

            exitAllButton.setOnClickListener(v -> {
                if (getActivity() != null)
                new MaterialAlertDialogBuilder(
                        getActivity(),
                        Utils.getCurrentTheme(getActivity()) == 0 ? R.style.LightAlertDialog : R.style.DarkAlertDialog
                ).setTitle("Exit questionnaire?").setMessage("If you exit now, all the progress will be lost. Are you sure?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            if (getActivity() instanceof MainActivity) {
                                ((MainActivity) getActivity()).forcefulOnBackPressed();
                            }
                        }).setNegativeButton("No" , null).show();
            });
        });

        mainViewPager.setAdapter(tabAdapter);
        mainViewPager.setPagingEnabled(false);
        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(pageChangeProgressBar.getProgress(), ((float)(position + 1) / (float)tabAdapter.getCount()) * 100f);
                valueAnimator.addUpdateListener(animation -> pageChangeProgressBar.setProgress(Math.round((float) animation.getAnimatedValue())));
                valueAnimator.start();
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        return mainView;
    }

}
