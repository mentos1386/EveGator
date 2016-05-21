package com.mentos1386.evegator.Controllers.DataThreads;

import com.google.api.client.util.Data;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mentos1386.evegator.Controllers.DataController;
import com.mentos1386.evegator.Endpoint;
import com.mentos1386.evegator.EveGator;
import com.mentos1386.evegator.ExceptionHandler;
import com.mentos1386.evegator.Interfaces.EndpointInterface;
import com.mentos1386.evegator.Views.LoadingData;

public class Constellation implements Runnable {


    @Override
    public void run() {
        EndpointInterface constellationsEndpoint = new Endpoint("constellations/", false);
        JsonObject constellations = constellationsEndpoint.get();

        DataController.allConstellations = constellations.get("totalCount").getAsInt();

        for (JsonElement constellation : constellations.getAsJsonArray("items")) {
            EndpointInterface constellationEndpoint = new Endpoint(
                    "constellations/" + constellation.getAsJsonObject().get("id").getAsString() + "/",
                    false);
            JsonObject data = constellationEndpoint.get();

            try {
                JsonObject position = data.getAsJsonObject().get("position").getAsJsonObject();
                EveGator.SQLite.addConstellation(
                        constellation.getAsJsonObject().get("id").getAsInt(),
                        getId(data.get("region").getAsJsonObject().get("href").getAsString()),
                        data.get("name").getAsString(),
                        position.get("x").getAsLong(),
                        position.get("y").getAsLong(),
                        position.get("z").getAsLong());

                DataController.constellationsLoaded++;
                Thread.sleep(200);
            } catch (Exception e) {
                new ExceptionHandler(e);
            }
        }
    }

    private static int getId(String href) {
        return Integer.parseInt(href.replaceAll("[\\D]", ""));
    }
}
