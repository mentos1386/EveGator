package com.mentos1386.evegator.Controllers.DataThreads;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mentos1386.evegator.Controllers.DataController;
import com.mentos1386.evegator.Endpoint;
import com.mentos1386.evegator.EveGator;
import com.mentos1386.evegator.ExceptionHandler;
import com.mentos1386.evegator.Interfaces.EndpointInterface;

public class SolarSystem implements Runnable {

    @Override
    public void run() {
        EndpointInterface solarSystemsEndpoint = new Endpoint("solarsystems/", false);
        JsonObject solarSystems = solarSystemsEndpoint.get();

        DataController.allSolarSystems = solarSystems.get("totalCount").getAsInt();

        for (JsonElement solarSystem : solarSystems.getAsJsonArray("items")) {
            EndpointInterface solarSystemEndpoint = new Endpoint(
                    "solarsystems/" + solarSystem.getAsJsonObject().get("id").getAsString() + "/",
                    false);
            JsonObject data = solarSystemEndpoint.get();

            try {
                JsonObject position = data.getAsJsonObject().get("position").getAsJsonObject();
                EveGator.SQLite.addSolarSystem(
                        data.getAsJsonObject().get("id").getAsInt(),
                        data.get("constellation").getAsJsonObject().get("id").getAsInt(),
                        data.get("name").getAsString(),
                        data.get("securityStatus").getAsLong(),
                        position.get("x").getAsLong(),
                        position.get("y").getAsLong(),
                        position.get("z").getAsLong());

                DataController.solarSystemsLoaded++;
                Thread.sleep(200);
            } catch (Exception e) {
                new ExceptionHandler(e);
            }

        }
    }
}
