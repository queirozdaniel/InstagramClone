package com.danielqueiroz.instagramclone.main.presentation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.danielqueiroz.instagramclone.R;
import com.danielqueiroz.instagramclone.common.view.AbstractActivity;
import com.danielqueiroz.instagramclone.login.presentation.LoginActivity;
import com.danielqueiroz.instagramclone.main.camera.presentation.CameraFragment;
import com.danielqueiroz.instagramclone.main.home.datasource.HomeDataSource;
import com.danielqueiroz.instagramclone.main.home.datasource.HomeLocalDataSource;
import com.danielqueiroz.instagramclone.main.home.presentation.HomeFragment;
import com.danielqueiroz.instagramclone.main.home.presentation.HomePresenter;
import com.danielqueiroz.instagramclone.main.profile.database.ProfileDatasource;
import com.danielqueiroz.instagramclone.main.profile.database.ProfileLocalDataSource;
import com.danielqueiroz.instagramclone.main.profile.presentation.ProfileFragment;
import com.danielqueiroz.instagramclone.main.profile.presentation.ProfilePresenter;
import com.danielqueiroz.instagramclone.main.search.presentation.SearchFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AbstractActivity implements BottomNavigationView.OnNavigationItemSelectedListener, MainView {

    public static  final int LOGIN_ACTIVITY = 0;
    public static  final int REGISTER_ACTIVITY = 1;
    public static  final String ACT_SOURCE = "act_source";

    private ProfilePresenter profilePresenter;
    private HomePresenter homePresenter;

    Fragment homeFragment;
    Fragment profileFragment;
    Fragment cameraFragment;
    Fragment searchFragment;
    Fragment active;


    public static void launch(Context context, int source) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(ACT_SOURCE, source);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            Drawable drawable = getResources().getDrawable(R.drawable.ic_insta_camera);
            getSupportActionBar().setHomeAsUpIndicator(drawable);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    protected void onInject() {
        ProfileDatasource profileDatasource = new ProfileLocalDataSource();
        HomeDataSource homeDataSource = new HomeLocalDataSource();

        profilePresenter = new ProfilePresenter(profileDatasource);
        homePresenter = new HomePresenter(homeDataSource);


        homeFragment = HomeFragment.newInstance(this, homePresenter);
        profileFragment = ProfileFragment.newInstance(this, profilePresenter);
        cameraFragment = new CameraFragment();
        searchFragment = new SearchFragment();

        active = homeFragment;

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.main_fragment, profileFragment).hide(profileFragment).commit();
        fragmentManager.beginTransaction().add(R.id.main_fragment, cameraFragment).hide(cameraFragment).commit();
        fragmentManager.beginTransaction().add(R.id.main_fragment, searchFragment).hide(searchFragment).commit();
        fragmentManager.beginTransaction().add(R.id.main_fragment, homeFragment).hide(homeFragment).commit();

    }

    @Override
    public void showProgressBar() {
        findViewById(R.id.main_progress_bar).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        findViewById(R.id.main_progress_bar).setVisibility(View.GONE);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        BottomNavigationView bv = findViewById(R.id.main_bottom_nav);
        bv.setOnNavigationItemSelectedListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int source = extras.getInt(ACT_SOURCE);
            if (source == REGISTER_ACTIVITY){
                getSupportFragmentManager().beginTransaction().hide(active).show(profileFragment).commit();
                active = profileFragment;
                scrollToolbarEnebled(true);
                profilePresenter.finUser();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void scrollToolbarEnebled(boolean enabled) {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        AppBarLayout appBarLayout = findViewById(R.id.main_appbar);

        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        CoordinatorLayout.LayoutParams appBarLayoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();

        if (enabled) {
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
            appBarLayoutParams.setBehavior(new AppBarLayout.Behavior());
            appBarLayout.setLayoutParams(appBarLayoutParams);
        } else {
            params.setScrollFlags(0);
            appBarLayoutParams.setBehavior(null);
            appBarLayout.setLayoutParams(appBarLayoutParams);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        switch (item.getItemId()){
            case R.id.menu_bottom_home:
                fm.beginTransaction().hide(active).show(homeFragment).commit();
                scrollToolbarEnebled(false);
                homePresenter.findFeed();
                active = homeFragment;
                return true;

            case R.id.menu_bottom_search:
                fm.beginTransaction().hide(active).show(searchFragment).commit();
                active = searchFragment;
                return true;

            case R.id.menu_bottom_profile:
                fm.beginTransaction().hide(active).show(profileFragment).commit();
                scrollToolbarEnebled(true);
                profilePresenter.finUser();
                active = profileFragment;
                return true;

            case R.id.menu_bottom_add:
                fm.beginTransaction().hide(active).show(cameraFragment).commit();
                active = cameraFragment;
                return true;
        }

        return false;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

}