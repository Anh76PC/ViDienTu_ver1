package com.example.do_an.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.do_an.MainActivity;
import com.example.do_an.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    Button loginButton;
    TextView greetingTextView, changePhoneTextView, forgotPasswordTextView;
    EditText passwordEditText;
    ImageButton backButton;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPreferences = getSharedPreferences("my_phone", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        String phoneNumber = getIntent().getStringExtra("PHONE_NUMBER");

        loginButton = findViewById(R.id.btLogin);
        backButton = findViewById(R.id.backLogin);
        changePhoneTextView = findViewById(R.id.changePhone);
        greetingTextView = findViewById(R.id.greeting);
        forgotPasswordTextView = findViewById(R.id.forgotPass);
        passwordEditText = findViewById(R.id.passwordEditText);

        greetingTextView.setText("Xin chào, " + phoneNumber);

        firestore = FirebaseFirestore.getInstance();

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                Toast.makeText(LoginActivity.this, "Vui lòng nhập mã OTP để tạo lại mật khẩu", Toast.LENGTH_SHORT).show();
                intent.putExtra("PHONE_NUMBER", phoneNumber);
                intent.putExtra("check", "1");
                startActivity(intent);
            }
        });

        changePhoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, EnterPhoneNumberActivity.class);
                startActivity(intent);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = passwordEditText.getText().toString().trim();

                firestore.collection("UsersInfo").document("CN" + phoneNumber).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        String storedPassword = document.getLong("MatKhau").toString();
                                        if (storedPassword != null && storedPassword.equals(password)) {
                                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            intent.putExtra("PHONE_NUMBER", phoneNumber);
                                            editor.putString("PHONE_NUMBER", phoneNumber);
                                            editor.apply();
                                            startActivity(intent);
                                            finishAffinity();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(LoginActivity.this, "User not found!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
