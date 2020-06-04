package com.kirsh.todoboom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;

public class NotCompletedMessageActivity extends AppCompatActivity {

    final String TAG = "NotCompletedTodoActivity";
    final String EMPTY_TEXT_MESSAGE = "Can't have an empty todo :/\nEnter a message!";
    final String UPDATED_TODO_CONTENT_MESSAGE = "Todo has been updated!";
    final String TODO_DONE_MESSAGE = "Done!";

    TextView textViewContent;
    TextView creationDate;
    TextView editDate;
    TodoMessage todoMessage;
    DatabaseCommunicator mCommunicator;
    SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_completed_message);

        Intent intent = getIntent();
        final String todoId = intent.getStringExtra(TodoAdapter.TODO_ID);

        textViewContent = findViewById(R.id.message_content);
        Button applyButton = findViewById(R.id.applyButton);
        creationDate = findViewById(R.id.creation_date);
        editDate = findViewById(R.id.edit_date);
        Button doneButton = findViewById(R.id.done_button);

        SuperApp app = (SuperApp) getApplicationContext();
        mCommunicator = app.mCommunicator;
        todoMessage = app.mCommunicator.getTodoFromId(todoId);

        textViewContent.setText(todoMessage.content);

        String creationTimeText = "Creation time\n" + sfd.format(todoMessage.creationTimestamp.toDate());
        creationDate.setText(creationTimeText);
        updateEditTimeText();

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newContent = textViewContent.getText().toString();
                if (!newContent.isEmpty()){
                    // update edit time
                    todoMessage = mCommunicator.updateEditTime(todoMessage);
                    updateEditTimeText();
                    //update change
                    mCommunicator.setTodoContent(todoMessage, newContent);
                    Snackbar.make(view, UPDATED_TODO_CONTENT_MESSAGE, Snackbar.LENGTH_SHORT).show();
                    Log.d(TAG, "Updated todo content");
                }else {
                    Snackbar.make(view, EMPTY_TEXT_MESSAGE, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todoMessage = mCommunicator.markTodoAsDone(todoMessage);
                Snackbar.make(view, TODO_DONE_MESSAGE, Snackbar.LENGTH_SHORT).show();
                Log.d(TAG, "Marked todo done.");
                openMain();
            }
        });

    }

    public void openMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void updateEditTimeText() {
        String text = "Edit time\n" + sfd.format(todoMessage.editTimestamp.toDate());
        editDate.setText(text);
    }
}
