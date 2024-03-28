package com.example.do_an.design_patten.Strategy;

public interface OnSearchResultListener {
    void onUserFound(String phoneNumber);

    void onUserNotFound();

    void onError();
}
