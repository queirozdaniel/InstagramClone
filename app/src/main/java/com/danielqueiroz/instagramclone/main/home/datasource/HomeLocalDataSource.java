package com.danielqueiroz.instagramclone.main.home.datasource;

import com.danielqueiroz.instagramclone.common.model.Database;
import com.danielqueiroz.instagramclone.common.model.Feed;
import com.danielqueiroz.instagramclone.common.presenter.Presenter;

import java.util.List;

public class HomeLocalDataSource implements HomeDataSource {

    @Override
    public void findFeed(Presenter<List<Feed>> presenter) {
        Database db = Database.getInstance();
        db.findFeed(db.getUser().getUUID())
                .addOnSuccessListener(presenter::onSuccess)
                .addOnFailuerListener(e -> presenter.onError(e.getMessage()))
                .addCompleteListener(presenter::onComplete);
    }

}
