package com.example.do_an.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.do_an.R;
import com.example.do_an.ui.EnterPhoneNumberActivity;
import com.example.do_an.ui.LoginAndSecurityActivity;
import com.example.do_an.ui.PersonalPageActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingFragment extends Fragment {
    Button btnLogout;
    TextView userName, phoneNumberText;
    LinearLayout userPageLayout, loginSecurityLayout;
    private FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        firestore = FirebaseFirestore.getInstance();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("my_phone", Context.MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString("PHONE_NUMBER", "");
        userName = view.findViewById(R.id.nameUser);
        phoneNumberText = view.findViewById(R.id.phoneNumber);
        phoneNumberText.setText(phoneNumber);
        observeName(phoneNumber).observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String fullName) {
                if (!fullName.equals(""))
                    userName.setText(fullName);
            }
        });
        btnLogout = view.findViewById(R.id.out);
        userPageLayout = view.findViewById(R.id.userPage);
        loginSecurityLayout = view.findViewById(R.id.loginSecurity);
        userPageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PersonalPageActivity.class);
                startActivity(intent);
            }
        });
        loginSecurityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginAndSecurityActivity.class);
                startActivity(intent);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutDialog();
            }
        });
        return view;
    }

    void loadFragment(Fragment newFragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public LiveData<String> observeName(String phoneNumber) {
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
                                fullNameLiveData.postValue("");
                            }
                        } else {
                            fullNameLiveData.postValue("");
                        }
                    }
                });

        return fullNameLiveData;
    }

    public void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có chắc chắn muốn đăng xuất khỏi ứng dụng?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(requireContext(), EnterPhoneNumberActivity.class);
                startActivity(intent);
                Toast.makeText(getContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                requireActivity().finishAffinity();
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
