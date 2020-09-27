package com.danielqueiroz.instagramclone.common.model;

import android.os.Handler;

import java.util.HashSet;
import java.util.Set;

public class Database {

    private static Set<UserAuth> usersAuth;
    private static Database INSTANCE;
    private UserAuth userAuth;

    static {
        usersAuth = new HashSet<>();
        //usersAuth.add(new UserAuth("daniel@gmail.com", "1234"));
        //usersAuth.add(new UserAuth("user@gmail.com", "12345"));
        //usersAuth.add(new UserAuth("userx@gmail.com", "123"));
    }

    private OnSuccessListener onSuccessListener;
    private OnFailureListener onFailureListener;
    private OnCompleteListener onCompleteListener;

    public static Database getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Database();

        return INSTANCE;
    }

    public <T> Database addOnSuccessListener(OnSuccessListener<T> listener) {
        this.onSuccessListener = listener;
        return this;
    }

    public Database addOnFailuerListener(OnFailureListener listener) {
        this.onFailureListener = listener;
        return this;
    }

    public Database addCompleteListener(OnCompleteListener listener) {
        this.onCompleteListener = listener;
        return this;
    }

    public Database login(String email, String password) {
        timeout(() -> {
            UserAuth user = new UserAuth();
            user.setEmail(email);
            user.setPassword(password);

            if (usersAuth.contains(user)) {
                this.userAuth = user;
                onSuccessListener.onSuccess(userAuth);
            } else {
                this.userAuth = null;
                onFailureListener.onFailure(new IllegalArgumentException("Usuário não encontrado"));
            }
            onCompleteListener.onComplete();
        });
        return this;
    }

    private void timeout(Runnable r) {
        new Handler().postDelayed(r, 2000);
    }

    public interface OnSuccessListener<T> {
        void onSuccess(T response);
    }

    public interface OnFailureListener {
        void onFailure(Exception e);
    }

    public interface OnCompleteListener {
        void onComplete();
    }

}
