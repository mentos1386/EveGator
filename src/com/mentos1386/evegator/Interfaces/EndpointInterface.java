package com.mentos1386.evegator.Interfaces;

import com.google.gson.JsonObject;
import java.io.IOException;

public interface EndpointInterface {

    // Send HTTP_GET for list of items
    JsonObject get() throws IOException;

    // Send HTTP_POST to root
    JsonObject post(JsonObject body) throws IOException;

    // Send HTTP_DELETE on root
    void delete() throws IOException;
}
