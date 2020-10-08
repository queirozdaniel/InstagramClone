package com.danielqueiroz.instagramclone.main.camera.presentation;

import android.net.Uri;

import com.danielqueiroz.instagramclone.common.view.View;

import java.util.List;

public interface GalleryView extends View {

    void onPicturesLoaded(List<Uri> uris);
}
