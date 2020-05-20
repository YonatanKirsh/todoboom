package com.example.postpc_ex3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final String errorMessage = "Woops! You need to enter a message first..";

    private TodoAdapter mAdapter;
    private EditText editText;
    private ArrayList<TodoSentence> todoSentences = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // init general crap
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.editText = findViewById(R.id.editText1);
        Button button = findViewById(R.id.btn1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add to list iff not empty
                Editable userInput = editText.getText();
                if (userInput.toString().isEmpty()){
                    Snackbar.make(v, errorMessage, Snackbar.LENGTH_SHORT).show();
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

        // create/re-create adapter
        if (savedInstanceState != null){
            String[] texts = savedInstanceState.getStringArray("texts");
            boolean[] dones = savedInstanceState.getBooleanArray("dones");
            if (texts != null){
                for (int i = 0; i < texts.length; ++i){
                    todoSentences.add(new TodoSentence(texts[i], dones[i]));
                }
            }
        }

        mAdapter = new TodoAdapter(todoSentences);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        int size = todoSentences.size();
        String[] texts = new String[size];
        boolean[] dones = new boolean[size];
        for (int i = 0; i < size; ++i){
            texts[i] = todoSentences.get(i).mText;
            dones[i] = todoSentences.get(i).mIsFinished;
        }

        outState.putStringArray("texts", texts);
        outState.putBooleanArray("dones", dones);
        super.onSaveInstanceState(outState);
    }
}
