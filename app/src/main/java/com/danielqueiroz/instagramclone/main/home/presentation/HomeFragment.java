package com.danielqueiroz.instagramclone.main.home.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.danielqueiroz.instagramclone.R;
import com.danielqueiroz.instagramclone.common.model.Feed;
import com.danielqueiroz.instagramclone.common.view.AbstractFragment;
import com.danielqueiroz.instagramclone.main.presentation.MainView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeFragment extends AbstractFragment<HomePresenter> implements MainView.HomeView{

    private MainView mainView;
    private FeedAdapter feedAdapter;

    @BindView(R.id.home_recycler)
    RecyclerView recyclerView;

    public static HomeFragment newInstance(MainView mainView, HomePresenter presenter) {
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setMainView(mainView);
        homeFragment.setPresenter(presenter);
        presenter.setView(homeFragment);
        return homeFragment;
    }

    public HomeFragment() { }

    private void setMainView(MainView mainView){
        this.mainView = mainView;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        feedAdapter = new FeedAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(feedAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
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
    public void showFeed(List<Feed> feed) {
        feedAdapter.setFeed(feed);
        feedAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_main_home;
    }




    private class FeedAdapter extends RecyclerView.Adapter<PostViewHolder> {

        private List<Feed> feed = new ArrayList<>();

        @NonNull
        @Override
        public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PostViewHolder(getLayoutInflater().inflate(R.layout.item_profile_list, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
            holder.bind(feed.get(position));
        }

        @Override
        public int getItemCount() {
            return feed.size();
        }

        public void setFeed(List<Feed> feed) {
            this.feed = feed;
        }

    }

    private class PostViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imagePost;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePost = itemView.findViewById(R.id.profile_image_grid);
        }

        public void bind(Feed feed){
            this.imagePost.setImageURI(feed.getUri());
        }
    }

}
