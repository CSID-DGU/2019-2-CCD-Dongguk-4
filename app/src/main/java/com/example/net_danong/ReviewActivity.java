package com.example.net_danong;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Transaction;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ReviewActivity extends AppCompatActivity implements
        View.OnClickListener,
        EventListener<DocumentSnapshot>,
        ReviewFragment.ReviewListener{

    private static final String TAG = "ReviewActivity";
    public static final String KEY_PRODUCT_ID = "key_product_id";


    private RecyclerView mReviewRecycler;
    private ViewGroup mEmptyView;

    private ReviewFragment mReviewFragment;

    private FirebaseFirestore mFirestore;
    private static DocumentReference mProductRef;
    private ListenerRegistration mProductRegistration;

    private ReviewAdapter mReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_total_review);

        mReviewRecycler= findViewById(R.id.recycler_totalReviews);
        findViewById(R.id.fab_show_rating_dialog).setOnClickListener(this);

        mEmptyView = findViewById(R.id.view_empty_ratings);

        // Initialize Firestore
        mFirestore = FirebaseFirestore.getInstance();
        mProductRef = mFirestore.collection("products").document();
        // Get ratings
        Query reviewQuery = mProductRef
                .collection("reviews")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(50);

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

        mReviewFragment = new ReviewFragment();
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
        if (mProductRegistration != null) {
            mProductRegistration.remove();
            mProductRegistration = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.product_button_back:
                onBackArrowClicked(v);
                break;
            case R.id.fab_show_rating_dialog:
                onAddRatingClicked(v);
                break;
        }
    }

    private Task<Void> addReview(final DocumentReference productRef,
                                 final Review review) {
        final DocumentReference reviewRef = productRef.collection("reviews")
                .document();

        // In a transaction, add the new rating and update the aggregate totals
        return mFirestore.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction)
                    throws FirebaseFirestoreException {

                ProductWriteInfo product = transaction.get(productRef)
                        .toObject(ProductWriteInfo.class);

                // Compute new number of ratings
                int newNumRatings = product.getNumRatings() + 1;

                // Compute new average rating
                double oldRatingTotal = product.getAvgRating() *
                        product.getNumRatings();
                double newAvgRating = (oldRatingTotal + review.getRating()) /
                        newNumRatings;

                // Set new product info
                product.setNumRatings(newNumRatings);
                product.setAvgRating(newAvgRating);

                // Commit to Firestore
                transaction.set(productRef, product);
                transaction.set(reviewRef, review);

                return null;
            }
        });
    }

    @Override
    public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
        if (e != null) {
            Log.w(TAG, "product:onEvent", e);
            return;
        }
    }

    private String getDocumentId() {
        return getIntent().getStringExtra(KEY_PRODUCT_ID);
    }

    public void onBackArrowClicked(View view) {
        onBackPressed();
    }

    public void onAddRatingClicked(View view) {
        mReviewFragment.show(getSupportFragmentManager(), ReviewFragment.TAG);
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onReview(Review review) {
        // In a transaction, add the new rating and update the aggregate totals
        addReview(mProductRef, review)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Review added");

                        // Hide keyboard and scroll to top
                        hideKeyboard();
                        mReviewRecycler.smoothScrollToPosition(0);
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Add rating failed", e);

                        // Show failure message and hide keyboard
                        hideKeyboard();
                        Snackbar.make(findViewById(android.R.id.content), "Failed to add rating",
                                Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

}
