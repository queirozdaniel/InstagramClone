package com.danielqueiroz.instagramclone.register.datasource;

import com.danielqueiroz.instagramclone.common.presenter.Presenter;

public interface RegisterDataSource {

    void createUser(String name, String email, String password, Presenter presenter);

}
