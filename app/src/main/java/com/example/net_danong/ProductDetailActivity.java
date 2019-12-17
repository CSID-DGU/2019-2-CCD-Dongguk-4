package com.example.net_danong;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Transaction;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ProductDetailActivity extends AppCompatActivity implements
        View.OnClickListener,
        EventListener<DocumentSnapshot>,
        ReviewFragment.ReviewListener{

        private PullToZoomScrollViewEx scrollView;
        private static final String TAG = "ProductDetail";

        public static final String KEY_PRODUCT_ID = "key_product_id";

        private ImageView mPdtImageView, mProviderProfileView;
        private TextView  mTitleView, mNameView, mCategoryView, mPriceView
                , mContentsView, mProviderIdView, mLocationView;
        private Button goProvider, goReview, btn_chat;

        private MaterialRatingBar mRatingIndicator;
        private TextView mNumRatingsView;
        private RecyclerView mReviewRecycler;
        private ViewGroup mEmptyView;

        private ReviewFragment mReviewFragment;
        private FirebaseAuth mAuth;
        private FirebaseFirestore mFirestore;
    private static DocumentReference mProductRef;
        private ListenerRegistration mProductRegistration;

        private ReviewAdapter mReviewAdapter;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            String productId = getDocumentId();
            //액션바 이름 설정
            ActionBar ab = getSupportActionBar();
            ab.setTitle("");
            setContentView(R.layout.activity_product_detail);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            loadViewForCode();
            scrollView = findViewById(R.id.scroll_view);
            mFirestore = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();

            DisplayMetrics localDisplayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
            int mScreenWidth = localDisplayMetrics.widthPixels;
            LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
            scrollView.setHeaderLayoutParams(localObject);

            mPdtImageView = findViewById(R.id.img_productZoom);
            mTitleView = findViewById(R.id.txt_pdtTitle);
            mNameView = findViewById(R.id.txt_pdtName);
            mCategoryView = findViewById(R.id.txt_pdtCategory);
            mPriceView = findViewById(R.id.txt_pdtPrice);
            mLocationView = findViewById(R.id.txt_pdtLocation);
            mContentsView = findViewById(R.id.txt_pdtContexts);
            mProviderProfileView = findViewById(R.id.img_providerProfile);
            mProviderIdView= findViewById(R.id.txt_provider);

            mEmptyView = findViewById(R.id.view_empty_ratings);
            mReviewRecycler= findViewById(R.id.recycler_reviews);

            mRatingIndicator = findViewById(R.id.product_rating);
            mNumRatingsView = findViewById(R.id.product_num_ratings);

            findViewById(R.id.btn_goProvider).setOnClickListener(this);
            findViewById(R.id.btn_goReview).setOnClickListener(this);
            findViewById(R.id.fab_show_review_dialog).setOnClickListener(this);

            //채팅하기 버튼
            btn_chat = findViewById(R.id.chat_button);


            // Get restaurant ID from extras
            if (productId == null) {
                throw new IllegalArgumentException("Must pass extra " + KEY_PRODUCT_ID);
            }

           // Initialize Firestore
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setTimestampsInSnapshotsEnabled(true)
                    .build();
            mFirestore.setFirestoreSettings(settings);
            mProductRef = mFirestore.collection("products").document(productId);
            // 10개만 불러오기
            Query reviewQuery = mProductRef
                    .collection("reviews")
                    .limit(10);

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
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()){
                case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                        finish();
                        break;
                }
            }
            return super.onOptionsItemSelected(item);
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

                case R.id.fab_show_review_dialog:
                    onAddReviewClicked(v);
                    break;
                case R.id.btn_goProvider:
/*                    Intent intent = new Intent(ProductDetailActivity.this, 판매자마켓.class);
                    startActivity(intent);*/
                case R.id.btn_goReview:
                    Intent intent = new Intent(ProductDetailActivity.this, PdtTotalReviewActivity.class);
                    intent.putExtra(PdtTotalReviewActivity.KEY_PRODUCT_ID, getDocumentId());
                    startActivity(intent);
            }
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

        @Override
        public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
            if (e != null) {
                Log.w(TAG, "product:onEvent", e);
                return;
            }
            onProductLoaded(snapshot.toObject(ProductWriteInfo.class));
        }

/*
    static String uid;
*/
        private void onProductLoaded(ProductWriteInfo product) {
            mProviderIdView.setText(product.getPublisher());
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


            // user 에서 image 불러오기
            DocumentReference userDocRef = mFirestore.collection("users").document(product.getUserUid());
            userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            UserModel user = document.toObject(UserModel.class);
                            Glide.with(mPdtImageView.getContext())
                                    .load(user.getPhotoURL())
                                    .into(mProviderProfileView);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
            btn_chat.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), MessageActivity.class);
                intent.putExtra("destinationUid",product.getUserUid());
                ActivityOptions activityOptions = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.fromright,R.anim.toleft);
                    startActivity(intent,activityOptions.toBundle());
                }
            });


        }

        private String getDocumentId() {
                return getIntent().getStringExtra(KEY_PRODUCT_ID);
        }

        public void onBackArrowClicked(View view) {
            onBackPressed();
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
                            Log.d(TAG, "ReviewModel added");

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
