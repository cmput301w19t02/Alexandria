package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.myBook.AddNewBookActivity;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.BookController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.BookParser;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import ca.ualberta.CMPUT3012019T02.alexandria.fragment.MessagesFragment;
import ca.ualberta.CMPUT3012019T02.alexandria.fragment.exchange.ExchangeFragment;
import ca.ualberta.CMPUT3012019T02.alexandria.fragment.library.LibraryFragment;
import ca.ualberta.CMPUT3012019T02.alexandria.model.BookListItem;

/**
 * The main screen that opens when the application opens
 * Code for bottom navigation bar
 * based on https://www.youtube.com/watch?v=jpaHMcQDaDg on 02/24/2019
 */
public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener, BookListProvider {

    private UserController userController = UserController.getInstance();
    private List<BookListItem> borrowedBookListings = new ArrayList<>();
    private List<BookListItem> ownedBookListings = new ArrayList<>();

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
        if(!userController.isAuthenticated()) {
            Intent startLoginActivity = new Intent(this, LoginActivity.class);
            startActivity(startLoginActivity);
        }
        else{
//            System.out.println("Already logged in");
            BookController bookController = BookController.getInstance();
            bookController.getMyBorrowedBooks().thenAcceptAsync(stringBorrowedBookHashMap -> {
                try {

                    System.out.println("Loading borrowed books");
                    // Gets accepted book list items
                    List<BookListItem> bookListItems = BookParser.UserBooksToBookList(stringBorrowedBookHashMap).get(5, TimeUnit.SECONDS);

                    borrowedBookListings = bookListItems;

                    updateFragments();

                    // Sort by alphabetical order of book titles
                    Collections.sort(borrowedBookListings, (o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getTitle(), o2.getTitle()));
                    System.out.println("Loading borrowed books");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }).exceptionally(throwable -> {
                throwable.printStackTrace();
                return null;
            });

            bookController.getMyOwnedBooks().thenAcceptAsync(stringOwnedBookHashMap -> {
                try {

                    System.out.println("Loading owned books");
                    // Gets accepted book list items
                    List<BookListItem> bookListItems = BookParser.UserBooksToBookList(stringOwnedBookHashMap).get(5, TimeUnit.SECONDS);

                    ownedBookListings = bookListItems;

                    updateFragments();

                    // Sort by alphabetical order of book titles
                    Collections.sort(ownedBookListings, (o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getTitle(), o2.getTitle()));
                    System.out.println("Finished loading owned books");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }).exceptionally(throwable -> {
                throwable.printStackTrace();
                return null;
            });
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
        // replace
        // Intent startProfileActivity = new Intent(this, ViewMyProfileActivity.class);
        //        startActivity(startProfileActivity);
        Intent intentViewImage = new Intent(this, ViewImageActivity.class);
        intentViewImage.putExtra("IMAGE_ID", "a4d0131b-ec92-4f7d-90cf-be1fb3f6427d");
        startActivity(intentViewImage);
    }

    @Override
    public List<BookListItem> getBorrowedBookList() {
        return borrowedBookListings;
    }

    @Override
    public List<BookListItem> getOwnedBookList() {
        return ownedBookListings;
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
