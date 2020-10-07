package com.danielqueiroz.instagramclone.main.camera.presentation;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;


import com.danielqueiroz.instagramclone.R;
import com.danielqueiroz.instagramclone.common.view.AbstractActivity;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;

public class AddActivity extends AbstractActivity implements AddView{

    @BindView(R.id.add_viewpager)
    ViewPager viewPager;

    @BindView(R.id.add_tab_layout)
    TabLayout tabLayout;

    public static void launch(Context context) {
        Intent intent = new Intent(context, AddActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            Drawable drawable = findDrawable(R.drawable.ic_close);
            getSupportActionBar().setHomeAsUpIndicator(drawable);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager){
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
                viewPager.setCurrentItem(tab.getPosition());
            }
        });

    }

    @Override
    protected void onInject() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        GalleryFragment galleryFragment = new GalleryFragment();
        adapter.add(galleryFragment);

        CameraFragment cameraFragment = CameraFragment.newInstance(this);
        adapter.add(cameraFragment);

        adapter.notifyDataSetChanged();
        tabLayout.setupWithViewPager(viewPager);

        TabLayout.Tab tabLeft = tabLayout.getTabAt(0);
        if (tabLeft != null)
            tabLeft.setText(getString(R.string.gallery));

        TabLayout.Tab tabRight = tabLayout.getTabAt(1);
        if (tabRight != null)
            tabRight.setText(getString(R.string.photo));

        viewPager.setCurrentItem(adapter.getCount() - 1);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onImageLoaded(Uri uri) {
        AddCaptionActivity.launch(this, uri);
        finish();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_add;
    }

}
