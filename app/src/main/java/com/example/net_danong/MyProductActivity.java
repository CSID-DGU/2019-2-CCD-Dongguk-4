package com.example.net_danong;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;


public class MyProductActivity extends AppCompatActivity implements  ProductListAdapter.OnProductSelectedListener {
    Context context;

    private RecyclerView mProductRecycler;
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private ProductListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_total_product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProductRecycler = findViewById(R.id.recycler_total_products);

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);
        // Initialize Firestore and the main RecyclerView
        initFirestore();

        mAdapter = new ProductListAdapter(mQuery, this) {
            @Override
            protected void onDataChanged() {
                if (getItemCount() == 0) {
                    mProductRecycler.setVisibility(View.GONE);
                } else {
                    mProductRecycler.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };
        mProductRecycler.setLayoutManager(new LinearLayoutManager(this));
        mProductRecycler.setAdapter(mAdapter);
    }

    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();

        mQuery = mFirestore.collectionGroup("products")
                .whereEqualTo("userUid", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .orderBy("avgRating", Query.Direction.DESCENDING);
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

    @Override
    public void OnProductSelected(DocumentSnapshot product) {
        // Go to the details page for the selected restaurant
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.KEY_PRODUCT_ID, product.getId());
        startActivity(intent);
    }
}
