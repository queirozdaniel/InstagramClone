package com.danielqueiroz.instagramclone.main.camera.datasource;

import android.content.Context;

import com.danielqueiroz.instagramclone.common.presenter.Presenter;

public interface GalleryDataSource {
    void findPictures(Context context, Presenter presenter);
}
