package ethiopia.covid.android.ui.fragment;

import android.animation.ValueAnimator;
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
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
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

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
public class QuestionnaireFragment extends BaseFragment {

    private ProgressBar pageChangeProgressBar;
    private YekomeViewPager mainViewPager;
    private FloatingActionButton nextButton;
    private TabAdapter tabAdapter;
    private List<QuestionnaireItem> questionnaireItems;
    private BottomAppBar bottomAppBar;

    private ResultFragment resultFragment = ResultFragment.newInstance(new HashMap<>());

    private SparseArray<ArrayList<QuestionItem>> questionState = new SparseArray<>();

    private QuestionnaireFragment(List<QuestionnaireItem> questionnaireItems) {
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
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.questionnaire_fragment, container, false);

        mainViewPager = mainView.findViewById(R.id.main_view_pager);
        pageChangeProgressBar = mainView.findViewById(R.id.current_page_progress_bar);
        nextButton = mainView.findViewById(R.id.next_fab);
        bottomAppBar = mainView.findViewById(R.id.bottom_bar);

        tabAdapter = new TabAdapter(getChildFragmentManager());
        tabAdapter.addFragment(IntroductionFragment.newInstance(), getString(R.string.news_menu_title));

        for (QuestionnaireItem questionnaire : questionnaireItems) {
            OnQuestionItemSelected questionItemSelected = (item, position) -> {
                int questionPositionInQuestionnaire = questionnaireItems.indexOf(questionnaire);
                if (questionnaire.getQuestionType() == QuestionnaireItem.QuestionType.SINGLE_BLOCK_QUESTION) {
                    questionState.put(questionPositionInQuestionnaire, new ArrayList<>(Collections.singletonList(item)));
                    next();
                } else if (questionnaire.getQuestionType() == QuestionnaireItem.QuestionType.SINGLE_CHOICE_QUESTION) {
                    questionState.put(questionPositionInQuestionnaire, new ArrayList<>(Collections.singletonList(item)));
                    next();
                } else if (questionnaire.getQuestionType() == QuestionnaireItem.QuestionType.SINGLE_MULTIPLE_CHOICE_QUESTION) {
                    ArrayList<QuestionItem> items = questionState.get(questionPositionInQuestionnaire);
                    if (items != null) {
                        items.add(item);
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
        }

        tabAdapter.addFragment(resultFragment , "Result");

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
        mainViewPager.setOffscreenPageLimit(tabAdapter.getCount());

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(pageChangeProgressBar.getProgress(), (1f / (float) tabAdapter.getCount()) * 100f);
        valueAnimator.addUpdateListener(animation -> pageChangeProgressBar.setProgress(Math.round((float) animation.getAnimatedValue())));
        valueAnimator.start();

        nextButton.setOnClickListener(v -> next());
        bottomAppBar.setNavigationOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) ((MainActivity) getActivity()).callBackOnParentFragment();
        });

        return mainView;
    }

}
