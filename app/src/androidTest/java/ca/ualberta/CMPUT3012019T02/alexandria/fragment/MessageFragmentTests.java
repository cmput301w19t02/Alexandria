package ca.ualberta.CMPUT3012019T02.alexandria.fragment;

import android.content.Intent;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.ChatRoomActivity;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.MainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * This class tests the MessageFragment
 */
@RunWith(AndroidJUnit4.class)
public class MessageFragmentTests {

    @Rule
    public ActivityTestRule<MainActivity>  mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testGoToChatRoom() throws InterruptedException {
        String chatId = "Testasdf1234";
        String receiverId = "ZvLVXXLOoWTZ7o6xmW6fT4PP0Wj1";
        String receiverName = "Nighla Kay";

        Intent startChatRoomActivity = new Intent(mainActivityTestRule.getActivity(), ChatRoomActivity.class);
        startChatRoomActivity.putExtra("chatId", chatId);
        startChatRoomActivity.putExtra("receiverId", receiverId);
        startChatRoomActivity.putExtra("receiverName", receiverName);
        mainActivityTestRule.getActivity().startActivity(startChatRoomActivity);

        Thread.sleep(5000);

        onView(withId(R.id.receiver_username)).check(matches(withText(receiverName)));
    }

}
