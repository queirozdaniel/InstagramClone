package com.danielqueiroz.instagramclone.login.presentation;

import android.os.Bundle;
import android.widget.EditText;

import com.danielqueiroz.instagramclone.R;
import com.danielqueiroz.instagramclone.common.model.Database;
import com.danielqueiroz.instagramclone.common.model.UserAuth;
import com.danielqueiroz.instagramclone.common.view.AbstractActivity;
import com.danielqueiroz.instagramclone.common.component.LoadingButton;
import com.danielqueiroz.instagramclone.login.datasource.LoginDataSource;
import com.danielqueiroz.instagramclone.login.datasource.LoginLocalDataSource;
import com.danielqueiroz.instagramclone.main.presentation.MainActivity;
import com.danielqueiroz.instagramclone.register.presentation.RegisterActivity;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class LoginActivity extends AbstractActivity implements LoginView {

    @BindView(R.id.login_edit_text_email)
    EditText editTextEmail;
    @BindView(R.id.login_edit_text_password)
    EditText editTextPassword;
    @BindView(R.id.login_edit_text_email_input)
    TextInputLayout inputLayoutEmail;
    @BindView(R.id.login_edit_text_password_input)
    TextInputLayout inputLayoutPassword;

    @BindView(R.id.login_button_enter)
    LoadingButton enterButton;

    LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarDark();

        UserAuth user = Database.getInstance().getUser();
        if (user != null){
            onUserLoged();
        }
    }

    @Override
    protected void onInject() {
        LoginDataSource dataSource = new LoginLocalDataSource();
        presenter = new LoginPresenter(this, dataSource);
    }

    @Override
    public void showProgressBar() {
        enterButton.showProgress(true);
    }

    @Override
    public void hideProgressBar() {
        enterButton.showProgress(false);
    }

    @Override
    public void onFailureForm(String emailError, String passwordError) {
        if (emailError != null) {
            inputLayoutEmail.setError(emailError);
            editTextEmail.setBackground(findDrawable(R.drawable.edit_text_background_error));
        }
        if (passwordError != null) {
            inputLayoutPassword.setError(passwordError);
            editTextPassword.setBackground(findDrawable(R.drawable.edit_text_background_error));
        }
    }

    @Override
    public void onUserLoged() {
        // TODO: implementar validação e mandar para Main
        MainActivity.launch(this, MainActivity.LOGIN_ACTIVITY);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @OnClick(R.id.login_button_enter)
    public void onButtonEnterClick() {
        presenter.login(editTextEmail.getText().toString(), editTextPassword.getText().toString());
    }

    @OnTextChanged({R.id.login_edit_text_email, R.id.login_edit_text_password})
    public void onTextChanged(CharSequence s) {
        enterButton.setEnabled(!editTextEmail.getText().toString().isEmpty() && !editTextPassword.getText().toString().isEmpty());

        if (s.hashCode() == editTextEmail.getText().hashCode()) {
            editTextEmail.setBackground(findDrawable(R.drawable.edit_text_background));
            inputLayoutEmail.setError(null);
            inputLayoutEmail.setErrorEnabled(false);
        } else if (s.hashCode() == editTextPassword.getText().hashCode()) {
            editTextPassword.setBackground(findDrawable(R.drawable.edit_text_background));
            inputLayoutPassword.setError(null);
            inputLayoutPassword.setErrorEnabled(false);
        }

    }

    @OnClick(R.id.login_text_view_register)
    public void onTextViewRegisterClick(){
        RegisterActivity.launch(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }
}