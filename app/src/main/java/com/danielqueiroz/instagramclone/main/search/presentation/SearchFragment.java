package com.danielqueiroz.instagramclone.main.search.presentation;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.danielqueiroz.instagramclone.R;
import com.danielqueiroz.instagramclone.common.model.User;
import com.danielqueiroz.instagramclone.common.presenter.Presenter;
import com.danielqueiroz.instagramclone.common.view.AbstractFragment;
import com.danielqueiroz.instagramclone.main.presentation.MainView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchFragment extends AbstractFragment<SearchPresenter> implements MainView.SearchView {

    @BindView(R.id.search_recycler)
    RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private MainView mainView;

    public static SearchFragment newInstance(MainView view, SearchPresenter presenter){
        SearchFragment fragment = new SearchFragment();
        fragment.setMainView(view);
        presenter.setView(fragment);
        fragment.setPresenter(presenter);
        return fragment;
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    public SearchFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO: app:layout_scrollFlags="scroll" na toolbar
        View view = super.onCreateView(inflater, container, savedInstanceState);

        userAdapter = new UserAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(userAdapter);

        return view;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_main_search;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }

        if (searchView != null) {
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(((AppCompatActivity)getContext()).getComponentName())
            );
            searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> Log.i("Teste Search", hasFocus + ""));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (!newText.isEmpty()){
                        presenter.findUsers(newText);
                    }
                    return false;
                }
            });
            searchItem.expandActionView();
        }

    }

    @Override
    public void showUsers(List<User> users) {
        userAdapter.setUser(users, user -> mainView.showProfile(user.getUuid()));
        userAdapter.notifyDataSetChanged();
    }

    private class UserAdapter extends RecyclerView.Adapter<PostViewHolder> {

        private List<User> user = new ArrayList<>();
        private ItemClickListener listener;

        @NonNull
        @Override
        public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_user_list, parent, false);
            return new PostViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
            holder.bind(user.get(position));
            holder.itemView.setOnClickListener(v -> {
                listener.onClick(user.get(position));
            });
        }

        @Override
        public int getItemCount() {
            return user.size();
        }

        public void setUser(List<User> user, ItemClickListener listener) {
            this.user = user;
            this.listener = listener;
        }
    }

    private class PostViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imagePost;
        private final TextView textViewUsername;
        private final TextView textViewName;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePost = itemView.findViewById(R.id.main_search_image_view_user);
            textViewUsername = itemView.findViewById(R.id.main_search_text_view_username);
            textViewName = itemView.findViewById(R.id.main_search_text_view_name);
        }

        public void bind(User user){
            this.textViewName.setText(user.getName());
            this.textViewUsername.setText(user.getName());
            this.imagePost.setImageURI(user.getUri());
        }
    }

    interface ItemClickListener {
        void onClick(User user);
    }

}
