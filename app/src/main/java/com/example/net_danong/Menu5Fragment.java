package com.example.net_danong;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Menu5Fragment extends Fragment {
    public static Menu5Fragment newInstance() {
        return new Menu5Fragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu5, container, false);

        //로그인 체크
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            ((MainActivity)getActivity()).replaceLoginFrag(LoginFragment.newInstance());
        } else{
            Toast.makeText(getActivity(), "E-mail:" + FirebaseAuth.getInstance().getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
        }

        Button button = (Button) view.findViewById(R.id.LogOut);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               FirebaseAuth.getInstance().signOut();
            }
        });
        return view;
    }
}

//        //
//        View view=null;//Fragment가 보여줄 View 객체를 참조할 참조변수
//        //View 객체로 생성
//        view= inflater.inflate(R.layout.activity_login, null);
//        view= inflater.inflate(R.layout.fragment_menu5, null);
//        //생성된 View 객체를 리턴
//        return view;
