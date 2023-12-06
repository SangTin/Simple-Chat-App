package chatapp;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Hashtable;

public class User {
    private static int UID_COUNTER = 0;

    private final int uid = ++UID_COUNTER;
    protected Image avatar;
    protected String username;
    protected final SimpleBooleanProperty isOnline;
    protected final ObservableMap<User, ArrayList<String>> messagesProperty = FXCollections.observableMap(new Hashtable<>());

    public User(String username) {
        this.username = username;
        this.isOnline = new SimpleBooleanProperty(false);
    }

    public void addMessage(User sender, String message) {
        ArrayList<String> messages = messagesProperty.getOrDefault(sender, new ArrayList<>());
        messages.add(message);
        messagesProperty.remove(sender);
        messagesProperty.put(sender, messages);
    }

    public String getMessages(User sender) {
        StringBuilder messages = new StringBuilder();
        for (String message : messagesProperty.getOrDefault(sender, new ArrayList<>())) {
            messages.append(message).append(". ");
        }
        messagesProperty.remove(sender);
        return messages.toString().replaceAll("\\s", "");
    }

    public ObservableMap<User, ArrayList<String>> getMessagesProperty() {
        return messagesProperty;
    }

    public Image getAvatar() {
        return avatar;
    }

    public String getUsername() {
        return username;
    }

    public int getUid() {
        return uid;
    }

    public SimpleBooleanProperty isOnlineProperty() {
        return isOnline;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }

    public boolean equals(User user) {
        return this.uid == user.getUid();
    }

    public boolean isOnline() {
        return isOnline.get();
    }
}
