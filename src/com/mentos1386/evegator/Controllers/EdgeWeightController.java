package com.mentos1386.evegator.Controllers;

import com.google.common.base.Function;
import com.mentos1386.evegator.Models.SolarSystemObject;
import com.mentos1386.evegator.Models.StargateObject;

import javax.annotation.Nullable;


public class EdgeWeightController implements Function<StargateObject, Number> {

    @Nullable
    @Override
    public Number apply(@Nullable StargateObject stargateObject) {

        SolarSystemObject ss = stargateObject.getToSolarSystem();

        int penalty = 10;

        switch (PathController.modifier) {
            // Safer 0.5 - 1.0
            case 2:
                if (ss.getSecurity() >= 0.5) {
                    penalty -= 3 + ss.getSecurity() * 2;
                } else {
                    penalty += 3 + Math.abs(ss.getSecurity()) * 2;
                }
                break;
            // Less secure 0.0 - 0.4
            case 1:
                if (ss.getSecurity() < 0.5) {
                    penalty -= 3 + Math.abs(ss.getSecurity()) * 2;
                } else {
                    penalty += 3 + ss.getSecurity() * 2;
                }
                break;
            // Shorter, ignore secure stats
            case 0:
                break;
        }

        // If avoiding is enabled and Solar System is on avoid list add +20 penalty
        if (PathController.avoid && (
                PathController.avoidListSolarSystems.contains(ss) ||
                        PathController.avoidListConstellations.contains(ss.getConstellation()) ||
                        PathController.avoidListRegions.contains(ss.getRegion())
        )) {
            penalty*= 8;
            System.out.println("Penalty " +penalty +" : " + ss.getName());
        }

        return penalty;
    }
}
