package com.example.todoboom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoMessageHolder>{
    private static final String SP_ADAPTER = "todo_adapter";
    public ArrayList<TodoMessage> todoList = new ArrayList<>();
    SharedPreferences sp;

    static class TodoMessageHolder extends RecyclerView.ViewHolder{
        TextView textView;

        TodoMessageHolder(View view){
            super(view);
            textView = view.findViewById(R.id.todoTextView);
        }
    }

    public TodoAdapter(Context context){
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        String todosAsJson = sp.getString(SP_ADAPTER, null);
        Gson gson = new Gson();
        if (todosAsJson != null){
            todoList = gson.fromJson(todosAsJson, new TypeToken<ArrayList<TodoMessage>>(){}.getType());
        }
    }

    TodoAdapter(ArrayList<TodoMessage> dataset){
        todoList = dataset;
    }

    TodoAdapter(){}

    @NonNull
    @Override
    public TodoMessageHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_sentence ,parent, false);
        final TodoMessageHolder todoHolder = new TodoMessageHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoMessage currentTodoMessage = todoList.get(todoHolder.getAbsoluteAdapterPosition());
                if (!currentTodoMessage.isDone){
                    currentTodoMessage.isDone = true;
                    String text = currentTodoMessage.content;
                    Snackbar.make(v, String.format("TODO %s is now DONE. BOOM!", text), Snackbar.LENGTH_LONG).show();
                    notifyUpdateMDataset();
                }
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view){
                TodoMessage currentTodoMessage = todoList.get(todoHolder.getAbsoluteAdapterPosition());
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(parent.getContext());
                alertBuilder.setMessage("Are you sure you want to delete:\n\'" + currentTodoMessage.content + "\'")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TodoMessage toDelete = todoList.get(todoHolder.getAbsoluteAdapterPosition());
                                deleteTodo(toDelete);
                            }
                        });
                alertBuilder.show();
                return true;
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
        return todoList.size();
    }

    void addSentence(String sentence){
        todoList.add(new TodoMessage(sentence));
        notifyUpdateMDataset();
    }

    private void deleteTodo(TodoMessage toDelete){
        todoList.remove(toDelete);
        notifyUpdateMDataset();
    }

    private void notifyUpdateMDataset(){
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String todosAsJson = gson.toJson(todoList);
        editor.putString(SP_ADAPTER, todosAsJson);
        editor.apply();
        notifyDataSetChanged();
    }
}

