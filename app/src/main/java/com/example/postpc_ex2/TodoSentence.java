package com.example.postpc_ex2;

class TodoSentence{
    String mText;
    boolean mIsFinished = false;

    TodoSentence(String input){
        mText = input;
    }

    TodoSentence(String input, boolean isFinished){
        mText = input;
        mIsFinished = isFinished;
    }
}
