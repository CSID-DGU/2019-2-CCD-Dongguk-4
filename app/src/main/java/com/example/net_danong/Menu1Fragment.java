package com.example.net_danong;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

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
        View view = inflater.inflate(R.layout.fragment_firstpage, container, false);

        Button btn_search = (Button) view.findViewById(R.id.btn_search);
        final EditText edit = (EditText)view.findViewById(R.id.edit_main);

        btn_search.setOnClickListener(new View.OnClickListener() {
            //            Fragment NewFragment;
            @Override
            public void onClick(View v) {
                // fragment search_frag로 전환
                // ((MainActivity)getActivity()).replaceSearchFrag(Menu1Fragment.newInstance());

                String search = edit.getText().toString();

                MapsFragment mapsFragment = new MapsFragment();

                Bundle mapbundle = new Bundle();
                mapbundle.putString("productName",search);
                mapsFragment.setArguments(mapbundle);


//                ((MainActivity)getActivity()).searchQ(search);
            }
        });

        return view;
    }
}