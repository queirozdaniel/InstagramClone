package com.danielqueiroz.instagramclone.login.datasource;

import com.danielqueiroz.instagramclone.common.model.Database;
import com.danielqueiroz.instagramclone.common.model.UserAuth;
import com.danielqueiroz.instagramclone.common.presenter.Presenter;

public class LoginLocalDataSource implements LoginDataSource{

    @Override
    public void login(String email, String password, Presenter presenter) {
        Database.getInstance().login(email, password)
                .addOnSuccessListener(new Database.OnSuccessListener<UserAuth>() {
                    @Override
                    public void onSuccess(UserAuth response) {
                        presenter.onSuccess(response);
                    }
                }).addOnFailuerListener(e -> presenter.onError(e.getMessage()))
                .addCompleteListener(() -> presenter.onComplete());
    }
}
