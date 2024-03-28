package com.example.do_an.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.do_an.R;

public class LoginAndSecurityActivity extends AppCompatActivity {
    LinearLayout changePasswordLayout, lockAccountLayout;
    ImageButton backLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_security);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        changePasswordLayout = findViewById(R.id.changePassSetting);
        backLoginButton = findViewById(R.id.btBack);
        lockAccountLayout = findViewById(R.id.lockAcc);

        backLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        changePasswordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginAndSecurityActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        lockAccountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginAndSecurityActivity.this, LockAccountActivity.class);
                startActivity(intent);
            }
        });
    }
}
