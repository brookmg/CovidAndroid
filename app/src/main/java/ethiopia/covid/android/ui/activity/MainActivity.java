package ethiopia.covid.android.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.lang.ref.WeakReference;

import ethiopia.covid.android.R;
import ethiopia.covid.android.ui.fragment.BaseFragment;
import ethiopia.covid.android.ui.fragment.HomeFragment;

import static ethiopia.covid.android.util.Constant.TAG_HOME;

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
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _fragmentContainer = findViewById(R.id.fragmentContainer);
        changeFragment(TAG_HOME, new Bundle(), null);

    }
}
