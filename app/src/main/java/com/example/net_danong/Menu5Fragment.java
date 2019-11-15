package com.example.net_danong;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

public class Menu5Fragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_login, container, false);

//        //
//        View view=null;//Fragment가 보여줄 View 객체를 참조할 참조변수
//        //View 객체로 생성
//        view= inflater.inflate(R.layout.activity_login, null);
//        view= inflater.inflate(R.layout.fragment_menu5, null);
//        //생성된 View 객체를 리턴
//        return view;

    }
}