package ethiopia.covid.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import ethiopia.covid.android.R;
import ethiopia.covid.android.ui.adapter.TabAdapter;

/**
 * Created by BrookMG on 3/23/2020 in ethiopia.covid.android.ui.fragment
 * inside the project CoVidEt .
 */
public class HomeFragment extends BaseFragment {

    private TabAdapter tabAdapter;
    private ViewPager viewPager;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.home_fragment, container, false);
        viewPager = mainView.findViewById(R.id.main_view_pager);

        tabAdapter = new TabAdapter(getChildFragmentManager());
        tabAdapter.addFragment(BlankFragment.newInstance(), getString(R.string.statistics_menu_title));
        tabAdapter.addFragment(BlankFragment.newInstance(), getString(R.string.covid19_menu_title));
        tabAdapter.addFragment(BlankFragment.newInstance(), getString(R.string.news_menu_title));
        tabAdapter.addFragment(BlankFragment.newInstance(), getString(R.string.contact_menu_title));

        viewPager.setAdapter(tabAdapter);
        viewPager.setOffscreenPageLimit(3);
        return mainView;
    }

}
