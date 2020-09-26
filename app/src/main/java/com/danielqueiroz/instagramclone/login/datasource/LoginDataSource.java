package com.danielqueiroz.instagramclone.login.datasource;

import com.danielqueiroz.instagramclone.common.presenter.Presenter;

public interface LoginDataSource {

    void login(String email, String password, Presenter presenter);
}
