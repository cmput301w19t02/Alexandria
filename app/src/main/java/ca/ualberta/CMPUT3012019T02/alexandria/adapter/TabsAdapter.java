package ca.ualberta.CMPUT3012019T02.alexandria.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for setting up fragment tabs in activities
 */
public class TabsAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();


    /**
     * Instantiates a new Tabs adapter.
     *
     * @param fm the FragmentManager
     */
    public TabsAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Add fragment.
     *
     * @param fragment the fragment
     * @param title    the title of the fragment
     */
    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
