package com.kirsh.todoboom;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DatabaseCommunicator {

    private static final String dbName = "TodoMessages";

    private static boolean initialized = false;
    private static DatabaseCommunicator singleton;
    private static FirebaseFirestore db;

    private DatabaseCommunicator(){
        initialized = true;
        db = FirebaseFirestore.getInstance();
    }

    public static DatabaseCommunicator getSingleton(){
        if (!initialized){
            singleton = new DatabaseCommunicator();
        }
        return singleton;
    }

    boolean addTodo(TodoMessage todo){
        return true;  //todo
    }

    boolean markTodoAsDone(TodoMessage todo){
        return true;  //todo
    }

    boolean deleteTodoForever(TodoMessage todo){
        return true;  //todo
    }

    ArrayList<TodoMessage> getAllTodos(){
        return null;  //todo
    }

    TodoMessage getTodoFromId(int id){
        return null;  //todo
    }

}
