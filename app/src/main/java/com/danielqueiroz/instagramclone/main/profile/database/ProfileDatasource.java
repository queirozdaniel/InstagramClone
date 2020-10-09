package com.danielqueiroz.instagramclone.main.profile.database;

import com.danielqueiroz.instagramclone.common.model.UserProfile;
import com.danielqueiroz.instagramclone.common.presenter.Presenter;

public interface ProfileDatasource {

    void findUser(String user, Presenter<UserProfile> presenter);

}
