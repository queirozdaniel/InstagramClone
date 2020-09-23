package com.danielqueiroz.instagramclone.register.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.danielqueiroz.instagramclone.R;
import com.danielqueiroz.instagramclone.common.view.CustomDialog;

public class RegisterPhotoFragment extends Fragment {

    public RegisterPhotoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO: scroll gravity top
        return inflater.inflate(R.layout.fragment_register_photo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CustomDialog customDialog = new CustomDialog.Builder(getContext())
                .setTitle(R.string.define_photo_profile)
                .addButton((v) -> {
                }, R.string.search_gallery)
                .build();

        customDialog.show();
    }
}
