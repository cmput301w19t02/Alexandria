package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import ca.ualberta.CMPUT3012019T02.alexandria.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * This class tests the LoginActivity
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityTests {

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    ViewAction forcedClick =
            new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return ViewMatchers.isEnabled();
                }

                @Override
                public String getDescription() {
                    return null;
                }

                @Override
                public void perform(UiController uiController, View view) {
                    view.performClick();
                }
            };

    @Test
    public void testValidLogin() throws InterruptedException {
        String username = "0457de6b_0a85_481a_9093_c73de1ba0020";
        String password = "4b5e9592-8c9e-4c37-b7d6-f5aed797e791";

        onView(withId(R.id.login_username_field)).perform(scrollTo(), typeText(username));
        onView(withId(R.id.login_password_field)).perform(scrollTo(), typeText(password));

        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(10000);

        Assert.assertTrue(loginActivityTestRule.getActivity().isFinishing());
    }

    @Test
    public void testInvalidPassword() throws InterruptedException {
        String username = "0457de6b_0a85_481a_9093_c73de1ba0020";

        onView(withId(R.id.login_username_field)).perform(scrollTo(), typeText(username));
        onView(withId(R.id.login_password_field)).perform(scrollTo(), typeText(""));

        onView(withId(R.id.login_button)).perform(click());

        onView(withId(R.id.error_message)).check(matches(withText(R.string.login_error)));
    }

    @Test
    public void testInvalidUsername() {
        String password = "4b5e9592-8c9e-4c37-b7d6-f5aed797e791";

        onView(withId(R.id.login_username_field)).perform(scrollTo(), typeText(""));
        onView(withId(R.id.login_password_field)).perform(scrollTo(), typeText(password));

        onView(withId(R.id.login_button)).perform(click());

        onView(withId(R.id.error_message)).check(matches(withText(R.string.login_error)));
    }

    @Test
    public void testNavigateToSignUp() throws InterruptedException {

        onView(withId(R.id.to_sign_up)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.sign_up_button)).check(matches(isDisplayed()));

        onView(allOf(withParent(withId(R.id.sign_up_toolbar)),isClickable())).perform(forcedClick);

        Thread.sleep(1000);

        onView(withId(R.id.login_button)).check(matches(isDisplayed()));
    }

}
