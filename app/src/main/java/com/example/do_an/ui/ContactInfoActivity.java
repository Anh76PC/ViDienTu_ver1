package com.example.do_an.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.do_an.R;
import com.example.do_an.model.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ContactInfoActivity extends AppCompatActivity {
    ImageButton backButton;
    TextView saveButton;
    EditText addressEditText, emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        SharedPreferences sharedPreferences = getSharedPreferences("my_phone", Context.MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString("PHONE_NUMBER", "");

        backButton = findViewById(R.id.backTTLH);
        saveButton = findViewById(R.id.saveTTLH);
        addressEditText = findViewById(R.id.address1);
        emailEditText = findViewById(R.id.email);

        getInfo(phoneNumber);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfo userInfo = new UserInfo("CN" + phoneNumber, emailEditText.getText().toString(), addressEditText.getText().toString());
                updateToFirestore(userInfo);
            }
        });
    }

    void getInfo(String phoneNumber) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("UsersInfo").document("CN" + phoneNumber).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String address = document.getString("DiaChi");
                                addressEditText.setText(address);
                                String email = document.getString("Email");
                                emailEditText.setText(email);
                            }
                        }
                    }
                });
    }

    private void updateToFirestore(UserInfo userInfo) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("UsersInfo").document(userInfo.getMaTTCN() + "").update("Email", userInfo.getEmail(), "DiaChi", userInfo.getDiaChi())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ContactInfoActivity.this, "Lưu thông tin thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ContactInfoActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show(); // Khi lưu thất bại
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() { // Khi lưu thất bại
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ContactInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
