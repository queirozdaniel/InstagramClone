package com.danielqueiroz.instagramclone.main.camera.datasource;

import android.net.Uri;

import com.danielqueiroz.instagramclone.common.model.Database;
import com.danielqueiroz.instagramclone.common.presenter.Presenter;

public class AddLocalDataSource implements AddDataSource {

    @Override
    public void savePost(Uri uri, String caption, Presenter presenter) {
        Database db = Database.getInstance();
        db.createPost(db.getUser().getUUID(), uri, caption)
                .addOnSuccessListener((Database.OnSuccessListener<Void>) presenter::onSuccess)
                .addOnFailuerListener(e -> presenter.onError(e.getMessage()))
                .addCompleteListener(presenter::onComplete);
    }
}
