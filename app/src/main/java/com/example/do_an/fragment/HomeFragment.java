

package com.example.do_an.fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.do_an.utils.Utils.numberOfNotifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.do_an.R;
import com.example.do_an.design_patten.Singleton.BalanceVisibilityManager;
import com.example.do_an.design_patten.Strategy.FirestoreSearchStrategy;
import com.example.do_an.design_patten.Strategy.OnSearchResultListener;
import com.example.do_an.design_patten.Strategy.UserManager;
import com.example.do_an.ui.NotificationActivity;
import com.example.do_an.ui.PersonalInformationActivity;
import com.example.do_an.ui.TransActivity;
import com.example.do_an.ui.DataActivity;
import com.example.do_an.ui.DeposActivity;
import com.example.do_an.ui.PersonalPageActivity;
import com.example.do_an.ui.WithdActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nex3z.notificationbadge.NotificationBadge;

// Singleton class for managing balance visibility

public class HomeFragment extends Fragment {
    ImageButton goUsers, thongBao, showSoduButton;
    TextView soDuVi;
    EditText searchEditText;
    LinearLayout napTien, rutTien, chuyenTien, napData;
    NotificationBadge notificationBadge;
    private UserManager userManager;
    private String searchNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userManager = new UserManager(new FirestoreSearchStrategy(FirebaseFirestore.getInstance()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("my_phone", MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString("PHONE_NUMBER", "");
        notificationBadge = view.findViewById(R.id.notification);
        goUsers = view.findViewById(R.id.goUsers);
        showSoduButton = view.findViewById(R.id.showSodu);

        soDuVi = view.findViewById(R.id.soduvi);
        napTien = view.findViewById(R.id.naptien);
        rutTien = view.findViewById(R.id.rutTien);
        chuyenTien = view.findViewById(R.id.chuyenTien);
        searchEditText = view.findViewById(R.id.search);
        napData = view.findViewById(R.id.napdata);
        thongBao = view.findViewById(R.id.thongbao);
        notificationBadge.setText(String.valueOf(numberOfNotifications));
        // Set visibility based on notification count
        if (numberOfNotifications > 0) {
            notificationBadge.setVisibility(View.VISIBLE);

        } else {
            notificationBadge.setVisibility(View.GONE);
        }

        showSoduButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBalance(phoneNumber);
            }
        });

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        event != null && event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    searchNumber = searchEditText.getText().toString().trim();
                    if (searchNumber.isEmpty()) {
                        // Handle empty search input
                        return true; // consume event to prevent default behavior
                    }
                    performSearch(searchNumber);
                    return true;
                }
                return false;
            }
        });
        napTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DeposActivity.class);
                startActivity(intent);
            }
        });
        rutTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WithdActivity.class);
                startActivity(intent);
            }
        });
        chuyenTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TransActivity.class);
                startActivity(intent);
            }
        });
        thongBao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                startActivity(intent);

            }
        });
        napData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DataActivity.class);
                startActivity(intent);
            }
        });
        goUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PersonalPageActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }


    private void performSearch(String searchNumber) {
        userManager.searchUser(searchNumber, new OnSearchResultListener() {
            @Override
            public void onUserFound(String phoneNumber) {
                Intent intent = new Intent(getActivity(), PersonalInformationActivity.class);
                intent.putExtra("ACCOUNT_ID", "CN" + phoneNumber);
                startActivity(intent);
            }

            @Override
            public void onUserNotFound() {
                Toast.makeText(getActivity(), "Tài khoản không tìm thấy!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Toast.makeText(getActivity(), "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showBalance(String phoneNumber) {
        // Sử dụng Singleton để truy cập trạng thái hiển thị
        boolean isBalanceShown = BalanceVisibilityManager.getInstance().isBalanceShown();
        BalanceVisibilityManager.getInstance().setBalanceShown(!isBalanceShown); // Toggle state

        if (isBalanceShown) {
            // Lấy số dư thực tế từ Firebase
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(phoneNumber).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    long soDu = document.getLong("soDuVi");
                                    soDuVi.setText("Số dư ví: " + String.format("%,d", soDu) + "đ");
                                }
                            }
                        }
                    });

            showSoduButton.setBackgroundResource(R.drawable.showpass);
            showSoduButton.setContentDescription(getString(R.string.show_balance));
        } else {
            soDuVi.setText("Số dư ví: *****");
            showSoduButton.setBackgroundResource(R.drawable.hidepass);
            showSoduButton.setContentDescription(getString(R.string.hide_balance));
        }

    }
}
