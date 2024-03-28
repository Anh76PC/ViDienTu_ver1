package com.example.do_an.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.do_an.R;
import com.example.do_an.model.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class PersonalInfoActivity extends AppCompatActivity {
    ImageButton backButton;
    TextView saveButton;
    EditText fullNameEditText, phoneNumberEditText, birthdayEditText, cccdEditText, genderEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        SharedPreferences sharedPreferences = getSharedPreferences("my_phone", Context.MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString("PHONE_NUMBER", "");

        backButton = findViewById(R.id.backTTCN);
        saveButton = findViewById(R.id.LuuTTCN);
        fullNameEditText = findViewById(R.id.hotenTTCN);
        phoneNumberEditText = findViewById(R.id.phoneTTCN);
        birthdayEditText = findViewById(R.id.sn);
        genderEditText = findViewById(R.id.sex1);
        cccdEditText = findViewById(R.id.cccd);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        birthdayEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = fullNameEditText.getText().toString();
                String birthday = birthdayEditText.getText().toString();
                String gender = genderEditText.getText().toString();
                String cccd = cccdEditText.getText().toString();

                UserInfo userInfo = new UserInfo("CN" + phoneNumber, fullName, gender, birthday, cccd);
                updateToFirestore(userInfo);
            }
        });

        getInfo(phoneNumber);
    }

    void getInfo(String phoneNumber) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("UsersInfo").document("CN" + phoneNumber).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                String hoTen = document.getString("HoTen");
                                String sn = document.getString("NgaySinh");
                                String cccd = document.getString("CCCD");
                                String sex = document.getString("GioiTinh");

                                fullNameEditText.setText(hoTen);
                                phoneNumberEditText.setText(phoneNumber);
                                birthdayEditText.setText(sn);
                                cccdEditText.setText(cccd);
                                genderEditText.setText(sex);
                            }
                        }
                    }
                });
    }

    private void updateToFirestore(UserInfo userInfo) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("UsersInfo").document(userInfo.getMaTTCN()).update(
                        "HoTen", userInfo.getHoTen(),
                        "NgaySinh", userInfo.getNgaySinh(),
                        "GioiTinh", userInfo.getGioiTinh(),
                        "CCCD", userInfo.getCCCD())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(PersonalInfoActivity.this, "Lưu thông tin thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PersonalInfoActivity.this, "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void showPopupMenu(View v) {
        // Your popup menu logic here
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                        birthdayEditText.setText(date);
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }
}
