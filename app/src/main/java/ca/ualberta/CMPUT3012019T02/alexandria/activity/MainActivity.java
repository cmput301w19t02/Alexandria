package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import ca.ualberta.CMPUT3012019T02.alexandria.fragment.MessagesFragment;
import ca.ualberta.CMPUT3012019T02.alexandria.fragment.exchange.ExchangeFragment;
import ca.ualberta.CMPUT3012019T02.alexandria.fragment.library.LibraryFragment;

/**
 * The main screen that opens when the application opens
 * Code for bottom navigation bar
 * based on https://www.youtube.com/watch?v=jpaHMcQDaDg on 02/24/2019
 */
public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private UserController userController = UserController.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        userController.deauthenticate(); // show login page on every app startup

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        // get a specific fragment to show, otherwise, shows exchange tab be default
        Intent intent = getIntent();
        if (intent.hasExtra("fragment_name")) {
            String fragment_name = intent.getStringExtra("fragment_name");
            Fragment fragment = null;
            switch (fragment_name) {
                case "message":
                    fragment = new MessagesFragment();
                    loadFragment(fragment);
                    navigation.getMenu().getItem(2).setChecked(true);
                    break;
                case "library":
                    fragment = new LibraryFragment();
                    loadFragment(fragment);
                    navigation.getMenu().getItem(1).setChecked(true);
                    break;
                default:
                    throw new RuntimeException("Fragment name is incorrect");
            }
        } else {
            loadFragment(new ExchangeFragment());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!userController.isAuthenticated()) {
            Intent startLoginActivity = new Intent(this, LoginActivity.class);
            startActivity(startLoginActivity);
        }
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.nav_exchange:
                fragment = new ExchangeFragment();
                break;
            case R.id.nav_library:
                fragment = new LibraryFragment();
                break;
            case R.id.nav_messages:
                fragment = new MessagesFragment();
                break;
        }

        return loadFragment(fragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Handles on profile button click
     *
     * @param view the button clicked
     */
    public void onProfileButtonClick(View view) {
         Intent startProfileActivity = new Intent(this, ViewMyProfileActivity.class);
         startActivity(startProfileActivity);
    }

    private void updateFragments(){
        for (Fragment parent:getSupportFragmentManager().getFragments()){
            for(Fragment child:parent.getChildFragmentManager().getFragments()){
                parent.getChildFragmentManager().beginTransaction()
                        .detach(child).attach(child).commit();
            }
        }
    }
}
