package com.kirsh.todoboom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
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
                    //update change
                    todoMessage = mCommunicator.setTodoContent(todoMessage, newContent);
                    showSnackbarTop(view, UPDATED_TODO_CONTENT_MESSAGE, Snackbar.LENGTH_SHORT);
                    updateEditTimeText();
                    Log.d(TAG, "Updated todo content");
                }else {
                    showSnackbarTop(view, EMPTY_TEXT_MESSAGE, Snackbar.LENGTH_SHORT);
                }
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todoMessage = mCommunicator.flipDone(todoMessage);
                showSnackbarTop(view, TODO_DONE_MESSAGE, Snackbar.LENGTH_SHORT);
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

    private void showSnackbarTop(View v, String message, int length){
        Snackbar snack = Snackbar.make(v, message, length);
        View snackView = snack.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackView.getLayoutParams();
        params.gravity = Gravity.TOP;
        snackView.setLayoutParams(params);
        snack.show();
    }
}
