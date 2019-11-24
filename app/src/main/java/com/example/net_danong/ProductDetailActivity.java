/*
package com.example.net_danong;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProductDetailActivity extends AppCompatActivity {
    RecyclerView mProductRecycler;
    private PullToZoomScrollViewEx scrollView;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadViewForCode();
        //리사이클러뷰 초기화

        mProductRecycler = (RecyclerView) findViewById(R.id.recycler_productDetail);
        mAdapter = new ProductDetailAdapter(this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    mProductRecycler.setVisibility(View.GONE);
                } else {
                    mProductRecycler.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(getActivity().findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };
        mProductRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mProductRecycler.setAdapter(mAdapter);
        scrollView = findViewById(R.id.scroll_view);
        scrollView.getPullRootView().findViewById(R.id.btn_goReview).setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                    Toast mToast = Toast.makeText(context,"리뷰모음 이동", Toast.LENGTH_SHORT);
                mToast.show();
            }
                }
        );
        scrollView.getPullRootView().findViewById(R.id.btn_goPrvider).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast mToast = Toast.makeText(context,"판매자 상점 이동", Toast.LENGTH_SHORT);
                mToast.show();
            }
        });

        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        scrollView.setHeaderLayoutParams(localObject);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void loadViewForCode() {
        PullToZoomScrollViewEx scrollView = findViewById(R.id.scroll_view);
        View zoomView = LayoutInflater.from(this).inflate(R.layout.item_zoom_view, null, false);
        View productView = LayoutInflater.from(this).inflate(R.layout.item_product_view, null, false);

        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(productView);
    }
}*/
