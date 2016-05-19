package com.mentos1386.evegator.Controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mentos1386.evegator.ExceptionHandler;
import com.mentos1386.evegator.Interfaces.EndpointInterface;
import com.mentos1386.evegator.Models.AuthObject;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;

public class Endpoint implements EndpointInterface {

    private final String base = "https://crest-tq.eveonline.com/";
    private AuthObject AuthObject;
    private String uri;

    public Endpoint(String uri) {

        this.AuthObject = AuthController.getAuth();
        this.uri = uri;
    }

    private RequestBuilder request(String method) {

        // Create request builder with necessary headers
        RequestBuilder builder = new RequestBuilder(method);
        return builder.setUrl(this.base + this.uri)
                .addHeader("Authorization", "Bearer " + this.AuthObject.ACCESS_TOKEN)
                .addHeader("User-Agent", "EveGator");
    }

    /*
     * Request Executioner
     */
    private JsonObject execute(Request request) {
        AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();
        String response = "";

        // Try to make request
        try {
            response = asyncHttpClient.executeRequest(request).get().getResponseBody();
        } catch (Exception e) {
            new ExceptionHandler(e);
        }

        // Return as JsonObject
        return new JsonParser().parse(response).getAsJsonObject();
    }

    /*
    * GET Request
    */
    @Override
    public JsonObject get() {

        // Create request
        Request request = this.request("GET").build();

        // Return response
        return this.execute(request);
    }

    /*
    * POST Request
    */
    @Override
    public JsonObject post(JsonObject body) {

        // Create request
        Request request = this.request("POST")
                .setBody(body.getAsString())
                .build();

        // Return response
        return this.execute(request);
    }

    @Override
    public void delete() {

    }
}
