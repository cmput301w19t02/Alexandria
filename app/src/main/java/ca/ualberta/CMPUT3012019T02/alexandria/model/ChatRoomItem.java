package ca.ualberta.CMPUT3012019T02.alexandria.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ChatRoomItem {

    private String user1Id;
    private String user1Name;
    private String user2Id;
    private String user2Name;
    private String chatRoomId;
    private boolean readStatus;

    public ChatRoomItem() {
    }
    // TODO: Checks on setters for tests
    public ChatRoomItem(String user2Id, String chatRoomId, boolean readStatus, String user2Name) {
        this.user2Id = user2Id;
        this.chatRoomId = chatRoomId;
        this.readStatus = readStatus;
        this.user2Name = user2Name;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> res = new HashMap<>();
        res.put("chatRoomId", chatRoomId);
        res.put("user1Id", user1Id);
        res.put("user1Name",user1Name);
        res.put("user2Id", user2Id);
        res.put("user2Name", user2Name);
        res.put("readStatus", readStatus);

        return res;
    }

    // TODO: Checks on setters for tests
    public String getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(String user1Id) {
        this.user1Id = user1Id;
    }

    public String getUser1Name() {
        return user1Name;
    }

    public void setUser1Name(String user1Name) {
        this.user1Name = user1Name;
    }

    public String getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(String user2Id) {
        this.user2Id = user2Id;
    }

    public String getUser2Name() {
        return user2Name;
    }

    public void setUser2Name(String user2Name) {
        this.user2Name = user2Name;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public boolean getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }
}
