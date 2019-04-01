package ca.ualberta.CMPUT3012019T02.alexandria.model.message;

/**
 * A subclass of message specifically for text only messages
 *
 */
public class TextMessage extends Message {
    /**
     * No-argument constructor is solely for the use with Firebase.
     * DO NOT USE NO-ARGUMENT CONSTRUCTOR!
     */
    @Deprecated
    public TextMessage (){}

    /**
     * Instantiates a new Text message.
     *
     * @param content the content
     * @param status  the status
     * @param date    the date
     * @param sender  the sender
     */
    public TextMessage(String content, String status, Long date, String sender) {
        super("text", content, status, date, sender);
    }

    /**
     * Gets text.
     *
     * @return the text
     */
    public String getText() {
        return getContent();
    }

    /**
     * Sets text.
     *
     * @param text the text
     */
    public void setText(String text) {
        setContent(text);
    }

}
