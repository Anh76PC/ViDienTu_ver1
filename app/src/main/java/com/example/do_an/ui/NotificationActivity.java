package com.example.do_an.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import static com.example.do_an.utils.Utils.numberOfNotifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.do_an.R;
import com.example.do_an.adapter.NotifyAdapter;
import com.example.do_an.adapter.TransHisAdapter;
import com.example.do_an.model.NotifyModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    private static final String TAG = "NotificationActivity";
    RecyclerView recyclerView;
    ImageButton btnNofBack;
    private List<NotifyModel> transactionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongbao);
        btnNofBack = findViewById(R.id.btnNofBack);
        recyclerView = findViewById(R.id.recyclethongbao);
        transactionList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SharedPreferences sharedPreferences = getSharedPreferences("my_phone", Context.MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString("PHONE_NUMBER", "");
        ActionBar actionBar = getSupportActionBar();
        numberOfNotifications = 0;
        if (actionBar != null) {
            actionBar.hide();
        }

        btnNofBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fetchUserTransactionsFromFirestore(phoneNumber);
    }
    private void fetchUserTransactionsFromFirestore(String phoneNumber) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        transactionList.clear(); // Xóa danh sách giao dịch trước khi thêm mới

        String[] collectionTypes = {"NapTien", "RutTien", "ChuyenTien"}; // Mảng chứa tên các collection

        for (String collectionType : collectionTypes) {
            db.collection("TransactionInfo").document(phoneNumber).collection(collectionType)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getString("iddata");
                                String amount = document.getString("pricetran");
                                String date = document.getString("date");
                                String time = document.getString("hour");
                                NotifyModel transaction = new NotifyModel(id, amount, date, time);
                                transactionList.add(transaction);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        // Cập nhật RecyclerView sau khi lấy xong dữ liệu từ một collection (có thể đặt trong onComplete hoặc sau vòng lặp)
                        updateRecyclerView();
                    });
        }
    }
    private void updateRecyclerView() {
        // Sắp xếp danh sách giao dịch theo thời gian (ngày và giờ)
        Collections.sort(transactionList, new Comparator<NotifyModel>() {
            @Override
            public int compare(NotifyModel o1, NotifyModel o2) {
                int dateComparison = o2.getDate().compareTo(o1.getDate());
                if (dateComparison == 0) {
                    return o2.getHour().compareTo(o1.getHour());
                }
                return dateComparison;
            }
        });

        // Cập nhật RecyclerView với danh sách giao dịch mới
        TransHisAdapter adapter = new TransHisAdapter(transactionList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}