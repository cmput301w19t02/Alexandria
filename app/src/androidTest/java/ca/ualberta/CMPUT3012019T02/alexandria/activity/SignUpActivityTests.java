package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.UserProfile;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

/**
 * This class tests the SignUpActivity
 */
@RunWith(AndroidJUnit4.class)
public class SignUpActivityTests {

    @Rule
    public ActivityTestRule<SignUpActivity> signUpActivityTestRule = new ActivityTestRule<>(SignUpActivity.class);

    @Test
    public void testInvalidName() {
        onView(withId(R.id.sign_up_name_field)).perform(scrollTo(), typeText(""));
        onView(withId(R.id.sign_up_usernname_field)).perform(scrollTo(), typeText("jsmith5"));
        onView(withId(R.id.sign_up_password_field)).perform(scrollTo(), typeText("John1985"));
        onView(withId(R.id.sign_up_email_field)).perform(scrollTo(), typeText("johnsmith5@example.com"));

        onView(withId(R.id.sign_up_button)).perform(click());

        onView(withText("Error: Name is invalid! Name must contain at least 4 character.")).inRoot(withDecorView(not(is(signUpActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void testInvalidUsername() {
        onView(withId(R.id.sign_up_name_field)).perform(scrollTo(), typeText("John Smith"));
        onView(withId(R.id.sign_up_usernname_field)).perform(scrollTo(), typeText("1"));
        onView(withId(R.id.sign_up_password_field)).perform(scrollTo(), typeText("John1985"));
        onView(withId(R.id.sign_up_email_field)).perform(scrollTo(), typeText("johnsmith5@example.com"));

        onView(withId(R.id.sign_up_button)).perform(click());

        onView(withText("Error: Username is invalid! Username must contain at least 4 character.")).inRoot(withDecorView(not(is(signUpActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void testInvalidPassword() {
        onView(withId(R.id.sign_up_name_field)).perform(scrollTo(), typeText("John Smith"));
        onView(withId(R.id.sign_up_usernname_field)).perform(scrollTo(), typeText("jsmith5"));
        onView(withId(R.id.sign_up_password_field)).perform(scrollTo(), typeText("5"));
        onView(withId(R.id.sign_up_email_field)).perform(scrollTo(), typeText("johnsmith5@example.com"));

        onView(withId(R.id.sign_up_button)).perform(click());

        onView(withText("Error: Password is invalid! Password must contain at least 8 characters.")).inRoot(withDecorView(not(is(signUpActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void testInvalidEmail() {
        onView(withId(R.id.sign_up_name_field)).perform(scrollTo(), typeText("John Smith"));
        onView(withId(R.id.sign_up_usernname_field)).perform(scrollTo(), typeText("jsmith5"));
        onView(withId(R.id.sign_up_password_field)).perform(scrollTo(), typeText("John1985"));
        onView(withId(R.id.sign_up_email_field)).perform(scrollTo(), typeText("not an email"));

        onView(withId(R.id.sign_up_button)).perform(click());

        onView(withText("Error: Email is invalid! Email must be valid email.")).inRoot(withDecorView(not(is(signUpActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void testValidSignUp() throws InterruptedException, TimeoutException, ExecutionException {
        String username = "test_" + UUID.randomUUID().toString().replace('-', '_');
        String password = UUID.randomUUID().toString();
        String email = username + "@example.com";

        onView(withId(R.id.sign_up_name_field)).perform(scrollTo(), typeText("John Smith"));
        onView(withId(R.id.sign_up_usernname_field)).perform(scrollTo(), typeText(username));
        onView(withId(R.id.sign_up_password_field)).perform(scrollTo(), typeText(password));
        onView(withId(R.id.sign_up_email_field)).perform(scrollTo(), typeText(email));

        onView(withId(R.id.sign_up_button)).perform(click());

        Thread.sleep(10000);

        UserProfile profile = UserController.getInstance().getMyProfile().get();

        Assert.assertEquals(email, profile.getEmail());
        Assert.assertEquals(username, profile.getUsername());
        Assert.assertEquals("John Smith", profile.getName());
    }

}
