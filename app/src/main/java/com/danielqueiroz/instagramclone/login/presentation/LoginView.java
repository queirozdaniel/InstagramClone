package com.danielqueiroz.instagramclone.login.presentation;

public interface LoginView {

    void onFailureForm(String emailError, String passwordError);
    void onUserLoged();
}
