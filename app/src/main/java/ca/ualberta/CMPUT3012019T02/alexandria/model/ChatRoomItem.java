package ca.ualberta.CMPUT3012019T02.alexandria.model;

public class ChatRoomItem {

    private String recieverId;
    private String recieverUsername;
    private String chatRoomId;
    private boolean readStatus;

    public ChatRoomItem() {
    }
    // TODO: Checks on setters for tests
    public ChatRoomItem(String recieverId, String chatRoomId, boolean readStatus, String recieverUsername) {
        this.recieverId = recieverId;
        this.chatRoomId = chatRoomId;
        this.readStatus = readStatus;
        this.recieverUsername = recieverUsername;
    }

    public String getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(String recieverId) {
        this.recieverId = recieverId;
    }

    public String getRecieverUsername() {
        return recieverUsername;
    }

    public void setRecieverUsername(String recieverUsername) {
        this.recieverUsername = recieverUsername;
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
