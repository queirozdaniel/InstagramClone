package com.danielqueiroz.instagramclone.main.presentation;

import android.net.Uri;

import com.danielqueiroz.instagramclone.common.model.Feed;
import com.danielqueiroz.instagramclone.common.model.Post;
import com.danielqueiroz.instagramclone.common.view.View;

import java.util.List;

public interface MainView extends View {

    void scrollToolbarEnebled(boolean enabled);

    public interface ProfileView extends View {

        void showPhoto(Uri photo);
        void showData(String name, String following, String followers, String posts);
        void showPosts(List<Post> posts);

    }

    public interface HomeView extends View{

        void showFeed(List<Feed> feed);

    }

}
