package com.example.do_an.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.do_an.R;
import com.example.do_an.adapter.AccountAdapter;
import com.example.do_an.model.AccountModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AccountAdminActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private AccountAdapter accountAdapter;
    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tai_khoan_admin);
        recyclerView = findViewById(R.id.recycle_tk_admin);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        // Trong onCreate() của AccountAdminActivity
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        accountAdapter = new AccountAdapter(); // Khởi tạo adapter của bạn
        recyclerView.setAdapter(accountAdapter);

        accountAdapter.setOnItemClickListener(new AccountAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AccountModel account) {
                // Tạo Intent để chuyển từ AccountAdminActivity sang DetailTkActivity
                Intent intent = new Intent(AccountAdminActivity.this, AccountDetailActivity.class);
                // Đặt thông tin tài khoản để chuyển sang DetailTkActivity (nếu cần thiết)
                intent.putExtra("ACCOUNT_ID", account.getMaTTCN());
                startActivity(intent);
            }
        });
        db = FirebaseFirestore.getInstance();
        fetchDataFromFirestore();
    }
    private void fetchDataFromFirestore() {
        db.collection("UsersInfo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<AccountModel> accounts = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                AccountModel accountModel = document.toObject(AccountModel.class);
                                accounts.add(accountModel);
                            }
                            accountAdapter.setAccounts(accounts);
                        } else {
                            // Xử lý khi lấy dữ liệu thất bại
                        }
                    }
                });
    }
}