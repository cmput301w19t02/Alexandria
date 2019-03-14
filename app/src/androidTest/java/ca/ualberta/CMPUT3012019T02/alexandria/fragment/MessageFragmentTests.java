package ca.ualberta.CMPUT3012019T02.alexandria.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.MainActivity;

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
    public void testChatRoomIntent(){
         onView(withId(R.id.));
    }
}
