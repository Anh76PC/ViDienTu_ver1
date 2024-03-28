package com.example.do_an.design_patten.Strategy;

public class UserManager {
    private SearchStrategy searchStrategy;

    public UserManager(SearchStrategy searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    public void searchUser(String phoneNumber, OnSearchResultListener listener) {
        searchStrategy.searchUser(phoneNumber, listener);
    }
}

