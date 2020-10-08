package com.danielqueiroz.instagramclone.common.model;

import android.net.Uri;
import android.os.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Database {

    private static Set<UserAuth> usersAuth;
    private static Set<User> users;
    private static Set<Uri> storages;
    private static HashMap<String, HashSet<Post>> posts;
    private static HashMap<String, HashSet<Feed>> feed;
    private static HashMap<String, HashSet<String>> followers;

    private static Database INSTANCE;
    private static  UserAuth userAuth;

    static {
        usersAuth = new HashSet<>();
        users = new HashSet<>();
        storages = new HashSet<>();
        posts =  new HashMap<>();
        feed =  new HashMap<>();
        followers =  new HashMap<>();

        //init();

        //usersAuth.add(new UserAuth("daniel@gmail.com", "1234"));
        //usersAuth.add(new UserAuth("user@gmail.com", "12345"));
        //usersAuth.add(new UserAuth("userx@gmail.com", "123"));
    }

    private OnSuccessListener onSuccessListener;
    private OnFailureListener onFailureListener;
    private OnCompleteListener onCompleteListener;

    public static Database getInstance() {
        return new Database();
        /*if (INSTANCE == null){
            INSTANCE = new Database();
            INSTANCE.init();
        }

        return INSTANCE; */
    }

    public static  void init() {
        String email =  "user1@gmail.com";
        String password = "123";
        String name = "user1";

        UserAuth userAuth = new UserAuth();
        userAuth.setEmail(email);
        userAuth.setPassword(password);

        usersAuth.add(userAuth);

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setUuid(userAuth.getUUID());

        users.add(user);

        Database.userAuth = userAuth;
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

    public Database findFeed(String uuid){
        timeout(() ->{
            HashMap<String, HashSet<Feed>> feed = Database.feed;
            HashSet<Feed> res = feed.get(uuid);

            if (res == null) {
                res = new HashSet<>();
            }

            if (onSuccessListener != null){
                onSuccessListener.onSuccess(new ArrayList<>(res));
            }

            if (onCompleteListener != null)
                onCompleteListener.onComplete();

        });

        return  this;
    }

    public Database findPosts(String uuid){
        timeout(() -> {
            HashMap<String, HashSet<Post>> posts = Database.posts;
            HashSet<Post> res = posts.get(uuid);

            if (res == null) {
                res = new HashSet<>();
            }

            if (onSuccessListener != null){
                onSuccessListener.onSuccess(new ArrayList<>(res));
            }

            if (onCompleteListener != null)
                onCompleteListener.onComplete();
        });

        return this;
    }

    public Database findUser(String uuid){
        timeout(()-> {
            Set<User> users = Database.users;
            User res = null;

            for (User user : users){
                if (user.getUuid().equals(uuid)){
                    res = user;
                    break;
                }
            }

            if (onSuccessListener != null && res != null){
                onSuccessListener.onSuccess(res);
            } else if (onFailureListener != null) {
                onFailureListener.onFailure(new IllegalArgumentException("Usuário não encontrado"));
            }

            if (onCompleteListener != null)
                onCompleteListener.onComplete();

        });

        return  this;
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

    public Database createPost(String uuid, Uri uri, String caption){
        timeout(() ->{
            HashMap<String, HashSet<Post>> postsMap = Database.posts;
            HashSet<Post> posts = postsMap.get(uuid);

            if (posts == null) {
                posts = new HashSet<>();
                postsMap.put(uuid,posts);
            }

            Post post = new Post();
            post.setUri(uri);
            post.setCaption(caption);
            post.setTimestamp(System.currentTimeMillis());
            post.setUuid(String.valueOf(post.hashCode()));
            posts.add(post);

            HashMap<String, HashSet<String>> followersMap = Database.followers;
            HashSet<String> followers = followersMap.get(uuid);

            if (followers == null) {
                followers = new HashSet<>();
                followersMap.put(uuid, followers);
            } else {
                HashMap<String, HashSet<Feed>> feedMap = Database.feed;

                for (String follower : followers){
                    HashSet<Feed> feeds = feedMap.get(follower);

                    if (feeds != null){
                        Feed feed = new Feed();
                        feed.setUri(post.getUri());
                        feed.setCaption(post.getCaption());
                        // feed.setPlubsher();
                        feed.setTimestamp(post.getTimestamp());

                        feeds.add(feed);
                    }
                }
                HashSet<Feed> feedMe = feedMap.get(uuid);
                if (feedMe != null) {
                    Feed feed = new Feed();
                    feed.setUri(post.getUri());
                    feed.setCaption(post.getCaption());
                    // feed.setPlubsher();
                    feed.setTimestamp(post.getTimestamp());

                    feedMe.add(feed);
                }
            }

            if (onSuccessListener != null)
                onSuccessListener.onSuccess(null);

            if (onCompleteListener != null)
                onCompleteListener.onComplete();

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
                Database.userAuth = userAuth;

                HashMap<String, HashSet<String>> followersMap = Database.followers;
                followersMap.put(userAuth.getUUID(), new HashSet<>());

                HashMap<String, HashSet<Feed>> feedMap = Database.feed;
                feedMap.put(userAuth.getUUID(), new HashSet<>());


                if (onSuccessListener != null)
                    onSuccessListener.onSuccess(userAuth);
            } else {
                Database.userAuth = null;
                if (onFailureListener != null)
                    onFailureListener.onFailure(new IllegalArgumentException("Usuário já existe"));
            }
            if (onCompleteListener != null)
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
                Database.userAuth = user;
                onSuccessListener.onSuccess(userAuth);
            } else {
                Database.userAuth = null;
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
