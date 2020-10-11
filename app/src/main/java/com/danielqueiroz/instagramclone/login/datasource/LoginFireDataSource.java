package com.danielqueiroz.instagramclone.login.datasource;

import com.danielqueiroz.instagramclone.common.presenter.Presenter;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFireDataSource implements LoginDataSource {

    @Override
    public void login(String email, String password, Presenter presenter) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    presenter.onSuccess(authResult.getUser());
                }).addOnFailureListener( e -> presenter.onError(e.getMessage()))
                .addOnCompleteListener(task -> presenter.onComplete());
    }
}
