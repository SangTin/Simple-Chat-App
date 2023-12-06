package chatapp;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.sql.Array;
import java.util.ArrayList;

public class AppDisplay extends BorderPane {
    @FXML private ScrollPane messagePane;
    @FXML private VBox messageArea;
    @FXML private ImageView userAvatar;
    @FXML private Label userStatus;
    @FXML private TextField messageInput;
    @FXML private Button sendButton;

    private final User sender = new User("User");
    private final User receiver = new ChatBot();

    public AppDisplay() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AppDisplay.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void initialize() {
        userAvatar.setImage(receiver.getAvatar());
        if (receiver.isOnline()) {
            userStatus.setText("Online");
            userStatus.getStyleClass().add("online");
        } else {
            userStatus.setText("Offline");
            userStatus.getStyleClass().remove("online");
        }
        receiver.isOnlineProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                userStatus.setText("Online");
                userStatus.getStyleClass().add("online");
            } else {
                userStatus.setText("Offline");
                userStatus.getStyleClass().remove("online");
            }
        });
        sendButton.setOnAction(e -> {
            sendMessage();
        });
        messageInput.setOnAction(e -> {
            sendMessage();
        });
        sender.getMessagesProperty().addListener((MapChangeListener<User, ArrayList<String>>) change -> {
            if (change.wasAdded() && change.getKey().equals(receiver)) {
                MessageModal messageModal = new MessageModal(change.getValueAdded().getLast(), receiver);
                addMessage(messageModal, MessageModal.MessageType.RECEIVED);
                sender.getMessages(receiver);
            }
        });
        messageArea.heightProperty().addListener((observable, oldValue, newValue) -> {
            messagePane.setVvalue(1.0);
        });
    }

    private void sendMessage() {
        String message = messageInput.getText();
        if (!message.isEmpty()) {
            MessageModal messageModal = new MessageModal(message, sender);
            addMessage(messageModal, MessageModal.MessageType.SENT);
            receiver.addMessage(sender, message);
            messageInput.setText("");
        }
    }

    private void addMessage(MessageModal message, MessageModal.MessageType type) {
        HBox messageRow = createMessageRow(message, type);
        messageArea.getChildren().add(messageRow);
    }

    private HBox createMessageRow(MessageModal message, MessageModal.MessageType type) {
        HBox messageContainer = new HBox();
        messageContainer.getStyleClass().add("message-pane");
        Label messageLabel = new Label(message.getMessage());
        messageLabel.getStyleClass().add("message-text");
        messageContainer.getChildren().add(messageLabel);

        HBox messageRow = new HBox();
        messageRow.getStyleClass().add("message-row");
        messageRow.getStyleClass().add(type == MessageModal.MessageType.SENT ? "sent" : "received");
        messageRow.getChildren().add(messageContainer);
        return messageRow;
    }
}
