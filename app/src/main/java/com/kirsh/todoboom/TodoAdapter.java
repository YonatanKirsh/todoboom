package com.kirsh.todoboom;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoMessageHolder>{

    public final static String TODO_ID = "todoId";

    public ArrayList<TodoMessage> todoList = new ArrayList<>();
    DatabaseCommunicator mCommunicator;
    AppCompatActivity mCaller;

    static class TodoMessageHolder extends RecyclerView.ViewHolder{
        TextView textView;

        TodoMessageHolder(View view){
            super(view);
            textView = view.findViewById(R.id.todoTextView);
        }
    }

    TodoAdapter(DatabaseCommunicator communicator, AppCompatActivity caller){
        mCommunicator = communicator;
        todoList = mCommunicator.getAllTodos();
        mCaller = caller;
    }

    TodoAdapter(){}

    @NonNull
    @Override
    public TodoMessageHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_view,parent, false);
        final TodoMessageHolder todoHolder = new TodoMessageHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoMessage currentTodoMessage = todoList.get(todoHolder.getAbsoluteAdapterPosition());
                String id = currentTodoMessage.id;
                if (!currentTodoMessage.isDone){
                    openNotComplete(id);
                }else {
                    openComplete(id);
                }
            }
        });

        return todoHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TodoMessageHolder holder, int position) {
        TodoMessage todoMessage = todoList.get(position);
        holder.textView.setText(todoMessage.content);
        if (todoMessage.isDone){
            holder.textView.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        if (todoList == null){
            return 0;
        }
        return todoList.size();
    }

    void addTodoMessage(String content){
        TodoMessage newTodo = new TodoMessage(content);
        mCommunicator.addTodo(newTodo);
        notifyUpdate();
    }

    private void deleteTodo(TodoMessage toDelete){
        mCommunicator.deleteTodoForever(toDelete.id);
        notifyUpdate();
    }

    void notifyUpdate(){
        todoList = mCommunicator.getAllTodos();
        notifyDataSetChanged();
    }

    private void openNotComplete(String id) {
        Intent intent = new Intent(mCaller, NotCompletedMessageActivity.class);
        intent.putExtra(TODO_ID, id);
        mCaller.startActivity(intent);
    }

    private void openComplete(String id){
        Intent intent = new Intent(mCaller, CompletedMessageActivity.class);
        intent.putExtra(TODO_ID, id);
        mCaller.startActivity(intent);
    }
}


//view.setOnLongClickListener(new View.OnLongClickListener(){
//@Override
//public boolean onLongClick(View view){
//        TodoMessage currentTodoMessage = todoList.get(todoHolder.getAbsoluteAdapterPosition());
//        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(parent.getContext());
//        alertBuilder.setMessage("Are you sure you want to delete:\n\'" + currentTodoMessage.content + "\'")
//        .setNegativeButton("No", null)
//        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//@Override
//public void onClick(DialogInterface dialog, int which) {
//        TodoMessage toDelete = todoList.get(todoHolder.getAbsoluteAdapterPosition());
//        deleteTodo(toDelete);
//        }
//        });
//        alertBuilder.show();
//        return true;
//        }
//        });

///////

//view.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View v) {
//        TodoMessage currentTodoMessage = todoList.get(todoHolder.getAbsoluteAdapterPosition());
//        if (!currentTodoMessage.isDone){
//        String text = currentTodoMessage.content;
//        Snackbar.make(v, String.format("TODO %s is now DONE. BOOM!", text), Snackbar.LENGTH_LONG).show();
//        mCommunicator.markTodoAsDone(currentTodoMessage.id);
//        notifyUpdate();
//        }
//        }
//        });
