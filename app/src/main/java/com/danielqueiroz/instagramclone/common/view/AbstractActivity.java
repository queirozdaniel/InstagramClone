package com.danielqueiroz.instagramclone.common.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.danielqueiroz.instagramclone.R;
import com.danielqueiroz.instagramclone.common.util.Colors;
import com.danielqueiroz.instagramclone.common.util.Drawables;
import com.danielqueiroz.instagramclone.login.presentation.LoginActivity;

import butterknife.ButterKnife;

public abstract class AbstractActivity extends AppCompatActivity implements View {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);

        onInject();
    }

    protected abstract @LayoutRes
    int getLayout();

    public Drawable findDrawable(@DrawableRes int drawableId) {
        return Drawables.getDrawable(this, drawableId);
    }

    public int findColor(@ColorRes int colorId){
        return Colors.getColor(this, colorId);
    }

    @Override
    public Context getContext() {
        return getBaseContext();
    }

    @Override
    public void showProgressBar() { }

    @Override
    public void hideProgressBar() { }

    @Override
    public void setStatusBarDark() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(findColor(R.color.colorAccent));
        }
    }

    protected abstract void onInject();
}
