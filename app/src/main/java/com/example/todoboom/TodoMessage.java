package com.example.todoboom;

import java.time.LocalDateTime;

class TodoMessage {
    String content;
    boolean isDone = false;
    LocalDateTime creationTimestamp;
    LocalDateTime editTimestamp;
    String id; // todo

    TodoMessage(String input){
        creationTimestamp = LocalDateTime.now();
        editTimestamp = creationTimestamp;
        content = input;
    }

    TodoMessage(String input, boolean isFinished){
        this(input);
        isDone = isFinished;
    }
}
