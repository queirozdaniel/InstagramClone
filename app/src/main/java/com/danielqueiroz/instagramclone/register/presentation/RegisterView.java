package com.danielqueiroz.instagramclone.register.presentation;

import android.content.Context;

import com.danielqueiroz.instagramclone.common.view.View;

public interface RegisterView {

    void showNextView(RegisterSteps steps);

    void onUserCreated();

    interface EmailView {

        Context getContext();
        void onFailureForm(String emailError);

    }

    interface NamePasswordView extends View {

        Context getContext();
        void onFailureForm(String nameError, String passwordError);
        void onFailureCreateUser(String message);

    }

    interface WelcomeView {}

    interface PhotoView {}

}
