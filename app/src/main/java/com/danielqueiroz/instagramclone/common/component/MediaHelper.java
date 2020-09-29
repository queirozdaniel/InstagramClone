package com.danielqueiroz.instagramclone.common.component;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class MediaHelper {

    private static WeakReference<MediaHelper> INSTANCE;
    private static final int CAMERA_CODE = 985;
    private static final int GALLERY_CODE = 989;

    private Activity activity;
    private Fragment fragment;
    private Uri mCropImageUri;
    private Uri saveImageUri;
    private OnImageCroppedListener listener;


    public static MediaHelper getInstance(Activity activity){
        if (INSTANCE == null){
            MediaHelper mediaHelper = new MediaHelper();
            INSTANCE = new WeakReference<>(mediaHelper);
            INSTANCE.get().setActivity(activity);
        }
        return INSTANCE.get();
    }

    public static MediaHelper getInstance(Fragment fragment){
        if (INSTANCE == null){
            MediaHelper mediaHelper = new MediaHelper();
            INSTANCE = new WeakReference<>(mediaHelper);
            INSTANCE.get().setFragment(fragment);
        }
        return INSTANCE.get();
    }

    public MediaHelper cropView(CropImageView cropImageView){

        cropImageView.setAspectRatio(1,1);
        cropImageView.setFixedAspectRatio(true);
        cropImageView.setOnCropImageCompleteListener((view, result) -> {
            Uri uri = result.getUri();
            if (uri !=null && listener != null){
                listener.onImageCropped(uri);
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

                SharedPreferences myPrefs = getContext().getSharedPreferences("camera_image", 0);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.putString("url", mCropImageUri.toString());
                editor.apply();

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
        SharedPreferences myPrefs = getContext().getSharedPreferences("camera_image", 0);
        String url = myPrefs.getString("url", null);

        if (mCropImageUri != null && url != null)
            mCropImageUri = Uri.parse(url);

        if (requestCode == CAMERA_CODE && resultCode == RESULT_OK){
            if (CropImage.isReadExternalStoragePermissionsRequired(getContext(), mCropImageUri)){
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                    if (activity != null)
                        activity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
                    else
                        fragment.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
                }
            } else {
                listener.onImagePicked(mCropImageUri);
            }
        } else if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            mCropImageUri = CropImage.getPickImageResultUri(getContext(), data);
            listener.onImagePicked(mCropImageUri);
        }
    }

    public void cropImage(CropImageView cropImageView) {
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

        void onImagePicked(Uri uri);
    }
}
