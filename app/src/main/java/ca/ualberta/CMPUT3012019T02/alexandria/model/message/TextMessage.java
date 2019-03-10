package ca.ualberta.CMPUT3012019T02.alexandria.model.message;

import java.util.Date;

public class TextMessage extends Message {

    public TextMessage(String content, String status, String/*Date*/ date, String sender) {
        super("text", content, status, date, sender);
    }

    public String getText() {
        return getContent();
    }

    public void setText(String text) {
        setContent(text);
    }

}
