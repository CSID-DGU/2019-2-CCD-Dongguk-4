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
import android.widget.SearchView;

public class SearchFragment  extends Fragment {

    //내부 전환 하려면 newInstance 필수
    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    //SearchBar관련
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

}
