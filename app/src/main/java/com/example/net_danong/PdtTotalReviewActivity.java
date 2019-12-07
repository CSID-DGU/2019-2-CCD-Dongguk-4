package com.example.net_danong;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Transaction;


public class PdtTotalReviewActivity extends AppCompatActivity implements
        View.OnClickListener,
        ReviewFragment.ReviewListener{

    public static final String KEY_PRODUCT_ID = "key_product_id";


    private RecyclerView mReviewRecycler;
    private ViewGroup mEmptyView;
    private ReviewFragment mReviewFragment;
    private ReviewAdapter mReviewAdapter;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private static DocumentReference mProductRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String productId = getDocumentId();

        setContentView(R.layout.layout_total_review);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mEmptyView = findViewById(R.id.view_empty_total_reviews);
        mReviewRecycler= findViewById(R.id.recycler_total_reviews);

        // Get product Id from extras
        if (productId == null) {
            throw new IllegalArgumentException("Must pass extra " + KEY_PRODUCT_ID);
        }

        // Initialize Firestore
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);
        mProductRef = mFirestore.collection("products").document(productId);
        // Get review
        Query reviewQuery = mProductRef
                .collection("reviews");

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_show_review_dialog:
                onAddReviewClicked(v);
                break;
        }
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

    private Task<Void> addReview(final DocumentReference productRef,
                                 final ReviewModel review) {

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

    private String getDocumentId() {
        return getIntent().getStringExtra(KEY_PRODUCT_ID);
    }

    //리뷰추가
    public void onAddReviewClicked(View view) {
        if (mAuth.getCurrentUser() == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, LoginFragment.newInstance()).commitAllowingStateLoss();
        }
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
    public void onReview(ReviewModel review) {
        // In a transaction, add the new rating and update the aggregate totals
        addReview(mProductRef, review)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Hide keyboard and scroll to top
                        hideKeyboard();
                        mReviewRecycler.smoothScrollToPosition(0);
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideKeyboard();
                        Snackbar.make(findViewById(android.R.id.content), "Failed to add rating",
                                Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

}
