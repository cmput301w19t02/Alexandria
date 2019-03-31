package ca.ualberta.CMPUT3012019T02.alexandria.model.chatroom;

import android.graphics.drawable.Drawable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * A chat room item that holds the data from Firebase
 */
public class ChatRoomItem {

    private String chatId;
    private String user1Id;
    private String user1Name;
    private String user1UserPic;
    private String user2Id;
    private String user2Name;
    private String user2UserPic;
    private boolean readStatus;

    /**
     * Instantiates a new Chat room item.
     */
    public ChatRoomItem() {
    }

    /**
     * Instantiates a new Chat room item.
     *
     * @param chatRoomId the chat room id
     * @param user1Id    the user 1 id
     * @param user1Name  the user 1 name
     * @param user2Id    the user 2 id
     * @param user2Name  the user 2 name
     * @param readStatus the read status
     */
// TODO: Checks on setters for tests
    public ChatRoomItem(String chatRoomId, String user1Id, String user1Name, String user1UserPic,
                        String user2Id, String user2Name, String user2UserPic, boolean readStatus) {
        if (chatRoomId == null || chatRoomId.isEmpty()) {
            throw new IllegalArgumentException("chatRoomId cannot be null or empty");
        }
        if (user1Id == null || user1Id.isEmpty()) {
            throw new IllegalArgumentException("user1Id cannot be null or empty");
        }
        if (user1Name == null || user1Name.isEmpty()) {
            throw new IllegalArgumentException("user1Name cannot be null or empty");
        }
        if (user1UserPic == null || user1UserPic.isEmpty()) {
            throw new IllegalArgumentException("user1UserPic cannot be null or empty");
        }
        if (user2Id == null || user2Id.isEmpty()) {
            throw new IllegalArgumentException("user2Id cannot be null or empty");
        }
        if (user2Name == null || user2Name.isEmpty()) {
            throw new IllegalArgumentException("user2Name cannot be null or empty");
        }
        if ( user2UserPic == null || user2UserPic.isEmpty()) {
            throw new IllegalArgumentException("user2UserPic cannot be null or empty");
        }
        this.chatId = chatRoomId;
        this.user1Id = user1Id;
        this.user1Name = user1Name;
        this.user1UserPic = user1UserPic;
        this.user2Id = user2Id;
        this.user2Name = user2Name;
        this.user2UserPic = user2UserPic;
        this.readStatus = readStatus;
    }

    /**
     * Returns a map of the object.
     * For future uses.
     *
     * @return the map
     */
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> res = new HashMap<>();
        res.put("chatRoomId", chatId);
        res.put("user1Id", user1Id);
        res.put("user1Name",user1Name);
        res.put("user1UserPic", user1UserPic);
        res.put("user2Id", user2Id);
        res.put("user2Name", user2Name);
        res.put("user2UserPic", user2UserPic);
        res.put("readStatus", readStatus);

        return res;
    }

    // TODO: Checks on setters for tests

    /**
     * Gets user 1 id.
     *
     * @return the user 1 id
     */
    public String getUser1Id() {
        return user1Id;
    }

    /**
     * Sets user 1 id.
     *
     * @param user1Id the user 1 id
     */
    public void setUser1Id(String user1Id) {
        if (user1Id == null || user1Id.isEmpty()) {
            throw new IllegalArgumentException("Type cannot be null or empty");
        }
        this.user1Id = user1Id;
    }

    /**
     * Gets user 1 name.
     *
     * @return the user 1 name
     */
    public String getUser1Name() {
        return user1Name;
    }

    /**
     * Sets user 1 name.
     *
     * @param user1Name the user 1 name
     */
    public void setUser1Name(String user1Name) {
        if (user1Name == null || user1Name.isEmpty()) {
            throw new IllegalArgumentException("Type cannot be null or empty");
        }
        this.user1Name = user1Name;
    }

    /**
     * gets user 1 user pic id.
     *
     * @return user 1 user pic id
     */
    public String getUser1UserPic() {
        return user1UserPic;
    }

    /**
     * gets user 1 name.
     *
     * @param user1UserPic the user 1 name
     */
    public void setUser1UserPic(String user1UserPic) {
        if (user1UserPic == null || user1UserPic.isEmpty()) {
            throw new IllegalArgumentException("user1UserPic cannot be null or empty");
        }
        this.user1UserPic = user1UserPic;
    }

    /**
     * Gets user 2 id.
     *
     * @return the user 2 id
     */
    public String getUser2Id() {
        return user2Id;
    }

    /**
     * Sets user 2 id.
     *
     * @param user2Id the user 2 id
     */
    public void setUser2Id(String user2Id) {
        if (user2Id == null || user2Id.isEmpty()) {
            throw new IllegalArgumentException("Type cannot be null or empty");
        }
        this.user2Id = user2Id;
    }

    /**
     * Gets user 2 name.
     *
     * @return the user 2 name
     */
    public String getUser2Name() {
        return user2Name;
    }

    /**
     * Sets user 2 name.
     *
     * @param user2Name the user 2 name
     */
    public void setUser2Name(String user2Name) {
        if (user2Name == null || user2Name.isEmpty()) {
            throw new IllegalArgumentException("Type cannot be null or empty");
        }
        this.user2Name = user2Name;
    }

    /**
     * gets user 1 user pic id.
     *
     * @return user 1 user pic id
     */
    public String getUser2UserPic() {
        return user2UserPic;
    }

    /**
     * gets user 1 name.
     *
     * @param user2UserPic the user 1 name
     */
    public void setUser2UserPic(String user2UserPic) {
        if ( user2UserPic == null || user2UserPic.isEmpty()) {
            throw new IllegalArgumentException("user2UserPic cannot be null or empty");
        }
        this.user2UserPic = user2UserPic;
    }

    /**
     * Gets chat id.
     *
     * @return the chat id
     */
    public String getChatId() {
        return chatId;
    }

    /**
     * Sets chat id.
     *
     * @param chatRoomId the chat room id
     */
    public void setChatId(String chatRoomId) {
        if (chatRoomId == null || chatRoomId.isEmpty()) {
            throw new IllegalArgumentException("Type cannot be null or empty");
        }
        this.chatId = chatRoomId;
    }

    /**
     * Gets read status.
     *
     * @return the read status
     */
    public boolean getReadStatus() {
        return readStatus;
    }

    /**
     * Sets read status.
     *
     * @param readStatus the read status
     */
    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }
}
