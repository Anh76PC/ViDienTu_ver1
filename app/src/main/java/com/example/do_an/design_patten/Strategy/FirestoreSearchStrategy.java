package com.example.do_an.design_patten.Strategy;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreSearchStrategy implements SearchStrategy {
    private final FirebaseFirestore firestore;

    public FirestoreSearchStrategy(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public void searchUser(String phoneNumber, OnSearchResultListener listener) {
        firestore.collection("Users").document(phoneNumber).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            listener.onUserFound(phoneNumber);
                        } else {
                            listener.onUserNotFound();
                        }
                    } else {
                        listener.onError();
                    }
                });
    }
}

