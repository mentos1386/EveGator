package com.mentos1386.evegator;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Authentication implements com.mentos1386.evegator.Interfaces.Authentication {

    private final String OAuthUrl = "https://login.eveonline.com/oauth/authorize";
    private final String TokenUrl = "https://login-tq.eveonline.com/oauth/token/";
    private final String redirectUrl = "http://localhost/729971297/";
    private final String client = "8e763e5cb9234108bc921eaec291c020";
    private final String Secret = "Pn5O9c3MViiu6DOUKpvTfu5zN17LRuIXSZao4jf5";
    private final String[] scopes = {
            // Navigation
            "characterNavigationWrite",
            // Location
            "characterLocationRead",
            // Bookmarks
            "characterBookmarksRead",
            // Character
            "characterClonesRead"
    };
    private String token = "";
    private String state = "";

    public void authenticate(Stage stage) {
        final String url = this.OAuthUrl + this.getQuery();
        BorderPane borderPane = new BorderPane();

        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();

        webEngine.load(url);
        borderPane.setCenter(browser);

        webEngine.setOnStatusChanged(new EventHandler<WebEvent<String>>() {
            public void handle(WebEvent<String> event) {
                if (event.getSource() instanceof WebEngine) {
                    WebEngine we = (WebEngine) event.getSource();
                    String location = we.getLocation();
                    if (location.startsWith(redirectUrl) && location.contains("code") && token.isEmpty()) {
                        try {
                            URL url = new URL(location);
                            String[] params = url.getQuery().split("&");
                            Map<String, String> map = new HashMap<String, String>();
                            for (String param : params) {
                                String name = param.split("=")[0];
                                String value = param.split("=")[1];
                                map.put(name, value);
                            }
                            String code = map.get("code");
                            // We have code so we continue the flow
                            verify(code);
                            stage.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        // create scene
        stage.setTitle("Authentication");
        Scene scene = new Scene(borderPane, 750, 500);
        stage.setScene(scene);
        stage.show();
    }

    private void verify(String code) throws IOException {
        URL obj = new URL(this.TokenUrl);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        String urlParameters = "grant_type=authorization_code&code=" + code;

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Basic " + new String(Base64.getEncoder().encode((this.client + ":" + this.Secret).getBytes())));
        con.setRequestProperty("Content-Length", String.valueOf(urlParameters.length()));
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        con.setDoOutput(true);
        con.setDoInput(true);

        DataOutputStream output = new DataOutputStream(con.getOutputStream());
        output.writeBytes(urlParameters);
        output.close();


        DataInputStream input = new DataInputStream(con.getInputStream());

        for (int c = input.read(); c != -1; c = input.read())
            System.out.print((char) c);
        input.close();

        System.out.println("Resp Code:" + con.getResponseCode());
        System.out.println("Resp Message:" + con.getResponseMessage());

    }

    private String getQuery() {
        String scope = "";
        this.generateState();

        for (String s : this.scopes) {
            scope += "+" + s;
        }

        return "?response_type=code&redirect_uri=" + this.redirectUrl +
                "&client_id=" + this.client +
                "&scope=" + scope +
                "&state=" + this.state;
    }

    private void generateState() {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        this.state = new String(array, Charset.forName("UTF-8"));
    }

    public boolean isAuthenticated() {

        return !this.token.isEmpty();
    }
}
