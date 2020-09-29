package com.danielqueiroz.instagramclone.register.presentation;

import android.net.Uri;

import com.danielqueiroz.instagramclone.R;
import com.danielqueiroz.instagramclone.common.model.User;
import com.danielqueiroz.instagramclone.common.model.UserAuth;
import com.danielqueiroz.instagramclone.common.presenter.Presenter;
import com.danielqueiroz.instagramclone.common.util.Strings;
import com.danielqueiroz.instagramclone.register.datasource.RegisterDataSource;

public class RegisterPresenter implements Presenter<UserAuth> {

    private RegisterView registerView;
    private RegisterView.EmailView emailView;
    private RegisterView.NamePasswordView namePasswordView;
    private RegisterView.PhotoView photoView;

    private final RegisterDataSource dataSource;

    private String email;
    private String name;
    private Uri uri;

    public RegisterPresenter(RegisterDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setRegisterView(RegisterView registerView){
        this.registerView = registerView;
    }

    public void setEmailView(RegisterView.EmailView emailView) {
        this.emailView = emailView;
    }

    public void setNamePasswordView(RegisterView.NamePasswordView namePasswordView){
        this.namePasswordView = namePasswordView;
    }

    public void setPhotoView(RegisterView.PhotoView photoView){
        this.photoView = photoView;
    }

    public void setEmail(String email){
        if (!Strings.emailValid(email)){
            emailView.onFailureForm(emailView.getContext().getString(R.string.invalid_email));
            return;
        }
        this.email = email;
        registerView.showNextView(RegisterSteps.NAME_PASSWORD);
    }

    public void setNameAndPassword(String name, String password, String confirmPassword){
        if (!password.equals(confirmPassword)){
            namePasswordView.onFailureForm(null, namePasswordView.getContext().getString(R.string.password_not_equal));
            return;
        }
        this.name = name;

        namePasswordView.showProgressBar();
        dataSource.createUser(name, email, password, this);
    }

    public String getName() {
        return name;
    }

    public void setUri(Uri uri){
        this.uri = uri;
        this.photoView.onImageCroped(uri);
    }

    public void showCamera(){
        registerView.showCamera();
    }

    public void showGallery(){
        registerView.showGallery();
    }

    public void showPhotoView() {
          registerView.showNextView(RegisterSteps.PHOTO);
    }

    public void jumpRegistration(){
        registerView.onUserCreated();
    }

    @Override
    public void onSuccess(UserAuth response) {
        registerView.showNextView(RegisterSteps.WELCOME);
    }

    @Override
    public void onError(String message) {
        namePasswordView.onFailureCreateUser(message);
    }

    @Override
    public void onComplete() {
        namePasswordView.hideProgressBar();
    }

}
