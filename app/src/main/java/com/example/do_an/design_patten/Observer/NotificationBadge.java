package com.example.do_an.design_patten.Observer;

import android.view.View;
import android.widget.TextView;

public class NotificationBadge implements NotificationObserver {

    private TextView badgeText;

    public NotificationBadge(TextView badgeText) {
        this.badgeText = badgeText;
    }

    @Override
    public void onNotificationCountChanged(int count) {
        if (count > 0) {
            badgeText.setText(String.valueOf(count));
            badgeText.setVisibility(View.VISIBLE);
        } else {
            badgeText.setVisibility(View.GONE);
        }
    }
}

