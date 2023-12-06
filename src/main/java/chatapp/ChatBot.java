package chatapp;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.collections.MapChangeListener;
import javafx.scene.image.Image;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;
import java.util.Timer;

public class ChatBot extends User {
    Hashtable<User, Timer> timers = new Hashtable<>();

    public ChatBot() {
        super("Zeno Bot");
        avatar = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/bot.png")));
        startOnlineThread();
        setupGetResponseListener();
    }

    private void setupGetResponseListener() {
        messagesProperty.addListener((MapChangeListener<User, ArrayList<String>>) change -> {
            if (change.wasAdded()) {
                User sender = change.getKey();
                Timer timer = timers.getOrDefault(sender, new Timer());
                timer.cancel();
                timer.purge();
                timer = new Timer();
                timer.schedule(new SendMessageTask(sender), 2000);
                timers.put(sender, timer);
            }
        });
    }

    private void startOnlineThread() {
        Thread onlineThread = new Thread(() -> {
            while (true) {
                try {
                    boolean online = checkOnline();
                    Platform.runLater(() -> {
                        isOnline.set(online);
                    });
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        onlineThread.setDaemon(true);
        onlineThread.start();
    }

    private static boolean checkOnline() {
        String url = "http://api.brainshop.ai/get";
        HttpClient httpclient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        try {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(httpclient.send(request, HttpResponse.BodyHandlers.ofString()).body(), JsonObject.class);
            return true;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    private static String getResponseFromBrainShop(User sender, String message) {
        String response = "";
        String url = String.format("http://api.brainshop.ai/get?bid=179347&key=2Iv5cUKYjU8FcRgY&uid=%d&msg=%s", sender.getUid(), message);
        HttpClient httpclient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        try {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(httpclient.send(request, HttpResponse.BodyHandlers.ofString()).body(), JsonObject.class);
            response = jsonObject.get("cnt").getAsString();
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

        return response;
    }

    private class SendMessageTask extends java.util.TimerTask {
        private final User sender;

        public SendMessageTask(User sender) {
            this.sender = sender;
        }

        @Override
        public void run() {
            String message = getMessages(sender);
            String response = getResponseFromBrainShop(sender, message);
            Platform.runLater(() -> {
                sender.addMessage(ChatBot.this, response);
            });
        }
    }
}
