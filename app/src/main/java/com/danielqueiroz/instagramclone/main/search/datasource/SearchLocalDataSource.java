package com.danielqueiroz.instagramclone.main.search.datasource;

import com.danielqueiroz.instagramclone.common.model.Database;
import com.danielqueiroz.instagramclone.common.model.User;
import com.danielqueiroz.instagramclone.common.presenter.Presenter;

import java.util.List;

public class SearchLocalDataSource implements SearchDataSource{
    @Override
    public void findUsers(String query, Presenter<List<User>> presenter) {
        Database db = Database.getInstance();
        db.findUsers(db.getUser().getUUID(), query)
                .addOnSuccessListener(presenter::onSuccess)
                .addOnFailuerListener(e -> presenter.onError(e.getMessage()))
                .addCompleteListener(presenter::onComplete);
    }
}
