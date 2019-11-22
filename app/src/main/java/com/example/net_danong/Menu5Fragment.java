package com.example.net_danong;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Menu5Fragment extends Fragment {
    //내부 전환 newInstance 필수
    public static Menu5Fragment newInstance() {
        return new Menu5Fragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_menu5, container, false);
//        return view;

    }
}