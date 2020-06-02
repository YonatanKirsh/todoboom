package com.kirsh.todoboom;

import android.app.Application;

public class SuperApp extends Application {

    public DatabaseCommunicator mCommunicator;
    public TodoAdapter mAdapter;

    @Override
    public void onCreate(){
        super.onCreate();
        mAdapter = new TodoAdapter(this);
        mCommunicator = DatabaseCommunicator.getSingleton();
    }
}
