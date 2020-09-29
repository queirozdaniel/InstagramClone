package com.danielqueiroz.instagramclone.common.component;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class MediaHelper {

    private static MediaHelper INSTANCE;
    private static final int CAMERA_CODE = 985;
    private static final int GALLERY_CODE = 989;

    private Activity activity;
    private Fragment fragment;
    private Uri mCropImageUri;
    private Uri saveImageUri;
    private CropImageView cropImageView;
    private OnImageCroppedListener listener;


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

        cropImageView.setAspectRatio(1,1);
        cropImageView.setFixedAspectRatio(true);
        cropImageView.setOnCropImageCompleteListener((view, result) -> {
            Uri uri = result.getUri();
            if (uri !=null && listener != null){
                listener.onImageCropped(uri);
                cropImageView.setVisibility(View.GONE);
            }
        });
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

    public void chooserCamera() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (i.resolveActivity(getContext().getPackageManager()) != null){
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Log.e("CAM_ERROR", e.getMessage(),e);
            }
            if (photoFile != null) {
                mCropImageUri = FileProvider.getUriForFile(getContext(), "com.danielqueiroz.instagramclone.fileprovider", photoFile);
                i.putExtra(MediaStore.EXTRA_OUTPUT, mCropImageUri);
                activity.startActivityForResult(i, CAMERA_CODE);
            }
        }
    }

    private void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    private void setActivity(Activity activity) {
        this.activity = activity;
    }

    public MediaHelper listener(OnImageCroppedListener listener) {
        this.listener = listener;
        return this;
    }

    public void onActivitResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CODE && resultCode == RESULT_OK){
            if (CropImage.isReadExternalStoragePermissionsRequired(getContext(), mCropImageUri)){
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                    if (activity != null)
                        activity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
                    else
                        fragment.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
                }
            } else {
                startCropImageActivity();
            }
        } else if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            mCropImageUri = CropImage.getPickImageResultUri(getContext(), data);
            startCropImageActivity();
        }
    }

    private void startCropImageActivity() {
        cropImageView.setImageUriAsync(mCropImageUri);
    }

    public void cropImage() {
        File getImage = getContext().getExternalCacheDir();
        if (getImage != null){
            saveImageUri = Uri.fromFile(new File(getImage.getPath(), System.currentTimeMillis() + ".jpeg"));
        }
        cropImageView.saveCroppedImageAsync(saveImageUri);
    }

    private File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpeg", storageDir);
    }

    public interface OnImageCroppedListener {
        void onImageCropped(Uri uri);
    }
}
