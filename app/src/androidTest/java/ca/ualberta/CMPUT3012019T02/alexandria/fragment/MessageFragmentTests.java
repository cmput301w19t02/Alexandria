package ca.ualberta.CMPUT3012019T02.alexandria.fragment;

import android.support.v4.app.Fragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.ChatRoomActivity;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.MainActivity;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * This class tests the MessageFragment
 */
@RunWith(AndroidJUnit4.class)
public class MessageFragmentTests {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new
            ActivityTestRule<>(MainActivity.class);

    @Before
    public void init(){
        Fragment fragment = new MessagesFragment();
        activityTestRule.getActivity().getSupportFragmentManager()
                .beginTransaction().replace(R.id.fragment_container, fragment).commit();

    }

    @Test
    public void testMessageFragToChatRoomActIntent() throws InterruptedException {
        // NEEDS TO HAVE A CHAT ROOM BEFORE A TEST IS RAN, else chat_recycler is hidden.
        Thread.sleep(5000);

        onView(withId(R.id.chat_recycler)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        intended(hasComponent(ChatRoomActivity.class.getName()));

        Thread.sleep(500);
        onView(withId(R.id.reciever_username)).check(matches(isDisplayed()));
    }
}
