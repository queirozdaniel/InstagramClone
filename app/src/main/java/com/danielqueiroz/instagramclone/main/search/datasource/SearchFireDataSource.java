package com.danielqueiroz.instagramclone.main.search.datasource;

import com.danielqueiroz.instagramclone.common.model.User;
import com.danielqueiroz.instagramclone.common.presenter.Presenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class SearchFireDataSource implements SearchDataSource{
    @Override
    public void findUsers(String query, Presenter<List<User>> presenter) {
        FirebaseFirestore.getInstance()
                .collection("user")
                .whereEqualTo("name", query)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<User> users = new ArrayList<>();
                    List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot doc : documents ) {
                        User user = doc.toObject(User.class);
                        if (!user.getUuid().equals(FirebaseAuth.getInstance().getUid())){
                            users.add(user);
                        }
                    }
                    presenter.onSuccess(users);
                })
                .addOnFailureListener(e -> presenter.onError(e.getMessage()))
                .addOnCompleteListener(task -> presenter.onComplete());
    }
}
