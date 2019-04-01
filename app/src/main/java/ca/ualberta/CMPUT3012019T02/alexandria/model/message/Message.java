package ca.ualberta.CMPUT3012019T02.alexandria.model.message;

import java.util.HashMap;
import java.util.Map;

/**
 * The abstract message class that is used in chat rooms.
 */
public abstract class Message {

    private String id;
    private String type;
    private String content;
    private String status;
    private Long date;
    private String sender;

    /**
     * Instantiates a new Message.
     * Necessary for Firebase.
     */
    public Message(){}

    /**
     * Instantiates a new Message.
     *
     * @param type    the type
     * @param content the content
     * @param status  the status
     * @param date    the date
     * @param sender  the sender
     */
    public Message(String type, String content, String status, Long date, String sender) {
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

    /**
     * Maps the object to a hash map to be used to send data to Firebase.
     *
     * @return the map
     */
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

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Type cannot be null or empty");
        }
        if (!type.equals("text") && !type.equals("image") && !type.equals("location")) {
            throw new IllegalArgumentException("Message object needs type of text, image or location");
        }

        this.type = type;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets content.
     *
     * @param content the content
     */
    public void setContent(String content) {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }

        this.content = content;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(String status) {
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        if (!status.equals("read") && !status.equals("unread")){
            throw new IllegalArgumentException("Message object needs a status of read or unread");
        }

        this.status = status;
    }

    /**
     * Gets date.
     *
     * @return the date
     */
    public Long getDate() {
        return date;
    }

    /**
     * Sets date.
     *
     * @param date the date
     */
    public void setDate(Long date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        this.date = date;
    }

    /**
     * Gets sender.
     *
     * @return the sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * Sets sender.
     *
     * @param sender the sender
     */
    public void setSender(String sender) {
        if (sender == null || sender.isEmpty()) {
            throw new IllegalArgumentException("Sender cannot be null or empty");
        }

        this.sender = sender;
    }

}
