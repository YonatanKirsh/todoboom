package com.example.postpc_ex3;

import android.app.Application;

public class PreferencesApp extends Application {


    public TodoAdapter mAdapter;

    @Override
    public void onCreate(){
        super.onCreate();
        mAdapter = new TodoAdapter(this);
    }
}
