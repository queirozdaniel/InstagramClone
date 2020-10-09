package com.danielqueiroz.instagramclone.main.profile.database;

import com.danielqueiroz.instagramclone.common.model.Database;
import com.danielqueiroz.instagramclone.common.model.Post;
import com.danielqueiroz.instagramclone.common.model.User;
import com.danielqueiroz.instagramclone.common.model.UserProfile;
import com.danielqueiroz.instagramclone.common.presenter.Presenter;

import java.util.List;

public class ProfileLocalDataSource implements ProfileDatasource {

    @Override
    public void findUser(String user, Presenter<UserProfile> presenter) {
        Database db = Database.getInstance();
        db.findUser(user)
                .addOnSuccessListener((Database.OnSuccessListener<User>) usr1 -> {
                    db.findPosts(usr1.getUuid())
                            .addOnSuccessListener((Database.OnSuccessListener<List<Post>>) post -> {
                                presenter.onSuccess(new UserProfile(usr1, post));
                                presenter.onComplete();
                            });
                });
    }

}
