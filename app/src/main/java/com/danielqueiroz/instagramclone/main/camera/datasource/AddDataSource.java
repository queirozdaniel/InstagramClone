package com.danielqueiroz.instagramclone.main.camera.datasource;

import android.net.Uri;

import com.danielqueiroz.instagramclone.common.presenter.Presenter;

public interface AddDataSource {
    void savePost(Uri uri, String caption, Presenter presenter);
}
