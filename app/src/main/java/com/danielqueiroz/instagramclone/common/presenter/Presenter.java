package com.danielqueiroz.instagramclone.common.presenter;

import com.danielqueiroz.instagramclone.common.model.UserAuth;

public interface Presenter {

    void onSuccess(UserAuth response);
    void onError(String message);
    void onComplete();

}
