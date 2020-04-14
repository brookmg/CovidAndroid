package ethiopia.covid.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.franmontiel.localechanger.LocaleChanger;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import ethiopia.covid.android.R;
import ethiopia.covid.android.data.QuestionnaireItem;
import ethiopia.covid.android.data.QuestionItem;
import ethiopia.covid.android.ui.fragment.BaseFragment;
import ethiopia.covid.android.ui.fragment.HomeFragment;
import ethiopia.covid.android.ui.fragment.QuestionnaireFragment;
import ethiopia.covid.android.util.Utils;
import mumayank.com.airlocationlibrary.AirLocation;

import static ethiopia.covid.android.util.Constant.TAG_HOME;
import static ethiopia.covid.android.util.Constant.TAG_QUESTIONNAIRE;
import static ethiopia.covid.android.util.Utils.readRawFile;

public class MainActivity extends AppCompatActivity {

    private FrameLayout _fragmentContainer;
    private WeakReference<Fragment> currentFragment;
    private AirLocation airLocation;
    private List<AirLocation.Callbacks> mainCallbacks = new ArrayList<>();

    private void setCurrentFragment(BaseFragment fragment) {
        currentFragment = new WeakReference<>(fragment);
    }

    public void registerLocationCallback(AirLocation.Callbacks callbacks) {
        if (!mainCallbacks.contains(callbacks)) mainCallbacks.add(callbacks);
    }

    public void unRegisterLocationCallback(AirLocation.Callbacks callbacks) {
        mainCallbacks.remove(callbacks);
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleChanger.configureBaseContext(newBase);
        super.attachBaseContext(newBase);
    }

    public void recreateActivity () {
        ActivityRecreationHelper.recreate(this , false);
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
                BaseFragment baseFragment = QuestionnaireFragment.newInstance(
                        new Gson().fromJson(
                                readRawFile(this, R.raw.sample),
                                new TypeToken<List<QuestionnaireItem>>(){}.getType()
                        )
                );
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

    public void forcefulOnBackPressed() {
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

    public void requestLocationForAll() {
        airLocation = new AirLocation(
                this, true,
                true, new AirLocation.Callbacks() {
            @Override
            public void onSuccess(@NotNull Location location) {
                for (AirLocation.Callbacks callback : mainCallbacks) callback.onSuccess(location);
            }

            @Override
            public void onFailed(@NotNull AirLocation.LocationFailedEnum locationFailedEnum) {
                for (AirLocation.Callbacks callback : mainCallbacks) callback.onFailed(locationFailedEnum);
            }
        });
    }

    public void requestLocationIndividually(AirLocation.Callbacks callbacks) {
        airLocation = new AirLocation(
                this, true,
                true, new AirLocation.Callbacks() {
            @Override
            public void onSuccess(@NotNull Location location) {
                callbacks.onSuccess(location);
            }

            @Override
            public void onFailed(@NotNull AirLocation.LocationFailedEnum locationFailedEnum) {
                callbacks.onFailed(locationFailedEnum);
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        airLocation.onActivityResult(requestCode , resultCode , data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        airLocation.onRequestPermissionsResult(requestCode , permissions, grantResults);
    }
}
