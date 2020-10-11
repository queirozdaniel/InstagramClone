package com.danielqueiroz.instagramclone.main.camera.presentation;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.danielqueiroz.instagramclone.common.view.AbstractActivity;
import com.danielqueiroz.instagramclone.main.camera.datasource.AddDataSource;
import com.danielqueiroz.instagramclone.main.camera.datasource.AddFireDataSource;
import com.danielqueiroz.instagramclone.main.camera.datasource.AddLocalDataSource;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.danielqueiroz.instagramclone.R;

import butterknife.BindView;

public class AddCaptionActivity extends AbstractActivity implements AddCaptionView {

    @BindView(R.id.main_add_caption_image_view)
    ImageView imageView;

    @BindView(R.id.main_add_caption_edit_text)
    EditText editText;

    @BindView(R.id.add_progress_bar)
    ProgressBar progressBar;

    private Uri uri;
    private AddPresenter presenter;

    public static void launch(Context context, Uri uri) {
        Intent intent = new Intent(context, AddCaptionActivity.class);
        intent.putExtra("uri", uri);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            Drawable drawable = findDrawable(R.drawable.ic_arrow_back);
            getSupportActionBar().setHomeAsUpIndicator(drawable);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }

    @Override
    protected void onInject() {
        uri = getIntent().getExtras().getParcelable("uri");
        imageView.setImageURI(uri);

        AddDataSource dataSource = new AddFireDataSource();
        presenter = new AddPresenter(this, dataSource);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_share:
                presenter.createPost(uri, editText.getText().toString());
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void postSaved() {
        finish();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_add_caption;
    }

}