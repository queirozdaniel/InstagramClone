package com.danielqueiroz.instagramclone.register.datasource;

import com.danielqueiroz.instagramclone.common.model.Database;
import com.danielqueiroz.instagramclone.common.model.UserAuth;
import com.danielqueiroz.instagramclone.common.presenter.Presenter;

public class RegisterLocalDataSource implements RegisterDataSource {
    @Override
    public void createUser(String name, String email, String password, Presenter presenter) {
        Database.getInstance().createUser(name, email, password)
                .addOnSuccessListener((Database.OnSuccessListener<UserAuth>) presenter::onSuccess)
                .addOnFailuerListener( e -> presenter.onError(e.getMessage()))
                .addCompleteListener(presenter::onComplete);
    }
}
