package com.danielqueiroz.instagramclone.main.home.presentation;

import com.danielqueiroz.instagramclone.common.model.Feed;
import com.danielqueiroz.instagramclone.common.presenter.Presenter;
import com.danielqueiroz.instagramclone.main.home.datasource.HomeDataSource;
import com.danielqueiroz.instagramclone.main.presentation.MainView;

import java.util.List;

public class HomePresenter implements Presenter<List<Feed>> {

    private final HomeDataSource dataSource;
    private MainView.HomeView view;

    public HomePresenter(HomeDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setView(MainView.HomeView view) {
        this.view = view;
    }

    public void findFeed(){
        view.showProgressBar();
        dataSource.findFeed(this);
    }

    @Override
    public void onSuccess(List<Feed> response) {
        view.showFeed(response);
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onComplete() {
        view.hideProgressBar();
    }
}
