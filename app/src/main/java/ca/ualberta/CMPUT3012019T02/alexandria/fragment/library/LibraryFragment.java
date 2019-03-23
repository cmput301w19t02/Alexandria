package ca.ualberta.CMPUT3012019T02.alexandria.fragment.library;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.myBook.AddNewBookActivity;
import ca.ualberta.CMPUT3012019T02.alexandria.adapter.TabsAdapter;

/**
 * Created as a fragment by MainActivity
 * Creates tab fragments, AllTabFragment, AvailableFragment,LoanedFragment
 * code for tabs adapted from https://youtu.be/bNpWGI_hGGg at 02/25/2019
 */
public class LibraryFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_library,null);

        ViewPager mViewPager = rootView.findViewById(R.id.library_pager);
        setupViewPager(mViewPager);

        TabLayout tabLayout = rootView.findViewById(R.id.library_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton addButton = rootView.findViewById(R.id.add_book_button);
        addButton.setOnClickListener(this);

        return rootView;
    }

    /**
     * Sets up tabs in the Fragment
     * @param viewPager
     */

    private void setupViewPager(ViewPager viewPager) {
        TabsAdapter adapter = new TabsAdapter(getChildFragmentManager());
        adapter.addFragment(new AllTabFragment(), "All");
        adapter.addFragment(new AvailableFragment(), "Available");
        adapter.addFragment(new LoanedFragment(),"Loaned");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        Intent startAddMyBookActivity = new Intent(getActivity(), AddNewBookActivity.class);
        startActivity(startAddMyBookActivity);
    }
}
