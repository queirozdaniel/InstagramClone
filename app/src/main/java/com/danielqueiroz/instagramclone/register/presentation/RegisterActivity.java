package com.danielqueiroz.instagramclone.register.presentation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.danielqueiroz.instagramclone.R;
import com.danielqueiroz.instagramclone.common.component.MediaHelper;
import com.danielqueiroz.instagramclone.common.view.AbstractActivity;
import com.danielqueiroz.instagramclone.main.presentation.MainActivity;
import com.danielqueiroz.instagramclone.register.datasource.RegisterDataSource;
import com.danielqueiroz.instagramclone.register.datasource.RegisterLocalDataSource;
import com.theartofdev.edmodo.cropper.CropImageView;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends AbstractActivity implements RegisterView, MediaHelper.OnImageCroppedListener{
    @BindView(R.id.register_root_container)
    FrameLayout rootContainer;

    @BindView(R.id.register_scrollview)
    ScrollView scrollView;

    @BindView(R.id.register_crop_image_view)
    CropImageView cropImageView;

    @BindView(R.id.register_button_crop)
    Button buttonCrop;

    private MediaHelper mediaHelper;
    private RegisterPresenter presenter;

    public static void launch(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarDark();
        mediaHelper = MediaHelper.getInstance(this)
            .cropView(cropImageView)
            .listener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cropViewEnabled(true);
        MediaHelper mediaHelper = MediaHelper.getInstance(this);
        mediaHelper.onActivitResult(requestCode, resultCode, data);
    }

    @Override
    public void onImageCropped(Uri uri) {
        presenter.setUri(uri);
    }

    @Override
    public void onImagePicked(Uri uri) {
        cropImageView.setImageUriAsync(uri);
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

        switch (steps){
            case EMAIL:
                fragment = RegisterEmailFragment.newInstance(presenter);
                break;
            case NAME_PASSWORD:
                fragment = RegisterNamePasswordFragment.newInstance(presenter);
                break;
            case WELCOME:
                fragment = RegisterWelcomeFragment.newInstance(presenter);
                break;
            case PHOTO:
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) scrollView.getLayoutParams();
                layoutParams.gravity = Gravity.TOP;
                scrollView.setLayoutParams(layoutParams);
                fragment = RegisterPhotoFragment.newInstance(presenter);
                break;
        }

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
    public void showCamera() {
       mediaHelper.chooserCamera();
    }

    @Override
    public void showGallery() {
        mediaHelper.chooserGallery();
    }

    @OnClick(R.id.register_button_crop)
    public void onButtonCropClick(){
        cropViewEnabled(false);
        MediaHelper.getInstance(this).cropImage(cropImageView);
    }

    private void cropViewEnabled(boolean enabled) {
        cropImageView.setVisibility(enabled ? View.VISIBLE : View.GONE);
        scrollView.setVisibility(enabled ? View.GONE : View.VISIBLE);
        buttonCrop.setVisibility(enabled ? View.VISIBLE : View.GONE);
        rootContainer.setBackgroundColor(enabled ? findColor(android.R.color.black) : findColor(android.R.color.white));
    }

    @Override
    public void onUserCreated() {
        MainActivity.launch(this,  MainActivity.REGISTER_ACTIVITY);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_register_email;
    }

}