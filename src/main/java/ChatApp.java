import chatapp.AppDisplay;
import javafx.application.Application;
import javafx.scene.layout.BorderPane;

public class ChatApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(javafx.stage.Stage primaryStage) throws Exception {
        BorderPane app = new AppDisplay();

        primaryStage.setTitle("Chat App");
        primaryStage.setScene(new javafx.scene.Scene(app, 400, 600));
        primaryStage.onCloseRequestProperty().setValue(e -> System.exit(0));
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }
}
