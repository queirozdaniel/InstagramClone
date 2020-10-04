package com.danielqueiroz.instagramclone.common.model;

import android.net.Uri;
import android.os.Handler;

import java.util.HashSet;
import java.util.Set;

public class Database {

    private static Set<UserAuth> usersAuth;
    private static Set<User> users;
    private static Set<Uri> storages;
    private static Database INSTANCE;
    private UserAuth userAuth;

    static {
        usersAuth = new HashSet<>();
        users = new HashSet<>();
        storages = new HashSet<>();

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

    public Database addPhoto(String uuid, Uri uri){
        timeout(() -> {
            Set<User> users = Database.users;
            for (User user : users) {
                if (user.getUuid().equals(uuid)){
                    user.setUri(uri);
                }
            }
            storages.add(uri);
            onSuccessListener.onSuccess(true);
        });

        return this;
    }

    public Database createUser(String name, String email, String password) {
        timeout(() -> {
            UserAuth userAuth = new UserAuth();
            userAuth.setEmail(email);
            userAuth.setPassword(password);

            usersAuth.add(userAuth);

            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setUuid(userAuth.getUUID());

            boolean added = users.add(user);
            if (added){
                this.userAuth = userAuth;
                onSuccessListener.onSuccess(userAuth);
            } else {
                this.userAuth = null;
                onFailureListener.onFailure(new IllegalArgumentException("Usuário já existe"));
            }
            onCompleteListener.onComplete();
        });
        return this;
    }

    public UserAuth getUser(){
        return userAuth;
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
