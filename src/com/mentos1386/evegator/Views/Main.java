package com.mentos1386.evegator.Views;

import com.mentos1386.evegator.EveGator;
import com.mentos1386.evegator.Interfaces.ViewInterface;
import javafx.scene.layout.Pane;

public class Main implements ViewInterface {

    @Override
    public Pane build() {


        EveGator.dataCon.solarSystems.forEach((integer, solarSystemObject) -> solarSystemObject.getStargates().forEach((solarSystemObject1, stargateObject) -> System.out.println(stargateObject)));

        return null;
    }
}
