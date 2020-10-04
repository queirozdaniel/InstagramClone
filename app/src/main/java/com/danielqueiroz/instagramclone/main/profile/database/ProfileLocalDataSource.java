package com.danielqueiroz.instagramclone.main.profile.database;

import com.danielqueiroz.instagramclone.common.model.Database;
import com.danielqueiroz.instagramclone.common.model.Post;
import com.danielqueiroz.instagramclone.common.model.User;
import com.danielqueiroz.instagramclone.common.model.UserProfile;
import com.danielqueiroz.instagramclone.common.presenter.Presenter;
import com.danielqueiroz.instagramclone.main.profile.presentation.ProfilePresenter;

import java.util.List;

public class ProfileLocalDataSource implements ProfileDatasource {

    @Override
    public void findUser(Presenter<UserProfile> presenter) {
        Database db = Database.getInstance();
        db.findUser(db.getUser().getUUID())
                .addOnSuccessListener((Database.OnSuccessListener<User>) user -> {
                    db.findPosts(user.getUuid())
                            .addOnSuccessListener((Database.OnSuccessListener<List<Post>>) post -> {
                                presenter.onSuccess(new UserProfile(user, post));
                                presenter.onComplete();
                            });
                });
    }

}
