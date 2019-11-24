package com.example.net_danong;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SearchFragment  extends Fragment {
    //내부 전환 (menu1-search간) newInstance 필수
    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    //SearchBar관련 (일단X)
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.search_fragment, container, false);

        //search_fragment.xml 내 view 변수들과 연결
        Button btn_search = (Button) view.findViewById(R.id.btn_pdtResult);
        final EditText edit_pdtSearch = (EditText) view.findViewById(R.id.edit_pdtSearch);
        //final TextView resultView = (TextView) view.findViewById(R.id.resultView);
        //ListView pdt_list_view = (ListView) view.findViewById(R.id.pdt_list_view);

        //검색 텍스트 초기화 (필요 없는 듯,,)
        //edit_pdtSearch.setText("");
        //String pdtText = edit_pdtSearch.getText().toString();

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //resultView.setText(edit_pdtSearch.getText().toString());
                String search = edit_pdtSearch.getText().toString();
                ((MainActivity)getActivity()).searchQ(search);

            }
        });

        return view;

        /*해야하는 것
        4. 콜렉션그룹(하위 콜렉션) 검색 시, 상위 콜렉션 (user) 필드 값도 받아오는 방법 서치 ..
        5. 상위 콜렉션 필드값도 map/array로 받은 후에 ListView / MapActivity+MapFragment랑 연동
        6. 아마도? 복수 색인 index 정의 및 firestore 규칙 재정의
        7. 지도 액티비티, 화면이랑 데이터 연결 후 데이터 변환 작업
        8. 주석 및 코드 정리
        9. Search_xml파일 레이아웃 정리 (텍스트 자동완성,, 뭐 이런거..ㅠㅠ)
        */



    }

}
