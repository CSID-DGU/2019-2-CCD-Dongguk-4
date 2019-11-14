package com.example.net_danong;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import android.view.View.OnClickListener;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

//검색 관련해서 임시로 추가
public class Menu1Fragment extends Fragment {

    Button btn;
    private static ArrayList<Type> mArrayList = new ArrayList<>();

    //firestore 연동
    private FirebaseFirestore db;
    private static final String TAG = "DocSnippets";
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        db = FirebaseFirestore.getInstance();
//        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//                .setTimestampsInSnapshotsEnabled(true)
//                .build();
//        db.setFirestoreSettings(settings);
//    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu1, container, false);

        btn = (Button)v.findViewById(R.id.button1);
        // btn.setOnClickListener(this);

        return v;
    }

    //검색쿼리
    private void searchDB() {
        db.collection("product")
                .whereEqualTo("pdtName", "토마토")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    //EditText랑 연결. Map?List?에 저장 후 다시 연동,,

    //온클릭 이벤트
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                searchDB();
                break;
        }
    }
}

