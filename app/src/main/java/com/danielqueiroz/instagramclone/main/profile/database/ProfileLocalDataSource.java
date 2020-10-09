package com.danielqueiroz.instagramclone.main.profile.database;

import com.danielqueiroz.instagramclone.common.model.Database;
import com.danielqueiroz.instagramclone.common.model.Post;
import com.danielqueiroz.instagramclone.common.model.User;
import com.danielqueiroz.instagramclone.common.model.UserProfile;
import com.danielqueiroz.instagramclone.common.presenter.Presenter;

import java.util.List;

public class ProfileLocalDataSource implements ProfileDatasource {

    @Override
    public void findUser(String uuid, Presenter<UserProfile> presenter) {
        Database db = Database.getInstance();
        db.findUser(uuid)
                .addOnSuccessListener((Database.OnSuccessListener<User>) usr1 -> {
                    db.findPosts(uuid)
                            .addOnSuccessListener((Database.OnSuccessListener<List<Post>>) post -> {
                                db.following(db.getUser().getUUID(), uuid)
                                        .addOnSuccessListener((Database.OnSuccessListener<Boolean>) following -> {
                                            presenter.onSuccess(new UserProfile(usr1, post, following));
                                            presenter.onComplete();
                                        });
                            });
                });
    }

    @Override
    public void follow(String user) {
        Database.getInstance().follow(Database.getInstance().getUser().getUUID(), user);
    }

    @Override
    public void unfollow(String user) {
        Database.getInstance().unfollow(Database.getInstance().getUser().getUUID(), user);
    }

}
