package chatapp;

public class MessageModal {
    enum MessageType {
        SENT, RECEIVED
    }

    // string to store our message and sender
    private User sender;
    private String message;

    // constructor.
    public MessageModal(String message, User sender) {
        this.message = message;
        this.sender = sender;
    }

    // getter and setter methods.
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
}
