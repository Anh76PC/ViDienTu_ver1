package com.example.do_an.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.do_an.R;
import com.example.do_an.adapter.TransHisAdapter;
import com.example.do_an.model.NotifyModel;
import com.example.do_an.ui.StatisticalActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TransHisFragment extends Fragment {
    private static final String TAG = "TransactionHistoryFragment";
    private RecyclerView recyclerView;
    private List<NotifyModel> transactionList;
    private TextView report;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trans_his, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("my_phone", Context.MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString("PHONE_NUMBER", "");
        report = view.findViewById(R.id.report);
        recyclerView = view.findViewById(R.id.recycle_transhis);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        transactionList = new ArrayList<>();

        fetchUserTransactionsFromFirestore(phoneNumber);

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StatisticalActivity.class);
                startActivity(intent);
            }
        });

        return view;
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


    public TransHisFragment() {
        super();
    }
}
