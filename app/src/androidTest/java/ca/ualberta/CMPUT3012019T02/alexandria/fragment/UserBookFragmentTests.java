package ca.ualberta.CMPUT3012019T02.alexandria.fragment;

import android.os.Bundle;

import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import androidx.test.rule.ActivityTestRule;
import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.MainActivity;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.BookController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class UserBookFragmentTests {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testProfileAttributes() throws InterruptedException, TimeoutException, ExecutionException {
        String userID = "b83muJfZ2cTgU9A5R6VrtvvdwAm2";

        Book book = BookController.getInstance().getBook("9781781102459").get(5, TimeUnit.SECONDS).get();

        Bundle b = new Bundle();

        b.putString("isbn", book.getIsbn());
        b.putString("ownerId", userID);
        b.putString("status", "available");
        UserBookFragment frag = new UserBookFragment();

        frag.setArguments(b);
        mainActivityTestRule.getActivity().getSupportFragmentManager()
                .beginTransaction().replace(R.id.fragment_container, frag).commit();

        Thread.sleep(5000);

        onView(withText(book.getIsbn())).check(matches(isDisplayed()));
        onView(withText(book.getAuthor())).check(matches(isDisplayed()));
        onView(withText(book.getTitle())).check(matches(isDisplayed()));
    }
}
