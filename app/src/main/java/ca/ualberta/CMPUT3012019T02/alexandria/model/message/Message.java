package ca.ualberta.CMPUT3012019T02.alexandria.model.message;

import java.util.HashMap;
import java.util.Map;

public abstract class Message {

    private String id;
    private String type;
    private String content;
    private String status;
    private String date;
    private String sender;

    public Message(){}

    public Message(String type, String content, String status, String date, String sender) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Type cannot be null or empty");
        }
        if (!type.equals("text") && !type.equals("image") && !type.equals("location")) {
            throw new IllegalArgumentException("Message object needs type of text, image or location");
        }
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        if (!status.equals("read") && !status.equals("unread")){
            throw new IllegalArgumentException("Message object needs a status of read or unread");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (sender == null || sender.isEmpty()) {
            throw new IllegalArgumentException("Sender cannot be null or empty");
        }

        this.type = type;
        this.content = content;
        this.status = status;
        this.date = date;
        this.sender = sender;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> res = new HashMap<>();
        res.put("id", id);
        res.put("type", type);
        res.put("status", status);
        res.put("date", date);
        res.put("sender", sender);
        res.put("content", content);

        return res;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Type cannot be null or empty");
        }
        if (!type.equals("text") && !type.equals("image") && !type.equals("location")) {
            throw new IllegalArgumentException("Message object needs type of text, image or location");
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
        if (!status.equals("read") && !status.equals("unread")){
            throw new IllegalArgumentException("Message object needs a status of read or unread");
        }

        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
