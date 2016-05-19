package com.mentos1386.evegator.Controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mentos1386.evegator.Models.AuthObject;
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

public class AuthController {

    private static String OAUTH_URL = "https://login.eveonline.com/oauth/authorize";
    private static String TOKEN_URL = "https://login-tq.eveonline.com/oauth/token/";
    private static String OWNER_DETAILS_URL = "https://login.eveonline.com/oauth/verify";
    private static String REDIRECT_URL = "http://localhost/729971297/";
    private static String CLIENT_KEY = "8e763e5cb9234108bc921eaec291c020";
    private static String CLIENT_SECRET = "Pn5O9c3MViiu6DOUKpvTfu5zN17LRuIXSZao4jf5";
    private static String STATE;
    private static AuthObject AUTH_OBECT;
    private static String[] SCOPES = {
            // Navigation
            "characterNavigationWrite",
            // Location
            "characterLocationRead",
            // Bookmarks
            "characterBookmarksRead",
            // Character
            "characterClonesRead"
    };

    // Create browser window with Authentication
    // Monitor for redirection on REDIRECT_URL and catch data send to it
    public static void authenticate() {

        Stage window = new Stage();

        final String url = OAUTH_URL + getQuery();

        BorderPane borderPane = new BorderPane();
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();

        webEngine.load(url);
        borderPane.setCenter(browser);

        webEngine.setOnStatusChanged(event -> {
            if (event.getSource() instanceof WebEngine) {
                WebEngine we = (WebEngine) event.getSource();
                String location = we.getLocation();
                // If we are redirected to REDIRECT_URL we get contents, and send it to verify() to get access token
                if (location.startsWith(REDIRECT_URL) && location.contains("code") && AUTH_OBECT == null) try {
                    URL url1 = new URL(location);
                    String[] params = url1.getQuery().split("&");
                    Map<String, String> map = new HashMap<>();
                    for (String param : params) {
                        String name = param.split("=")[0];
                        String value = param.split("=")[1];
                        map.put(name, value);
                    }
                    String code = map.get("code");
                    // We have code so we continue the flow
                    verify(code);
                    window.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // create scene
        window.setTitle("Authentication");
        Scene scene = new Scene(borderPane, 750, 500);
        window.setScene(scene);
        window.showAndWait();
    }

    // Get Access Token from received code
    private static void verify(String code) throws IOException {
        URL obj = new URL(TOKEN_URL);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        String urlParameters = "grant_type=authorization_code&code=" + code;

        //add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Basic " + new String(Base64.getEncoder().encode((CLIENT_KEY + ":" + CLIENT_SECRET).getBytes())));
        con.setRequestProperty("Content-Length", String.valueOf(urlParameters.length()));
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        con.setDoOutput(true);
        con.setDoInput(true);

        DataOutputStream output = new DataOutputStream(con.getOutputStream());
        output.writeBytes(urlParameters);
        output.close();
        DataInputStream input = new DataInputStream(con.getInputStream());

        generateAuthObject(input);
    }

    // Create query to be used when creating Authentication request
    private static String getQuery() {
        String scope = "";
        generateState();

        for (String s : SCOPES) {
            scope += "+" + s;
        }

        return "?response_type=code&redirect_uri=" + REDIRECT_URL +
                "&client_id=" + CLIENT_KEY +
                "&scope=" + scope +
                "&STATE=" + STATE;
    }

    private static void generateAuthObject(DataInputStream input) throws IOException {

        String response = "";
        for (int c = input.read(); c != -1; c = input.read())
            response += (char) c;
        input.close();

        Gson g = new Gson();
        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();

        // Get required fields
        String accessToken = jsonObject.get("access_token").getAsString();
        String refreshToken = jsonObject.get("refresh_token").getAsString();

        // Add expiration time
        long currentTime = System.currentTimeMillis() / 1000;
        long expiresIn = jsonObject.get("expires_in").getAsLong() + currentTime;

        // Get owner details (ID)
        String owner = getOwnerDetails(accessToken);

        // Create new AuthObject with all required fields
        AUTH_OBECT = new AuthObject(accessToken, refreshToken, expiresIn, owner);
    }

    private static String getOwnerDetails(String access_token) throws IOException {

        URL obj = new URL(OWNER_DETAILS_URL);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        con.setRequestProperty("Authorization", "Bearer " + access_token);
        con.setDoOutput(true);
        con.setDoInput(true);
        DataInputStream input = new DataInputStream(con.getInputStream());

        String response = "";
        for (int c = input.read(); c != -1; c = input.read())
            response += (char) c;
        input.close();

        Gson g = new Gson();
        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();

        // Return user id
        return jsonObject.get("CharacterID").getAsString();

    }

    public static AuthObject getAuth() {
        return AUTH_OBECT;
    }

    // State is generated to be used when sending Authentication request
    private static void generateState() {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        STATE = new String(array, Charset.forName("UTF-8"));
    }
}
