package com.kirsh.todoboom;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static java.util.Collections.sort;

public class DatabaseCommunicator {

    private static final String TAG = "Firestorm Communicator";
    private static final String COLLECTION_NAME = "TodoMessages";

    private static DatabaseCommunicator singleton = null;
    private CollectionReference collectionReference;
    private ArrayList<TodoMessage> todoMessages;

    private DatabaseCommunicator(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        collectionReference = db.collection(COLLECTION_NAME);
        setListen();
    }

    static DatabaseCommunicator getInstance(){
        if (singleton == null){
            singleton = new DatabaseCommunicator();
        }
        return singleton;
    }

    void setListen(){
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    Log.d(TAG, "Failed to connect to Firestore", e);
                    return;
                }
                if (querySnapshots == null){
                    Log.d(TAG, "No data on Firestore");
                    return;
                }
                todoMessages = (ArrayList<TodoMessage>) querySnapshots.toObjects(TodoMessage.class);
                sort(todoMessages);
            }
        });

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                todoMessages = (ArrayList<TodoMessage>) task.getResult().toObjects(TodoMessage.class);
                sort(todoMessages);
            }
        });

        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshots) {
                todoMessages = (ArrayList<TodoMessage>) querySnapshots.toObjects(TodoMessage.class);
                sort(todoMessages);
            }
        });
    }

    void addTodo(final TodoMessage todo){
        DocumentReference newDoc = collectionReference.document();
        todo.id = newDoc.getId();
        newDoc.set(todo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Todo successfully written. id: " + todo.id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing Todo: ", e);
                    }
                });
    }

    TodoMessage flipDone(TodoMessage todoMessage){
        todoMessage.flipDone();
        collectionReference.document(todoMessage.id).set(todoMessage);
        if (todoMessage.isDone){
            Log.d(TAG, "Todo has been marked done. id: " + todoMessage.id);
        }else {
            Log.d(TAG, "Todo has been marked undone. id: " + todoMessage.id);
        }

        return todoMessage;
    }

    void deleteTodoForever(String id){
        // remove from db
        collectionReference
                .document(id)
                .delete();
        Log.d(TAG, "Todo has been deleted. id: " + id);
    }

    TodoMessage setTodoContent(TodoMessage todoMessage, String content){
        todoMessage.setContent(content);
        collectionReference
                .document(todoMessage.id)
                .set(todoMessage);
        Log.d(TAG, "Todo's content has been updated. id: " + todoMessage.id);
        return todoMessage;
    }

    TodoMessage updateEditTime(TodoMessage todoMessage){
        todoMessage.updateEditTimestamp();
        collectionReference.document(todoMessage.id).set(todoMessage);
        return todoMessage;
    }

    ArrayList<TodoMessage> getAllTodos(){
        return todoMessages;
    }

    TodoMessage getTodoFromId(String id){
        for (TodoMessage todo :
                todoMessages) {
            if (todo.id.equals(id)) {
                return todo;
            }
        }
        Log.d(TAG, "Unable to find TodoMessage with id: " + id);
        return null;

//        DocumentReference docRef = collectionReference.document(id);
//        DocumentSnapshot docSnap = docRef.get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                    } else {
//                        Log.d(TAG, "No such document");
//                    }
//                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
//                }
//            }
//        }).getResult();
//        if (docSnap.exists()){
//            return docSnap.toObject(TodoMessage.class);
//        }
//        Log.d(TAG, "Unable to retrieve message with id " + id);
//        return null;
    }
}
