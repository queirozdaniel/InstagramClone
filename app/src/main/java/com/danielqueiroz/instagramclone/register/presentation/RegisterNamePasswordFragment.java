package com.danielqueiroz.instagramclone.register.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.danielqueiroz.instagramclone.R;
import com.danielqueiroz.instagramclone.common.view.AbstractFragment;
import com.danielqueiroz.instagramclone.common.view.LoadingButton;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class RegisterNamePasswordFragment extends AbstractFragment<RegisterPresenter> implements RegisterView.NamePasswordView {

    @BindView(R.id.register_edit_text_name_input)
    TextInputLayout inputLayoutName;

    @BindView(R.id.register_edit_text_name)
    EditText editTextName;

    @BindView(R.id.register_edit_text_name_password_input)
    TextInputLayout inputLayoutPassword;

    @BindView(R.id.register_edit_text_name_password)
    EditText editTextPassword;

    @BindView(R.id.register_edit_text_name_password_confirm_input)
    TextInputLayout inputLayoutConfirm;

    @BindView(R.id.register_edit_text_name_password_confirm)
    EditText editTextConfirm;

    @BindView(R.id.register_name_button_next)
    LoadingButton buttonNext;

    public RegisterNamePasswordFragment() {
    }

    public static RegisterNamePasswordFragment newInstance(RegisterPresenter presenter) {
        RegisterNamePasswordFragment fragment = new RegisterNamePasswordFragment();
        fragment.setPresenter(presenter);
        presenter.setNamePasswordView(fragment);
        return fragment;
    }

    @Override
    public void showProgressBar() {
        buttonNext.showProgress(true);
    }

    @Override
    public void hideProgressBar() {
        buttonNext.showProgress(false);
    }

    @Override
    public void onFailureForm(String nameError, String passwordError) {
        if (nameError != null){
            inputLayoutName.setError(nameError);
            editTextName.setBackground(findDrawable(R.drawable.edit_text_background_error));
        }

        if (passwordError != null){
            inputLayoutPassword.setError(passwordError);
            editTextPassword.setBackground(findDrawable(R.drawable.edit_text_background_error));
        }

    }

    @Override
    public void onFailureCreateUser(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.register_text_view_login)
    public void onTextViewLoginClick(){
        if (isAdded() && getActivity() != null)
            getActivity().finish();
    }

    @OnClick(R.id.register_name_button_next)
    public void onButtonNextClick(){
        presenter.setNameAndPassword(
                editTextName.getText().toString(),
                editTextPassword.getText().toString(),
                editTextConfirm.getText().toString());
    }

    @OnTextChanged({
            R.id.register_edit_text_name,
            R.id.register_edit_text_name_password_confirm,
            R.id.register_edit_text_name_password
    })
    public void onTextChanged(CharSequence s){
        buttonNext.setEnabled(
                !editTextConfirm.getText().toString().isEmpty() &&
                !editTextName.getText().toString().isEmpty() &&
                !editTextPassword.getText().toString().isEmpty()
        );
        editTextName.setBackground(findDrawable(R.drawable.edit_text_background));
        inputLayoutName.setError(null);
        inputLayoutName.setErrorEnabled(false);

        editTextPassword.setBackground(findDrawable(R.drawable.edit_text_background));
        inputLayoutPassword.setError(null);
        inputLayoutPassword.setErrorEnabled(false);

        editTextConfirm.setBackground(findDrawable(R.drawable.edit_text_background));
        inputLayoutConfirm.setError(null);
        inputLayoutConfirm.setErrorEnabled(false);
    }


    @Override
    protected int getLayout() {
        return R.layout.fragment_register_name_password;
    }
}
