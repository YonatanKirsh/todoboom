package com.example.postpc_ex3;

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

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoSentenceHolder>{
    private static final String SP_ADAPTER = "todo_adapter";
    public ArrayList<TodoSentence> mDataset = new ArrayList<>();
    SharedPreferences sp;

    static class TodoSentenceHolder extends RecyclerView.ViewHolder{
        TextView textView;

        TodoSentenceHolder(View view){
            super(view);
            textView = view.findViewById(R.id.todoTextView);
        }
    }

    public TodoAdapter(Context context){
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        String todosAsJson = sp.getString(SP_ADAPTER, null);
        Gson gson = new Gson();
        if (todosAsJson != null){
            mDataset = gson.fromJson(todosAsJson, new TypeToken<ArrayList<TodoSentence>>(){}.getType());
        }
    }

    TodoAdapter(ArrayList<TodoSentence> dataset){
        mDataset = dataset;
    }

    TodoAdapter(){}

    @NonNull
    @Override
    public TodoSentenceHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_sentence ,parent, false);
        final TodoSentenceHolder todoHolder = new TodoSentenceHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoSentence currentTodoSentence = mDataset.get(todoHolder.getAbsoluteAdapterPosition());
                if (!currentTodoSentence.mIsFinished){
                    currentTodoSentence.mIsFinished = true;
                    String text = currentTodoSentence.mText;
                    Snackbar.make(v, String.format("TODO %s is now DONE. BOOM!", text), Snackbar.LENGTH_LONG).show();
                    notifyUpdateMDataset();
                }
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view){
                TodoSentence currentTodoSentence = mDataset.get(todoHolder.getAbsoluteAdapterPosition());
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(parent.getContext());
                alertBuilder.setMessage("Are you sure you want to delete:\n\'" + currentTodoSentence.mText + "\'")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TodoSentence toDelete = mDataset.get(todoHolder.getAbsoluteAdapterPosition());
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
    public void onBindViewHolder(@NonNull TodoSentenceHolder holder, int position) {
        TodoSentence todoSentence = mDataset.get(position);
        holder.textView.setText(todoSentence.mText);
        if (todoSentence.mIsFinished){
            holder.textView.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    void addSentence(String sentence){
        mDataset.add(new TodoSentence(sentence));
        notifyUpdateMDataset();
    }

    private void deleteTodo(TodoSentence toDelete){
        mDataset.remove(toDelete);
        notifyUpdateMDataset();
    }

    private void notifyUpdateMDataset(){
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String todosAsJson = gson.toJson(mDataset);
        editor.putString(SP_ADAPTER, todosAsJson);
        editor.apply();
        notifyDataSetChanged();
    }
}

