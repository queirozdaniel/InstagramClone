package com.danielqueiroz.instagramclone.main.search.datasource;

import com.danielqueiroz.instagramclone.common.model.User;
import com.danielqueiroz.instagramclone.common.presenter.Presenter;

import java.util.List;

public interface SearchDataSource {

    void findUsers(String query, Presenter<List<User>> presenter);

}
