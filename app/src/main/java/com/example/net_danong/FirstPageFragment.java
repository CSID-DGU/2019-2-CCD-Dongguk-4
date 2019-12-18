package com.example.net_danong;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.net_danong.Board.BoardInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FirstPageFragment extends Fragment implements Adapter2.OnModelSelectedListener {

    public static FirstPageFragment newInstance() {
        return new FirstPageFragment();
    }

    private Query mQuery;
    private Adapter2 mAdapter;
    private RecyclerView mModelRecycler;
    List<Model> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    FirebaseStorage storage;
    RequestManager mRequestManager;
    StorageReference storageRef;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storage = FirebaseStorage.getInstance();
        mRequestManager = Glide.with(this);
        storageRef = storage.getReference();
        models = new ArrayList<>();

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_firstpage, container, false);
        mModelRecycler = view.findViewById(R.id.viewPager);
        FirebaseFirestore.setLoggingEnabled(true);
        initFirestore();


        mModelRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mModelRecycler.setPadding(130, 0, 130, 0);
        mAdapter = new Adapter2(mQuery, this);
        mModelRecycler.setAdapter(mAdapter);

        Button btn_search = view.findViewById(R.id.btn_search);
        EditText editText_main = view.findViewById(R.id.edit_main);
//        editText_main.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), SearchFragment.class);
//                startActivity(intent);
//            }
//        });

        //우선 SearchFrag, 다른 화면으로 연결하는거 말고 버튼 클릭시 바로 결과 연동되도록..
        btn_search.setOnClickListener(new View.OnClickListener() {
            //            Fragment NewFragment;
            @Override
            public void onClick(View v) {
                // fragment search_frag로 전환
                // ((MainActivity)getActivity()).replaceSearchFrag(Menu1Fragment.newInstance());
                String search = editText_main.getText().toString();
                ((MainActivity) getActivity()).searchWord(search);
            }
        });

        Integer[] colors_temp = {
                getResources().getColor(R.color.white),

        };

        colors = colors_temp;
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }
    public void OnModelSelected(DocumentSnapshot product) {
        // Go to the details page for the selected restaurant
        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.KEY_PRODUCT_ID, product.getId());
        startActivity(intent);
    }

    private void initFirestore() {
        mQuery =  FirebaseFirestore.getInstance().collectionGroup("products")
                .orderBy("avgRating", Query.Direction.DESCENDING)
                .limit(10);
    }
}
