package com.example.do_an;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.do_an.fragment.ChatFragment;
import com.example.do_an.fragment.HomeFragment;

import com.example.do_an.fragment.SettingFragment;
import com.example.do_an.fragment.TransHisFragment;
import com.example.do_an.fragment.PrefereFragment;
import com.example.do_an.ui.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView mnBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mnBottom = findViewById(R.id.navmenu);
        mnBottom.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        HomeFragment fragment = new HomeFragment();
        loadFragment(fragment);
        mnBottom.setOnItemSelectedListener(getListener());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        return true;
    }

    @NonNull
    private NavigationBarView.OnItemSelectedListener getListener() {
        return new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mnHome:
                        loadFragment(new HomeFragment());
                        break;

                    case R.id.mnUuDai:
                        loadFragment(new PrefereFragment());
                        break;
//
                    case R.id.LSGD:
                        loadFragment(new TransHisFragment());
                        break;
//
                    case R.id.mnChat:
                        loadFragment(new ChatFragment());
                        break;

                    case R.id.mnViCuaToi:
                        loadFragment(new SettingFragment());
                        break;
                }
                return true;
            }
        };
    }

    void loadFragment(Fragment fmNew) {
        // In your activity, when replacing fragments:
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, fmNew);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}