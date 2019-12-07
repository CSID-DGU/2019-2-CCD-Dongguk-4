package com.example.net_danong;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;


public class MyReviewActivity extends AppCompatActivity {

    private RecyclerView mReviewRecycler;
    private ViewGroup mEmptyView;
    private ReviewAdapter mReviewAdapter;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_total_review);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mEmptyView = findViewById(R.id.view_empty_total_reviews);
        mReviewRecycler= findViewById(R.id.recycler_total_reviews);

        // Initialize Firestore
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);

        // 내 uid랑 같은 review 출력
        Query reviewQuery = mFirestore.collectionGroup("reviews")
                .whereEqualTo("userUId",mAuth.getCurrentUser().getUid());

        // RecyclerView
        mReviewAdapter = new ReviewAdapter(reviewQuery) {
            @Override
            protected void onDataChanged() {
                if (getItemCount() == 0) {
                    mReviewRecycler.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    mReviewRecycler.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
            }
        };

        mReviewRecycler.setLayoutManager(new LinearLayoutManager(this));
        mReviewRecycler.setAdapter(mReviewAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mReviewAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mReviewAdapter.stopListening();
    }

}
