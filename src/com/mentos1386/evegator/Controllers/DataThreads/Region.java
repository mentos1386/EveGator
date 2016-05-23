package com.mentos1386.evegator.Controllers.DataThreads;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mentos1386.evegator.Controllers.DataController;
import com.mentos1386.evegator.Endpoint;
import com.mentos1386.evegator.EveGator;
import com.mentos1386.evegator.ExceptionHandler;
import com.mentos1386.evegator.Interfaces.EndpointInterface;

public class Region implements Runnable{

    @Override
    public void run() {
        EndpointInterface regionsEndpoint = new Endpoint("regions/", false);
        JsonObject regions = regionsEndpoint.get();

        DataController.allRegions = regions.get("totalCount").getAsInt();

        for (JsonElement region : regions.getAsJsonArray("items")) {
            EndpointInterface regionEndpoint = new Endpoint(
                    "regions/" + region.getAsJsonObject().get("id").getAsString() + "/",
                    false);
            JsonObject data = regionEndpoint.get();

            try {
                EveGator.SQLite.addRegion(
                        data.get("id").getAsInt(),
                        data.get("name").getAsString());

                DataController.regionsLoaded++;
                Thread.sleep(200);
            } catch (Exception e) {
                new ExceptionHandler(e);
            }
        }
    }
}
