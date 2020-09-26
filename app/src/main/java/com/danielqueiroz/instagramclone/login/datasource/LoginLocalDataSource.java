package com.danielqueiroz.instagramclone.login.datasource;

import com.danielqueiroz.instagramclone.common.presenter.Presenter;

public class LoginLocalDataSource implements LoginDataSource{

    @Override
    public void login(String email, String senha, Presenter presenter) {
        presenter.onError("Error1");
        presenter.onComplete();
    }
}
