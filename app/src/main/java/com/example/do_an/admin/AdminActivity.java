package com.example.do_an.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.do_an.R;
import com.example.do_an.ui.EnterPhoneNumberActivity;
import com.example.do_an.ui.PrefereAdminActivity;
import com.example.do_an.ui.StaticsAdminActivity;

public class AdminActivity extends AppCompatActivity {
    private TextView logoutAdmin;

    LinearLayout taiKhoan, uuDai, phanHoi, thongKe;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        logoutAdmin = findViewById(R.id.logout_admin);
        taiKhoan = findViewById(R.id.taikhoan_admin);
        uuDai = findViewById(R.id.uudai_admin);
        phanHoi = findViewById(R.id.phanhoi_admin);
        thongKe = findViewById(R.id.thongke_admin);

        taiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AccountAdminActivity.class);
                startActivity(intent);
            }
        });

        uuDai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, PrefereAdminActivity.class);
                startActivity(intent);
            }
        });

//        phanHoi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(AdminActivity.this, PhanHoiAdminActivity.class);
//                startActivity(intent);
//            }
//        });

        thongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, StaticsAdminActivity.class);
                startActivity(intent);
            }
        });

        logoutAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có chắc chắn muốn đăng xuất khỏi ứng dụng?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(AdminActivity.this, EnterPhoneNumberActivity.class);
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
