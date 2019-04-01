package ca.ualberta.CMPUT3012019T02.alexandria.fragment;

import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import androidx.test.rule.ActivityTestRule;
import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.MainActivity;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.BookController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import ca.ualberta.CMPUT3012019T02.alexandria.fragment.exchange.AcceptedFragment;
import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class AcceptedFragmentTests {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testProfileAttributes() throws InterruptedException, TimeoutException, ExecutionException {
        String username = "0457de6b_0a85_481a_9093_c73de1ba0020";
        String password = "4b5e9592-8c9e-4c37-b7d6-f5aed797e791";

        UserController.getInstance().authenticate(username,password).get(5, TimeUnit.SECONDS);

        Thread.sleep(5000);

        Book book = BookController.getInstance().getBook("9780553582024").get(5, TimeUnit.SECONDS).get();

        AcceptedFragment frag = new AcceptedFragment();

        mainActivityTestRule.getActivity().getSupportFragmentManager()
                .beginTransaction().replace(R.id.fragment_container, frag).commit();
        Thread.sleep(5000);

        onView(withText(book.getIsbn())).check(matches(isDisplayed()));
        onView(withText(book.getAuthor())).check(matches(isDisplayed()));
        onView(withText(book.getTitle())).check(matches(isDisplayed()));
    }
}
