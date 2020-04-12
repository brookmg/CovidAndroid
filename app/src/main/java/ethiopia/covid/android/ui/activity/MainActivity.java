package ethiopia.covid.android.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.Arrays;

import ethiopia.covid.android.R;
import ethiopia.covid.android.data.QuestionnaireItem;
import ethiopia.covid.android.data.QuestionItem;
import ethiopia.covid.android.ui.fragment.BaseFragment;
import ethiopia.covid.android.ui.fragment.HomeFragment;
import ethiopia.covid.android.ui.fragment.QuestionnaireFragment;
import ethiopia.covid.android.util.Utils;

import static ethiopia.covid.android.util.Constant.TAG_HOME;
import static ethiopia.covid.android.util.Constant.TAG_QUESTIONNAIRE;

public class MainActivity extends AppCompatActivity {

    private FrameLayout _fragmentContainer;
    private WeakReference<Fragment> currentFragment;

    private void setCurrentFragment(BaseFragment fragment) {
        currentFragment = new WeakReference<>(fragment);
    }

    private void fragStarter (String fragTag , BaseFragment baseFragment , Bundle bundle, View sharedView) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer , baseFragment , fragTag);
        if (sharedView != null){
            String transitionName = ViewCompat.getTransitionName(sharedView);
            fragmentTransaction.addSharedElement(sharedView , transitionName != null ? transitionName : "nothing_here");
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        setCurrentFragment(baseFragment);
    }

    public void changeFragment (String fragmentTag , Bundle bundle, @Nullable View view) {
        switch (fragmentTag) {
            case TAG_HOME: {
                BaseFragment baseFragment = HomeFragment.newInstance();
                fragStarter(fragmentTag , baseFragment , bundle, view);
                break;
            }

            case TAG_QUESTIONNAIRE: {
                BaseFragment baseFragment = QuestionnaireFragment.newInstance(Arrays.asList(
                        new QuestionnaireItem(
                                QuestionnaireItem.QuestionType.SINGLE_BLOCK_QUESTION,
                                "How are you feeling today?",
                                Arrays.asList(
                                    new QuestionItem("Good", R.drawable.ic_details),
                                    new QuestionItem("Bad", R.drawable.ic_details)
                                )
                        ),
                        new QuestionnaireItem(
                                QuestionnaireItem.QuestionType.SINGLE_CHOICE_QUESTION,
                                "In which region are you currently located",
                                Arrays.asList(
                                    new QuestionItem("Addis Abeba", R.drawable.ic_details),
                                    new QuestionItem("Bahir dar", R.drawable.ic_details),
                                    new QuestionItem("Gondar", R.drawable.ic_details),
                                    new QuestionItem("Hawassa", R.drawable.ic_details),
                                    new QuestionItem("Mekelle", R.drawable.ic_details)
                                )
                        ),
                        new QuestionnaireItem(
                                QuestionnaireItem.QuestionType.SINGLE_MULTIPLE_CHOICE_QUESTION,
                                "What are you feeling today?",
                                Arrays.asList(
                                        new QuestionItem("Sweating", R.drawable.ic_details),
                                        new QuestionItem("Coughing", R.drawable.ic_details),
                                        new QuestionItem("Sour throat", R.drawable.ic_details),
                                        new QuestionItem("Diarrhea", R.drawable.ic_details)
                                )
                        )
                ));
                fragStarter(fragmentTag , baseFragment, bundle, view);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (currentFragment.get() != null && ((BaseFragment) currentFragment.get()).canGoBack()) {
            callBackOnParentFragment();
        } else {

            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                //there are more items in the back stack. We are not on the home frag
                getSupportFragmentManager().popBackStackImmediate();
            } else {
                Snackbar.make(_fragmentContainer, "Press back again or 👉🏾 button", Snackbar.LENGTH_SHORT)
                        .setAction("Exit", (v) -> finish()).show();
            }

            if (getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1)
                    instanceof BaseFragment)
                setCurrentFragment((BaseFragment) getSupportFragmentManager().getFragments()
                        .get(getSupportFragmentManager().getFragments().size() - 1));
        }
    }


    public void changeTheme() {
        int currentTheme = Utils.getCurrentTheme(this);
        if (currentTheme == 0) Utils.setCurrentTheme(this , 1);
        else Utils.setCurrentTheme(this, 0);
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(0,0);
        finish();
    }

    public void callNextOnParentFragment() {
        if (currentFragment.get() != null) ((BaseFragment) currentFragment.get()).next();
    }

    public void callBackOnParentFragment() {
        if (currentFragment.get() != null) ((BaseFragment) currentFragment.get()).back();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Utils.getCurrentTheme(this) == 0 ? R.style.LightTheme : R.style.DarkTheme);
        setContentView(R.layout.activity_main);

        _fragmentContainer = findViewById(R.id.fragmentContainer);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (Utils.getCurrentTheme(this) == 0)
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                );
            else
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                );


        changeFragment(TAG_HOME, new Bundle(), null);

    }
}
