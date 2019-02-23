package ca.ualberta.CMPUT3012019T02.alexandria.model.message;

import java.util.Date;

public abstract class Message {
    private String type;
    private String content;
    private String status;
    private Date date;
    private String sender;

    public Message(String type, String content, String status, Date date, String sender) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Type cannot be null or empty");
        }
        if (!type.equals("text") || !type.equals("image") || !type.equals("location")) {
            throw new IllegalArgumentException("Message object needs type of text, image or location")
        }
        if (content == null || type.isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
        if (status == null || type.isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        if (!status.equals("read") || !status.equals("unread")){
            throw new IllegalArgumentException("Message object needs a status of read or unread")
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (sender == null || type.isEmpty()) {
            throw new IllegalArgumentException("Sender cannot be null or empty");
        }

        this.type = type;
        this.content = content;
        this.status = status;
        this.date = date;
        this.sender = sender;
    }

    public String getType() {
        return type;
    }

    public String setType(String type) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Type cannot be null or empty");
        }
        if (!type.equals("text") || !type.equals("image") || !type.equals("location")) {
            throw new IllegalArgumentException("Message object needs type of text, image or location")
        }

        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }

        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        if (!status.equals("read") || !status.equals("unread")){
            throw new IllegalArgumentException("Message object needs a status of read or unread")
        }

        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        if (sender == null || sender.isEmpty()) {
            throw new IllegalArgumentException("Sender cannot be null or empty");
        }

        this.sender = sender;
    }
}
