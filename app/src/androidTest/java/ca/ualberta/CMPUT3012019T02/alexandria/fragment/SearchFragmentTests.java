package ca.ualberta.CMPUT3012019T02.alexandria.fragment;

import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import androidx.test.rule.ActivityTestRule;
import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.MainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class SearchFragmentTests {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testProfileAttributes() throws InterruptedException, TimeoutException, ExecutionException {
        Thread.sleep(5000);
        onView(withId(R.id.exchange_search)).perform(click());
        Thread.sleep(5000);
        onView(withId(R.id.search_input)).perform(typeText("Harry Potter and the Philosopher's Stone"));
        Thread.sleep(1000);
        onView(withText("9781781102459")).check(matches(isDisplayed()));
    }
}
