package com.kirsh.todoboom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kirsh.todoboom.R;
import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity {
    final String ERROR_MESSAGE = "Woops! You need to enter a message first..";
    private final static String TAG = "Main Activity";
    private final static String CURRENT_INPUT = "current_input";

    private TodoAdapter mAdapter;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init adapter holder
        initAdapter();

        // define editText and button
        this.editText = findViewById(R.id.editText1);
        Button button = findViewById(R.id.btn1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add to list iff not empty
                Editable userInput = editText.getText();
                if (userInput.toString().isEmpty()){
                    Snackbar.make(v, ERROR_MESSAGE, Snackbar.LENGTH_SHORT).show();
                    return;
                }
                mAdapter.addSentence(userInput.toString());
                userInput.clear();
            }
        });

        // init recycler crap
        RecyclerView recyclerView = findViewById(R.id.todo_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // update input in editText
        if (savedInstanceState != null){
            String currentInput = savedInstanceState.getString(CURRENT_INPUT);
            editText.setText(currentInput);
        }
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        String currentInput = editText.getText().toString();
        outState.putString(CURRENT_INPUT, currentInput);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final SuperApp app = (SuperApp) getApplicationContext();
        DatabaseCommunicator communicator = app.mCommunicator;
        mAdapter = new TodoAdapter(communicator, this);
    }

    private void initAdapter(){
        SuperApp app = (SuperApp) getApplicationContext();
        DatabaseCommunicator communicator = app.mCommunicator;
        mAdapter = new TodoAdapter(communicator, this);
//        mAdapter = new TodoAdapter(DatabaseCommunicator.getSingleton());
        Log.d(TAG, "Todo List Size: " + mAdapter.getItemCount());
    }
}
