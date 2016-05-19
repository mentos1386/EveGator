package com.mentos1386.evegator.Interfaces;

import com.google.gson.JsonObject;

public interface EndpointInterface {

    // Send HTTP_GET for list of items
    JsonObject get();

    // Send HTTP_POST to root
    JsonObject post(JsonObject body);

    // Send HTTP_DELETE on root
    void delete();
}
