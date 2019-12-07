package com.example.net_danong;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;



public class PeopleFragment extends Fragment {
    public static PeopleFragment newInstance() {
        return new PeopleFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatlist, container, false);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            ((MainActivity)getActivity()).replaceLoginFrag(LoginFragment.newInstance());
        } else{
            Toast.makeText(getActivity(), "E-mail:" + FirebaseAuth.getInstance().getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
        }
        RecyclerView recyclerView = view.findViewById(R.id.rv_chatlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new PeopleFragmentRecyclerViewAdapter(getContext()));

        return view;
    }



}
