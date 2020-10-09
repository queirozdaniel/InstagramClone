package com.danielqueiroz.instagramclone.main.profile.presentation;

import com.danielqueiroz.instagramclone.common.model.Database;
import com.danielqueiroz.instagramclone.common.model.Post;
import com.danielqueiroz.instagramclone.common.model.User;
import com.danielqueiroz.instagramclone.common.model.UserProfile;
import com.danielqueiroz.instagramclone.common.presenter.Presenter;
import com.danielqueiroz.instagramclone.main.presentation.MainView;
import com.danielqueiroz.instagramclone.main.profile.database.ProfileDatasource;

import java.util.List;

public class ProfilePresenter implements Presenter<UserProfile> {
    private final ProfileDatasource datasource;
    private final String user;
    private MainView.ProfileView view;

    public ProfilePresenter(ProfileDatasource datasource) {
        this(datasource, Database.getInstance().getUser().getUUID());
    }

    public ProfilePresenter(ProfileDatasource datasource, String user) {
        this.datasource = datasource;
        this.user = user;
    }

    public void setView(MainView.ProfileView view) {
        this.view = view;
    }

    public void finUser() {
        view.showProgressBar();
        datasource.findUser(user, this);
    }

    public void follow(boolean follow){
        if (follow)
            datasource.follow(user);
        else
            datasource.unfollow(user);
    }

    public String getUser() {
        return user;
    }

    @Override
    public void onSuccess(UserProfile response) {
        User user = response.getUser();
        List<Post> posts = response.getPosts();

        boolean editProfile = user.getUuid().equals(Database.getInstance().getUser().getUUID());

        view.showData(user.getName(),
                String.valueOf(user.getFollowing()),
                String.valueOf(user.getFollowers()),
                String.valueOf(user.getPosts()),
                editProfile,
                response.isFollowing()
                );

        view.showPosts(posts);

        if (user.getUri() != null){
            view.showPhoto(user.getUri());
        }
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onComplete() {
        view.hideProgressBar();
    }

}
