package com.example.net_danong;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;

public class Menu1Fragment extends Fragment {

    // 각각의 Fragment마다 Instance를 반환해 줄 메소드를 생성
    public static Menu1Fragment newInstance() {
        return new Menu1Fragment();
    }



    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // return inflater.inflate(R.layout.fragment_menu1, container, false);

        //View view = inflater.inflate(R.layout.fragment_menu1, container, false);
        View view = inflater.inflate(R.layout.activity_firstpage, container, false);

        //Button btn_search = (Button) view.findViewById(R.id.btn_search);
        EditText edit = (EditText)view.findViewById(R.id.edit_main);

        edit.setOnClickListener(new View.OnClickListener() {
//            Fragment NewFragment;
            @Override
            public void onClick(View v) {
                //Log.i("STATE", "SEARCH BUTTON 실행중");
                // getActivity()로 MainActivity의 replaceFragment를 불러옵니다.
                // 새로 불러올 Fragment의 Instance를 Main으로 전달
                ((MainActivity)getActivity()).replaceSearchFrag(Menu1Fragment.newInstance());

                //Log.i("STATE", "fragment 변경완료");
            }
        });

        return view;
    }
}