package com.danielqueiroz.instagramclone.main.profile.database;

import com.danielqueiroz.instagramclone.common.model.UserProfile;
import com.danielqueiroz.instagramclone.common.presenter.Presenter;
import com.danielqueiroz.instagramclone.main.profile.presentation.ProfilePresenter;

public interface ProfileDatasource {

    void findUser(Presenter<UserProfile> presenter);

}
