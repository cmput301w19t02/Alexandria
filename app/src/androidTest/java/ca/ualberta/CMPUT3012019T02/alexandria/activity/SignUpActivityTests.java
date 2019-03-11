package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import ca.ualberta.CMPUT3012019T02.alexandria.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * This class tests the SignUpActivity
 */
@RunWith(AndroidJUnit4.class)
public class SignUpActivityTests {

    @Rule
    public ActivityTestRule<SignUpActivity> signUpActivityTestRule = new ActivityTestRule<>(SignUpActivity.class);

    @Test
    public void testInvalidUsername(){
        onView(withId(R.id.sign_up_name_field)).perform(typeText("John Smith"));
        onView(withId(R.id.sign_up_usernname_field)).perform(typeText("jsmith5"));
        onView(withId(R.id.sign_up_password_field)).perform(typeText("John1985"));
        onView(withId(R.id.sign_up_email_field)).perform(typeText("johnsmith5@example.com"));

        onView(withId(R.id.sign_up_button)).perform(click());
    }
}
