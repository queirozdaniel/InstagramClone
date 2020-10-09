package com.danielqueiroz.instagramclone.main.search.presentation;

import com.danielqueiroz.instagramclone.common.model.User;
import com.danielqueiroz.instagramclone.common.presenter.Presenter;
import com.danielqueiroz.instagramclone.main.presentation.MainView;
import com.danielqueiroz.instagramclone.main.search.datasource.SearchDataSource;

import java.util.List;

public class SearchPresenter implements Presenter<List<User>> {

    private final  SearchDataSource dataSource;
    private MainView.SearchView view;

    public SearchPresenter(SearchDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setView(MainView.SearchView view) {
        this.view = view;
    }

    public void findUsers(String newText) {
        dataSource.findUsers(newText, this);
    }

    @Override
    public void onSuccess(List<User> response) {
        view.showUsers(response);
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onComplete() {

    }

}
