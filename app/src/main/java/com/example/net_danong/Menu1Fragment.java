package com.example.net_danong;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class Menu1Fragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu1, container, false);
    }
}