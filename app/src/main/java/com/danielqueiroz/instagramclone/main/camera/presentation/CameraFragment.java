package com.danielqueiroz.instagramclone.main.camera.presentation;

import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.danielqueiroz.instagramclone.R;
import com.danielqueiroz.instagramclone.common.component.CameraPreview;
import com.danielqueiroz.instagramclone.common.component.MediaHelper;
import com.danielqueiroz.instagramclone.common.view.AbstractFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class CameraFragment extends AbstractFragment {

    @BindView(R.id.camera_progress)
    ProgressBar progressBar;

    @BindView(R.id.camera_surface)
    FrameLayout frameLayout;

    @BindView(R.id.container_preview)
    LinearLayout containerPreview;

    @BindView(R.id.camera_image_view_picture)
    Button buttonCamera;

    private MediaHelper mediaHelper;
    private Camera camera;

    public CameraFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO: app:layout_scrollFlags="scroll" na toolbar
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (getContext() != null) {
            mediaHelper = MediaHelper.getInstance(this);
            if (mediaHelper.checkCameraHardware(getContext())){
                camera = mediaHelper.getCameraInstance();
                CameraPreview cameraPreview = new CameraPreview(getContext(), camera);
                frameLayout.addView(cameraPreview);
            }
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (camera != null) {
            camera.release();
        }
    }

    @OnClick(R.id.camera_image_view_picture)
    public void onCameraButtonClick(){
        progressBar.setVisibility(View.VISIBLE);
        buttonCamera.setVisibility(View.GONE);
        camera.takePicture(null, null, (data, camera) -> {
            mediaHelper.saveCameraFile(data);
            progressBar.setVisibility(View.GONE);
            buttonCamera.setVisibility(View.VISIBLE);
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_main_camera;
    }

}
