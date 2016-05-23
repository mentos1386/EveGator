package com.mentos1386.evegator.Controllers;

import javafx.concurrent.Task;

public abstract class DataTask<V> extends Task<V>{
    // Added public updateDataLoading function that is called from DataController
    public void updateDataLoading(double current, double max)
    {
        updateProgress(current, max);
    }
}
