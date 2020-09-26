package com.danielqueiroz.instagramclone.login.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.danielqueiroz.instagramclone.R;
import com.danielqueiroz.instagramclone.common.view.AbstractActivity;
import com.danielqueiroz.instagramclone.common.view.LoadingButton;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AbstractActivity implements LoginView, TextWatcher {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));
        }

        editTextEmail.addTextChangedListener(this);
        editTextPassword.addTextChangedListener(this);

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
    }

    @OnClick(R.id.login_button_enter)
    public void onButtonEnterClick(){
        enterButton.showProgress(true);

        new Handler().postDelayed(() -> {
            enterButton.showProgress(false);
        }, 3000);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!s.toString().isEmpty()) {
            enterButton.setEnabled(true);
        } else {
            enterButton.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }
}