package com.danielqueiroz.instagramclone.register.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.danielqueiroz.instagramclone.R;

public class RegisterPhotoFragment extends Fragment {

    public RegisterPhotoFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO: scroll gravity top
        return inflater.inflate(R.layout.fragment_register_photo, container, false);
    }
}
