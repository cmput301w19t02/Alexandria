package ca.ualberta.CMPUT3012019T02.alexandria.model.message;

import java.util.Date;

public abstract class Message {
    private String type;
    private String content;
    private String status;
    private Date date;
    private String sender;

    public Message(String type, String content, String status, Date date, String sender) {
        this.type = type;
        this.content = content;
        this.status = status;
        this.date = date;
        this.sender = sender;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
