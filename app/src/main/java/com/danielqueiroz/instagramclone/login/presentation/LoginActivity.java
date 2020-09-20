package com.danielqueiroz.instagramclone.login.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.danielqueiroz.instagramclone.R;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        final EditText editTextEmail = findViewById(R.id.login_edit_text_email);
        final EditText editTextPassword = findViewById(R.id.login_edit_text_password);

        editTextEmail.addTextChangedListener(watcher);
        editTextPassword.addTextChangedListener(watcher);

        findViewById(R.id.login_button_enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputLayout inputLayout = findViewById(R.id.login_edit_text_email_input);
                inputLayout.setError("Esse email é inválido");
                editTextEmail.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.edit_text_background_error));

                TextInputLayout inputLayoutP = findViewById(R.id.login_edit_text_password_input);
                inputLayoutP.setError("Essa senha é inválida");
                editTextPassword.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.edit_text_background_error));
            }
        });

    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().isEmpty()) {
                findViewById(R.id.login_button_enter).setEnabled(true);
            } else {
                findViewById(R.id.login_button_enter).setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}