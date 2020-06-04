package com.kirsh.todoboom;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.kirsh.todoboom.R;

public class CompletedMessageActivity extends AppCompatActivity {

    final String TAG = "CompletedTodoActivity";
    final String UNMARKED_DONE_MESSAGE = "Unmarked todo";
    final String DELETED_TODO_MESSAGE = "Todo has been permanently deleted";

    TextView textViewContent;
    TodoMessage todoMessage;
    DatabaseCommunicator mCommunicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // init vars
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_message);

        Intent intent = getIntent();
        final String todoId = intent.getStringExtra(TodoAdapter.TODO_ID);

        textViewContent = findViewById(R.id.complete_textview);

        Button unMarkButton = findViewById(R.id.unMarkButton);

        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setBackgroundColor(Color.RED);

        // init todoMessage specifics
        SuperApp app = (SuperApp) getApplicationContext();
        mCommunicator = app.mCommunicator;

        todoMessage = mCommunicator.getTodoFromId(todoId);
        textViewContent.setText(todoMessage.content);

        unMarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCommunicator.flipDone(todoMessage);
                Snackbar.make(view, UNMARKED_DONE_MESSAGE, Snackbar.LENGTH_SHORT).show();
                openMain();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view){
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CompletedMessageActivity.this);
                alertBuilder.setMessage("Are you sure you want to delete this todo??")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "User has deleted todo with id: " + todoId);
                                mCommunicator.deleteTodoForever(todoId);
                                Snackbar.make(view, DELETED_TODO_MESSAGE, Snackbar.LENGTH_SHORT).show();
                                openMain();
                            }});
                alertBuilder.show();
            }
        });
    }



    public void openMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
