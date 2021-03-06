package com.danielqueiroz.instagramclone.main.profile.presentation;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.danielqueiroz.instagramclone.R;
import com.danielqueiroz.instagramclone.common.model.Database;
import com.danielqueiroz.instagramclone.common.model.Post;
import com.danielqueiroz.instagramclone.common.view.AbstractFragment;
import com.danielqueiroz.instagramclone.main.presentation.MainView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends AbstractFragment<ProfilePresenter>  implements  MainView.ProfileView{

    private MainView mainView;
    private PostAdapter postAdapter;

    @BindView(R.id.profile_recycler)
    RecyclerView recyclerView;

    @BindView(R.id.profile_image_icon)
    CircleImageView imageViewProfile;

    @BindView(R.id.profile_text_view_user_name)
    TextView txtUsername;

    @BindView(R.id.profile_text_view_following_count)
    TextView txtFollowingCount;

    @BindView(R.id.profile_text_view_followers_count)
    TextView txtFollowersCount;

    @BindView(R.id.profile_text_view_post_count)
    TextView txtPostCount;

    @BindView(R.id.profile_navigation_tabs)
    BottomNavigationView bottomNavigationView;

    @BindView(R.id.profile_button_edit_profile)
    Button button;

    public static ProfileFragment newInstance(MainView mainView, ProfilePresenter presenter) {
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setMainView(mainView);
        profileFragment.setPresenter(presenter);
        presenter.setView(profileFragment);
        return profileFragment;
    }

    public ProfileFragment() { }

    private void setMainView(MainView mainView){
        this.mainView = mainView;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO: app:layout_scrollFlags="scroll" na toolbar
        View view = super.onCreateView(inflater, container, savedInstanceState);

        postAdapter = new PostAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(postAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.menu_profile_grid:
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                    return true;
                case R.id.menu_profile_list:
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    return true;
            }
            return false;
        });
    }

    @Override
    public void showProgressBar() {
        mainView.showProgressBar();
    }

    @Override
    public void hideProgressBar() {
        mainView.hideProgressBar();
    }

    @Override
    public void showPhoto(String photo) {
        Glide.with(getContext()).load(photo).into(imageViewProfile);
    }

    @Override
    public void showData(String name, String following, String followers, String posts, boolean editProfile, boolean follow) {
        txtUsername.setText(name);
        txtFollowingCount.setText(following);
        txtFollowersCount.setText(followers);
        txtPostCount.setText(posts);

        if (editProfile){
            button.setText(getString(R.string.edit_profile));
            button.setTag(null);
        } else if (follow){
            button.setText(getString(R.string.unfollow));
            button.setTag(false);
        } else {
            button.setText(getString(R.string.follow));
            button.setTag(true);
        }
    }

    @OnClick(R.id.profile_button_edit_profile)
    public void onButtonFollowClick(){
        Boolean follow = (Boolean) button.getTag();

        if (follow != null){
            button.setText(follow ? R.string.unfollow : R.string.follow);
            presenter.follow(follow);
            button.setTag(!follow);
        }
    }

    @Override
    public void showPosts(List<Post> posts) {
        postAdapter.setPosts(posts);
        postAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (!presenter.getUser().equals(FirebaseAuth.getInstance().getUid()))
                    mainView.disposeProfileDetail();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_main_profile;
    }

    private class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {

        private List<Post> posts = new ArrayList<>();

        @NonNull
        @Override
        public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PostViewHolder(getLayoutInflater().inflate(R.layout.item_profile_grid, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
            holder.bind(posts.get(position));
        }

        public void setPosts(List<Post> posts) {
            this.posts = posts;
        }

        @Override
        public int getItemCount() {
            return posts.size();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.finUser();
    }

    private class PostViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imagePost;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePost = itemView.findViewById(R.id.profile_image_grid);
        }

        public void bind(Post post){
            Glide.with(itemView.getContext()).load(post.getPhotoUrl()).into(imagePost);
        }
    }

}
