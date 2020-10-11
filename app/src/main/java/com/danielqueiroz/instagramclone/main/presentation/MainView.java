package com.danielqueiroz.instagramclone.main.presentation;

import android.net.Uri;

import com.danielqueiroz.instagramclone.common.model.Feed;
import com.danielqueiroz.instagramclone.common.model.Post;
import com.danielqueiroz.instagramclone.common.model.User;
import com.danielqueiroz.instagramclone.common.view.View;

import java.util.List;

public interface MainView extends View {

    void scrollToolbarEnebled(boolean enabled);

    void showProfile(String user);

    void disposeProfileDetail();

    public interface ProfileView extends View {

        void showPhoto(String photo);
        void showData(String name, String following, String followers, String posts, boolean editProfile, boolean follow);
        void showPosts(List<Post> posts);

    }

    public interface HomeView extends View{

        void showFeed(List<Feed> feed);

    }

    public interface SearchView {
        void showUsers(List<User> users);
    }

}
