package com.example.do_an.design_patten.Strategy;

public interface SearchStrategy {
    void searchUser(String phoneNumber, OnSearchResultListener listener);
}

