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

public class ViewUserProfileActivityTests {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testProfileAttributes() throws InterruptedException, TimeoutException, ExecutionException {
        String userID = "eQgZfhN2Yng9TPHcXvfBZs5ZKxj1";

        UserProfile profile = UserController.getInstance().getUserProfile(userID).get(5, TimeUnit.SECONDS);

        Intent startViewProfile = new Intent(mainActivityTestRule.getActivity(), ViewUserProfileActivity.class);
        startViewProfile.putExtra("USER_ID",userID);
        mainActivityTestRule.getActivity().startActivity(startViewProfile);

        Thread.sleep(5000);

        onView(withId(R.id.user_profile_name)).check(matches(withText(profile.getName())));
        onView(withId(R.id.user_profile_username)).check(matches(withText(profile.getUsername())));
    }
}
