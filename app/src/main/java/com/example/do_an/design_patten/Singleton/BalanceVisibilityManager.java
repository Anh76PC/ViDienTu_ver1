package com.example.do_an.design_patten.Singleton;

public class BalanceVisibilityManager {

    private static BalanceVisibilityManager instance;
    private boolean isBalanceShown;

    private BalanceVisibilityManager() {
    }

    public static BalanceVisibilityManager getInstance() {
        if (instance == null) {
            instance = new BalanceVisibilityManager();
        }
        return instance;
    }

    public boolean isBalanceShown() {
        return isBalanceShown;
    }

    public void setBalanceShown(boolean isBalanceShown) {
        this.isBalanceShown = isBalanceShown;
    }
}
