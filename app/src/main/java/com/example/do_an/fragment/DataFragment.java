package com.example.do_an.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.do_an.R;
import com.example.do_an.adapter.DataAdapter;
import com.example.do_an.model.MenuCollection;
import com.example.do_an.ui.DPDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class DataFragment extends Fragment {
    private RecyclerView recyclerView;
    private ImageButton backButton;
    private List<MenuCollection> dataList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data, container, false);

        backButton = view.findViewById(R.id.btnBack1);
        recyclerView = view.findViewById(R.id.recyclerView);

        setupRecyclerView();
        setupBackButton();

        return view;
    }

    private void setupRecyclerView() {
        dataList = new ArrayList<>();
        dataList.add(new MenuCollection(1, "70.000 Đ", R.drawable.k70));
        dataList.add(new MenuCollection(2, "90.000 Đ", R.drawable.k90));
        dataList.add(new MenuCollection(3, "120.000 Đ", R.drawable.k120));
        dataList.add(new MenuCollection(4, "30.000 Đ", R.drawable.k30));
        dataList.add(new MenuCollection(5, "10.000 Đ", R.drawable.k10));
        dataList.add(new MenuCollection(6, "5.000 Đ", R.drawable.k5));
        dataList.add(new MenuCollection(7, "3.000 Đ", R.drawable.k3));
        dataList.add(new MenuCollection(8, "15.000 Đ", R.drawable.k15));

        DataAdapter adapter = new DataAdapter(dataList, requireContext(), new DataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MenuCollection menuCollection, String title) {
                Intent intent = new Intent(requireContext(), DPDetailActivity.class);
                intent.putExtra("title", title);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void setupBackButton() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
    }
}
