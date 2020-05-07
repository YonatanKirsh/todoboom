package com.example.postpc_ex2;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoSentenceHolder>{
    private ArrayList<TodoSentence> mDataset = new ArrayList<>();

    static class TodoSentenceHolder extends RecyclerView.ViewHolder{
        TextView textView;

        TodoSentenceHolder(View view){
            super(view);
            textView = view.findViewById(R.id.todoTextView);
        }
    }

    TodoAdapter(ArrayList<TodoSentence> dataset){
        mDataset = dataset;
    }

    TodoAdapter(){}

    @NonNull
    @Override
    public TodoSentenceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_sentence ,parent, false);
        final TodoSentenceHolder todoHolder = new TodoSentenceHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoSentence todoSentence = mDataset.get(todoHolder.getAbsoluteAdapterPosition());
                if (!todoSentence.mIsFinished){
                    todoSentence.mIsFinished = true;
                    String text = todoSentence.mText;
                    Snackbar.make(v, String.format("TODO %s is now DONE. BOOM!", text), Snackbar.LENGTH_LONG).show();
                    notifyDataSetChanged();
                }
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
        notifyDataSetChanged();
    }
}

