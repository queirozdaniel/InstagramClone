package com.danielqueiroz.instagramclone.login.presentation;

import com.danielqueiroz.instagramclone.common.view.View;

public interface LoginView extends View {

    void onFailureForm(String emailError, String passwordError);
    void onUserLoged();
}
