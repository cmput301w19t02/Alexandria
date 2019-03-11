package ca.ualberta.CMPUT3012019T02.alexandria.model.message;

public class TextMessage extends Message {
    /**
     * No-argument constructor is solely for the use with Firebase.
     * DO NOT USE NO-ARGUMENT CONSTRUCTOR!
     */
    @Deprecated
    public TextMessage (){}

    public TextMessage(String content, String status, String date, String sender) {
        super("text", content, status, date, sender);
    }

    public String getText() {
        return getContent();
    }

    public void setText(String text) {
        setContent(text);
    }

}
