package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.content.Intent;

import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import androidx.test.rule.ActivityTestRule;
import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.UserProfile;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class ViewMyProfileActivityTests {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testProfileAttributes() throws InterruptedException, TimeoutException, ExecutionException {
        String username = "0457de6b_0a85_481a_9093_c73de1ba0020";
        String password = "4b5e9592-8c9e-4c37-b7d6-f5aed797e791";

        UserController.getInstance().authenticate(username,password).get(5, TimeUnit.SECONDS);
        UserProfile profile = UserController.getInstance().getMyProfile().get(5, TimeUnit.SECONDS);

        Intent startProfileActivity = new Intent(mainActivityTestRule.getActivity(), ViewMyProfileActivity.class);
        mainActivityTestRule.getActivity().startActivity(startProfileActivity);

        Thread.sleep(5000);

        onView(withId(R.id.my_profile_email)).check(matches(withText(profile.getEmail())));
        onView(withId(R.id.my_profile_name)).check(matches(withText(profile.getName())));
        onView(withId(R.id.my_profile_username)).check(matches(withText(profile.getUsername())));
    }
}
