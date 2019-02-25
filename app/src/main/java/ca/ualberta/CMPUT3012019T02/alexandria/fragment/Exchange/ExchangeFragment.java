package ca.ualberta.CMPUT3012019T02.alexandria.fragment.Exchange;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.model.SectionsPageAdapter;

/**
 * Created as a fragment by MainActivity
 * Creates tab fragments
 * code for tabs adapted from https://youtu.be/bNpWGI_hGGg at 02/25/2019
 */

public class ExchangeFragment extends Fragment {

    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_exchange,null);

        mViewPager = rootView.findViewById(R.id.exchange_pager);
        setupViewPager(mViewPager);

        TabLayout tabLayout = rootView.findViewById(R.id.exchange_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return rootView;
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getChildFragmentManager());
        adapter.addFragment(new BorrowedFragment(), "Borrowed");
        adapter.addFragment(new AcceptedFragment(), "Accepted");
        adapter.addFragment(new RequestedFragment(),"Requested");
        viewPager.setAdapter(adapter);
    }

    public void onSearch(View view){}
}
