package com.danielqueiroz.instagramclone.common.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static android.app.Activity.RESULT_OK;

public class MediaHelper {

    private static MediaHelper INSTANCE;
    private static final int CAMERA_CODE = 985;
    private static final int GALLERY_CODE = 989;

    private Activity activity;
    private Fragment fragment;
    private Uri mCropImagetUri;
    private CropImageView cropImageView;


    public static MediaHelper getInstance(Activity activity){
        if (INSTANCE == null)
            INSTANCE = new MediaHelper();
        INSTANCE.setActivity(activity);
        return INSTANCE;
    }

    public static MediaHelper getInstance(Fragment fragment){
        if (INSTANCE == null)
            INSTANCE = new MediaHelper();
        INSTANCE.setFragment(fragment);
        return INSTANCE;
    }

    public MediaHelper cropView(CropImageView cropImageView){
        this.cropImageView = cropImageView;

        return this;
    }

    private Context getContext(){
        if (fragment != null && fragment.getActivity() != null) {
            return fragment.getContext();
        }

        return activity;
    }

    public void chooserGallery(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(i, GALLERY_CODE);
    }

    private void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    private void setActivity(Activity activity) {
        this.activity = activity;
    }


    public void onActivitResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CODE && resultCode == RESULT_OK){

        } else if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            mCropImagetUri = CropImage.getPickImageResultUri(getContext(), data);
            startCropImageActivity();
        }
    }

    private void startCropImageActivity() {
        cropImageView.setImageUriAsync(mCropImagetUri);
    }
}
