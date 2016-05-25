package com.mentos1386.evegator.Views;

import com.mentos1386.evegator.EveGator;
import com.mentos1386.evegator.Interfaces.ViewInterface;
import javafx.scene.Scene;

public class Main implements ViewInterface {

    @Override
    public Scene build() {

        EveGator.gb.init();


        return EveGator.gb.build();
    }
}
