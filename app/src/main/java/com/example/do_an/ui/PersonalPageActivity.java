package com.example.do_an.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.do_an.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PersonalPageActivity extends AppCompatActivity {
    LinearLayout personalInfoLayout, contactInfoLayout;
    TextView fullNameTextView, phoneNumberTextView;
    ImageButton backButton;
    Button logoutButton;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_page);

        firestore = FirebaseFirestore.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences("my_phone", Context.MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString("PHONE_NUMBER", "");

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        fullNameTextView = findViewById(R.id.fullNameTextView);
        logoutButton = findViewById(R.id.logoutButton);
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView);

        phoneNumberTextView.setText(phoneNumber);

        getName(phoneNumber).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String fullName) {
                if (!fullName.equals(""))
                    fullNameTextView.setText(fullName);
            }
        });

        personalInfoLayout = findViewById(R.id.personalInfoLayout);
        contactInfoLayout = findViewById(R.id.contactInfoLayout);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        personalInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalPageActivity.this, PersonalInfoActivity.class);
                startActivity(intent);
            }
        });

        contactInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonalPageActivity.this, ContactInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    public LiveData<String> getName(String phoneNumber) {
        MutableLiveData<String> fullNameLiveData = new MutableLiveData<>();

        firestore.collection("UsersInfo").document("CN" + phoneNumber).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String fullName = document.getString("HoTen");
                                fullNameLiveData.postValue(fullName);
                            } else {
                                fullNameLiveData.postValue(""); // Document not found or HoTen is empty
                            }
                        } else {
                            fullNameLiveData.postValue(""); // Error occurred
                        }
                    }
                });

        return fullNameLiveData;
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có chắc chắn muốn đăng xuất khỏi ứng dụng?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(PersonalPageActivity.this, EnterPhoneNumberActivity.class);
                startActivity(intent);
                Toast myToast = Toast.makeText(getApplicationContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT);
                myToast.show();
                finishAffinity(); // kết thúc tất cả các activity và xóa khỏi stack
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing, just dismiss the dialog
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
