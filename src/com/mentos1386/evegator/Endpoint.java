package com.mentos1386.evegator;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mentos1386.evegator.Interfaces.EndpointInterface;
import com.mentos1386.evegator.Objects.AuthObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;

public class Endpoint implements EndpointInterface {

    private HttpsURLConnection con;
    private String requestMethod = "GET";
    private AuthObject AuthObject;
    private String base = "https://crest-tq.eveonline.com/";
    private String uri;

    public Endpoint(String uri) throws IOException {

        this.AuthObject = EveGator.AUTHENTICATION;
        this.uri = uri;

        URL obj = new URL(this.base + this.uri);
        this.con = (HttpsURLConnection) obj.openConnection();

        this.createHeaders();
    }

    private void createHeaders() throws IOException {
        //add request header
        this.con.setRequestProperty("Authorization", "Bearer " + this.AuthObject.ACCESS_TOKEN);
        this.con.setRequestProperty("User-Agent", "EveGator");
    }

    @Override
    public JsonObject get() throws IOException {
        con.setDoOutput(true);
        con.setDoInput(true);

        DataInputStream input = new DataInputStream(con.getInputStream());
        String response = "";
        for (int c = input.read(); c != -1; c = input.read())
            response += (char) c;
        input.close();

        return new JsonParser().parse(response).getAsJsonObject();
    }

    @Override
    public JsonObject post(JsonObject body) {
        return null;
    }

    @Override
    public void delete() {

    }
}
