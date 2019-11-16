package com.example.net_danong;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

public class SearchFragment  extends Fragment {

    //내부 전환 하려면 newInstance 필수
    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    //SearchBar관련
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.search_fragment, container, false);

        Button btn_search = (Button) view.findViewById(R.id.btn_pdtResult);
        EditText edit_pdtSearch = (EditText) view.findViewById(R.id.edit_pdtSearch);

        //검색 텍스트 초기화
        edit_pdtSearch.setText("");
        String pdtText = edit_pdtSearch.getText().toString();

        btn_search.setOnClickListener(new View.OnClickListener() {
            //            Fragment NewFragment;
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).searchQuery("토마토");
            }
        });

        edit_pdtSearch.requestFocus();
        edit_pdtSearch.setCursorVisible(true);

        return view;

        /*해야하는 것
        1. edit_pdtSearch 사용자 입력값 string 변수로 받아오고 이거 searchQuery랑 연결시키기 (동적으로 뭔가 이벤트 취해야 하는ㄴ둣,,)
        2. Log에 찍히는 대로 ProductList.get("pdtName");를 string 변수,,배열?에 저장하기 ..아마 ArrayList인가 이용하면 될 듯
        3. array어쩌고랑 search_frag.xml이랑 연동해서 ListView에 구현 ??
        3-1. ListView 구현하거나, 다른 activity로 데이터 넘기는 원리 이용해서 Map에 값 넘겨주기..?ㅠㅠㅠㅠ
        4. 콜렉션그룹(하위 콜렉션) 검색 시, 상위 콜렉션 (user) 필드 값도 받아오는 방법 서치 ..
        5. 상위 콜렉션 필드값도 map/array로 받은 후에 ListView / MapActivity+MapFragment랑 연동
        6. 아마도? 복수 색인 index 정의 및 firestore 규칙 재정의
        8. 주석 및 코드 정리

        1번 하고, 4번 해서 일단 상품이름 입력 하면 상품+사용자 정보 데이터 값 가져오는 거 구현하기..
        어차피 지도로 데이터 넘기는게 목적이니깐 1>4>3-1 순으로 먼저 구현하기.!!!!!
        */

    }

}
