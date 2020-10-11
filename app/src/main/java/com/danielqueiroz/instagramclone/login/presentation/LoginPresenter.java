package com.danielqueiroz.instagramclone.login.presentation;

import android.os.Handler;

import androidx.annotation.StringRes;

import com.danielqueiroz.instagramclone.R;
import com.danielqueiroz.instagramclone.common.model.UserAuth;
import com.danielqueiroz.instagramclone.common.presenter.Presenter;
import com.danielqueiroz.instagramclone.common.util.Strings;
import com.danielqueiroz.instagramclone.login.datasource.LoginDataSource;
import com.google.firebase.auth.FirebaseUser;

public class LoginPresenter implements Presenter<FirebaseUser> {
    private final LoginView view;
    private final LoginDataSource dataSource;

    public LoginPresenter(LoginView view, LoginDataSource dataSource) {
        this.view = view;
        this.dataSource = dataSource;
    }

    public void login(String email, String password){
        if (!Strings.emailValid(email)){
            view.onFailureForm(view.getContext().getString(R.string.invalid_email), null);
            return;
        }

        view.showProgressBar();
        dataSource.login(email, password, this);
    }

    @Override
    public void onSuccess(FirebaseUser userAuth) {
        view.onUserLoged();
    }

    @Override
    public void onError(String message) {
        view.onFailureForm("Error", message);
    }

    @Override
    public void onComplete() {
        view.hideProgressBar();
    }
}
