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

    void flipDone(){
        isDone = !isDone;
        updateEditTimestamp();
    }

    @Override
    public int compareTo(TodoMessage other) {
        return creationTimestamp.compareTo(other.creationTimestamp);
    }
    void setContent(String newContent){
        content = newContent;
        updateEditTimestamp();
    }
}
