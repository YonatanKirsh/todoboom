package com.kirsh.todoboom;

import com.google.firebase.Timestamp;

class TodoMessage implements Comparable<TodoMessage>{
    public String content;
    public boolean isDone = false;
    public Timestamp creationTimestamp;
    public Timestamp editTimestamp;
    public String id;

    // must include for Firestorm compatibility
    TodoMessage(){}

    TodoMessage(String input){
        creationTimestamp = Timestamp.now();
        editTimestamp = creationTimestamp;
        content = input;
    }


    void updateEditTimestamp(){ editTimestamp = Timestamp.now(); }

    void markAsDone(){
        isDone = true;
        updateEditTimestamp();
    }

    @Override
    public int compareTo(TodoMessage other) {
        return creationTimestamp.compareTo(other.creationTimestamp);
    }
}
