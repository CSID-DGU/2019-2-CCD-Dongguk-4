package com.example.net_danong;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Transaction;

import java.util.zip.Inflater;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ProductDetailActivity extends AppCompatActivity implements
        View.OnClickListener,
        EventListener<DocumentSnapshot>,
        ReviewFragment.ReviewListener{

        private PullToZoomScrollViewEx scrollView;
        private static final String TAG = "ProductDetail";

        public static final String KEY_PRODUCT_ID = "key_product_id";

        private ImageView mPdtImageView, mProviderProfileView;
        private TextView mViewNumView, mTitleView, mLoveNumView, mNameView, mCategoryView, mPriceView
                , mContentsView, mProviderIdView, mLocationView;
        private Button goProvider, goReview;

        private FieldPath fieldPath;
        private MaterialRatingBar mRatingIndicator;
        private TextView mNumRatingsView;
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
            setContentView(R.layout.activity_product_detail);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            loadViewForCode();
            scrollView = findViewById(R.id.scroll_view);

            DisplayMetrics localDisplayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
            int mScreenWidth = localDisplayMetrics.widthPixels;
            LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
            scrollView.setHeaderLayoutParams(localObject);

            mPdtImageView = findViewById(R.id.img_productZoom);
            mViewNumView = findViewById(R.id.txt_pdtViewNum);
            mTitleView = findViewById(R.id.txt_pdtTitle);
            mLoveNumView = findViewById(R.id.txt_pdtViewNum);
            mNameView = findViewById(R.id.txt_pdtName);
            mCategoryView = findViewById(R.id.txt_pdtCategory);
            mPriceView = findViewById(R.id.txt_pdtPrice);
            mLocationView = findViewById(R.id.txt_pdtLocation);
            mContentsView = findViewById(R.id.txt_pdtContexts);
            mProviderProfileView = findViewById(R.id.img_providerProfile);
            mProviderIdView= findViewById(R.id.txt_provider);

            mEmptyView = findViewById(R.id.view_empty_ratings);


            mRatingIndicator = findViewById(R.id.product_rating);
            mNumRatingsView = findViewById(R.id.product_num_ratings);
            mReviewRecycler= findViewById(R.id.recycler_reviews);

            findViewById(R.id.btn_goProvider).setOnClickListener(this);
            findViewById(R.id.btn_goReview).setOnClickListener(this);
            findViewById(R.id.product_button_back).setOnClickListener(this);
            findViewById(R.id.fab_show_rating_dialog).setOnClickListener(this);

            // Get restaurant ID from extras
            String productId = getDocumentId();
            if (productId == null) {
                throw new IllegalArgumentException("Must pass extra " + KEY_PRODUCT_ID);
            }

           // Initialize Firestore
            mFirestore = FirebaseFirestore.getInstance();
            mProductRef = mFirestore.collection("products").document(productId);
            // Get ratings
            Query ratingsQuery = mFirestore
                    .collection("reviews")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(50);

            // RecyclerView
            mReviewAdapter = new ReviewAdapter(ratingsQuery) {
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

        private void loadViewForCode() {
            PullToZoomScrollViewEx scrollView = findViewById(R.id.scroll_view);
            View zoomView = LayoutInflater.from(this).inflate(R.layout.item_zoom_view, null, false);
            View contentView = LayoutInflater.from(this).inflate(R.layout.item_product_view, null, false);
            scrollView.setZoomView(zoomView);
            scrollView.setScrollContentView(contentView);

        }

        @Override
        public void onStart() {
            super.onStart();

            mReviewAdapter.startListening();
            mProductRegistration = mProductRef.addSnapshotListener(this);
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
                case R.id.btn_goProvider:
/*                    Intent intent = new Intent(ProductDetailActivity.this, 판매자마켓.class);
                    startActivity(intent);*/
                case R.id.btn_goReview:
                    Intent intent = new Intent(ProductDetailActivity.this, ReviewActivity.class);
                    startActivity(intent);
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
            onProductLoaded(snapshot.toObject(ProductWriteInfo.class));
        }

        private void onProductLoaded(ProductWriteInfo product) {
            mTitleView.setText(product.getTitle());
            mNameView.setText(product.getProduct());
            mLocationView.setText(product.getLocation());
            mCategoryView.setText(product.getCategory());
            mPriceView.setText(product.getPrice());
            mContentsView.setText(product.getContents());
            mRatingIndicator.setRating((float) product.getAvgRating());
            mNumRatingsView.setText(getString(R.string.fmt_num_ratings, product.getNumRatings()));
            // Background image
            Glide.with(mPdtImageView.getContext())
                    .load(product.getPhotoUrl())
                    .into(mPdtImageView);
            // Profile image

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
