package com.danielqueiroz.instagramclone.common.presenter;

import com.danielqueiroz.instagramclone.common.model.UserAuth;

public interface Presenter<T> {

    void onSuccess(T response);
    void onError(String message);
    void onComplete();

}
