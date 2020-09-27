package com.danielqueiroz.instagramclone.register.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.danielqueiroz.instagramclone.R;
import com.danielqueiroz.instagramclone.common.view.AbstractActivity;
import com.danielqueiroz.instagramclone.main.presentation.MainActivity;
import com.danielqueiroz.instagramclone.register.datasource.RegisterDataSource;
import com.danielqueiroz.instagramclone.register.datasource.RegisterLocalDataSource;

import butterknife.BindView;

public class RegisterActivity extends AbstractActivity implements RegisterView{

    @BindView(R.id.register_scrollview)
    ScrollView scrollView;

    public static void launch(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    private RegisterPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarDark();

    }

    @Override
    protected void onInject() {
        RegisterDataSource dataSource = new RegisterLocalDataSource();
        presenter = new RegisterPresenter(dataSource);
        presenter.setRegisterView(this);

        showNextView(RegisterSteps.EMAIL);
    }

    @Override
    public void showNextView(RegisterSteps steps) {
        Fragment fragment = null;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) scrollView.getLayoutParams();

        switch (steps){
            case EMAIL:
                layoutParams.gravity = Gravity.BOTTOM;
                fragment = RegisterEmailFragment.newInstance(presenter);
                break;
            case NAME_PASSWORD:
                layoutParams.gravity = Gravity.BOTTOM;
                fragment = RegisterNamePasswordFragment.newInstance(presenter);
                break;
            case WELCOME:
                layoutParams.gravity = Gravity.BOTTOM;
                fragment = RegisterWelcomeFragment.newInstance(presenter);
                break;
            case PHOTO:
                layoutParams.gravity = Gravity.TOP;
                fragment = RegisterPhotoFragment.newInstance(presenter);
                break;
        }
        scrollView.setLayoutParams(layoutParams);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if (manager.findFragmentById(R.id.register_fragment) == null){
            transaction.add(R.id.register_fragment, fragment, steps.name());
        } else {
            transaction.replace(R.id.register_fragment, fragment, steps.name());
            transaction.addToBackStack(steps.name());
        }

        transaction.commit();
    }

    @Override
    public void onUserCreated() {
        MainActivity.launch(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_register_email;
    }

}