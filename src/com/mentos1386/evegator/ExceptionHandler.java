package com.mentos1386.evegator;

public class ExceptionHandler {

    public ExceptionHandler(Exception e) {

        e.printStackTrace();
        System.out.println("[ERROR] " + e.getMessage());
    }
}
