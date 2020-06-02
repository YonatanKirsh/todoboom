package com.example.todoboom;

import android.app.Application;

public class PreferencesApp extends Application {

    public DatabaseCommunicator mCommunicator;
    public TodoAdapter mAdapter;

    @Override
    public void onCreate(){
        super.onCreate();
        mAdapter = new TodoAdapter(this);
        mCommunicator = DatabaseCommunicator.getSingleton();
    }
}
