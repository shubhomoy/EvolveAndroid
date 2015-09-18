package com.evolve.evolve.EvolveActivities.EvolveUtilities;

import android.app.Application;
import android.content.Context;

/**
 * Created by shubhomoy on 18/9/15.
 */
public class MyApplication extends Application {
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return instance.getApplicationContext();
    }
}
