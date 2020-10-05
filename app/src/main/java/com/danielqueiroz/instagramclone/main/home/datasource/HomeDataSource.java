package com.danielqueiroz.instagramclone.main.home.datasource;

import com.danielqueiroz.instagramclone.common.model.Feed;
import com.danielqueiroz.instagramclone.common.presenter.Presenter;

import java.util.List;

public interface HomeDataSource {

    void findFeed(Presenter<List<Feed>> presenter);
}
