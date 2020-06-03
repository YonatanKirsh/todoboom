package com.kirsh.todoboom;

import android.app.Application;

public class SuperApp extends Application {

    public DatabaseCommunicator mCommunicator;

    @Override
    public void onCreate(){
        super.onCreate();
        mCommunicator = DatabaseCommunicator.getInstance();
    }
}
