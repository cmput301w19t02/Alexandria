package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.MainActivity;
import ca.ualberta.CMPUT3012019T02.alexandria.model.chatroom.ChatRoomItem;

/**
 * This class tests the ChatController
 */
@RunWith(AndroidJUnit4.class)
public class ChatControllerTests {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testAddChatRoom() {
        ChatController controller = ChatController.getInstance();

        String chatId;

        ChatRoomItem chatRoomItem = new ChatRoomItem("");

    }

    @Test
    public void testSetChatRoomReadStatus() {}

    @Test
    public void testAddTextMessage() {}

    @Test
    public void testAddLocationMessage(){}
}
