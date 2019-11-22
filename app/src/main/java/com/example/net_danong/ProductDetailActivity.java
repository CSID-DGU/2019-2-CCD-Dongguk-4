package com.example.net_danong;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

    public class ProductDetailActivity extends AppCompatActivity /*implements
            View.OnClickListener,
            EventListener<DocumentSnapshot>*/ {

        private static final String TAG = "ProductDetail";

        public static final String KEY_PRODUCT_UID = "key_product_uid";

        private ImageView mImageView;
        private TextView mNameView;
        private TextView mpriceView;
        private TextView mCategoryView;
        private TextView mPriceView;
        private RecyclerView mRatingsRecycler;

        private FirebaseFirestore mFirestore;
        private DocumentReference mProductRef;
        private ListenerRegistration mProductRegistration;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_product_detail);
/*
            mImageView = findViewById(R.id.restaurant_image);
            mNameView = findViewById(R.id.restaurant_name);
            mRatingIndicator = findViewById(R.id.restaurant_rating);
            mNumRatingsView = findViewById(R.id.restaurant_num_ratings);
            mCityView = findViewById(R.id.restaurant_city);
            mCategoryView = findViewById(R.id.restaurant_category);
            mPriceView = findViewById(R.id.restaurant_price);
            mEmptyView = findViewById(R.id.view_empty_ratings);
            mRatingsRecycler = findViewById(R.id.recycler_ratings);

            findViewById(R.id.restaurant_button_back).setOnClickListener(this);
            findViewById(R.id.fab_show_rating_dialog).setOnClickListener(this);

            // Get restaurant ID from extras
            String restaurantId = getIntent().getExtras().getString(KEY_UID);
            if (restaurantId == null) {
                throw new IllegalArgumentException("Must pass extra " + KEY_UID);
            }

            // Initialize Firestore
            mFirestore = FirebaseFirestore.getInstance();

            // Get reference to the restaurant
            mRestaurantRef = mFirestore.collection("restaurants").document(restaurantId);

            // Get ratings
            Query ratingsQuery = mRestaurantRef
                    .collection("ratings")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(50);

            // RecyclerView
            mRatingAdapter = new RatingAdapter(ratingsQuery) {
                @Override
                protected void onDataChanged() {
                    if (getItemCount() == 0) {
                        mRatingsRecycler.setVisibility(View.GONE);
                        mEmptyView.setVisibility(View.VISIBLE);
                    } else {
                        mRatingsRecycler.setVisibility(View.VISIBLE);
                        mEmptyView.setVisibility(View.GONE);
                    }
                }
            };

            mRatingsRecycler.setLayoutManager(new LinearLayoutManager(this));
            mRatingsRecycler.setAdapter(mRatingAdapter);

            mRatingDialog = new RatingDialogFragment();*/
        }
/*
        @Override
        public void onStart() {
            super.onStart();

            mRatingAdapter.startListening();
            mRestaurantRegistration = mRestaurantRef.addSnapshotListener(this);
        }

        @Override
        public void onStop() {
            super.onStop();

            mRatingAdapter.stopListening();

            if (mRestaurantRegistration != null) {
                mRestaurantRegistration.remove();
                mRestaurantRegistration = null;
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.restaurant_button_back:
                    onBackArrowClicked(v);
                    break;
                case R.id.fab_show_rating_dialog:
                    onAddRatingClicked(v);
                    break;
            }
        }

        private Task<Void> addRating(final DocumentReference restaurantRef, final Rating rating) {
            // TODO(developer): Implement
            return Tasks.forException(new Exception("not yet implemented"));
        }

*
         * Listener for the Restaurant document ({@link #mRestaurantRef}).


        @Override
        public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
            if (e != null) {
                Log.w(TAG, "restaurant:onEvent", e);
                return;
            }

            onRestaurantLoaded(snapshot.toObject(Restaurant.class));
        }

        private void onRestaurantLoaded(Restaurant restaurant) {
            mNameView.setText(restaurant.getName());
            mRatingIndicator.setRating((float) restaurant.getAvgRating());
            mNumRatingsView.setText(getString(R.string.fmt_num_ratings, restaurant.getNumRatings()));
            mCityView.setText(restaurant.getCity());
            mCategoryView.setText(restaurant.getCategory());
            mPriceView.setText(RestaurantUtil.getPriceString(restaurant));

            // Background image
            Glide.with(mImageView.getContext())
                    .load(restaurant.getPhoto())
                    .into(mImageView);
        }

        public void onBackArrowClicked(View view) {
            onBackPressed();
        }

        public void onAddRatingClicked(View view) {
            mRatingDialog.show(getSupportFragmentManager(), RatingDialogFragment.TAG);
        }

        @Override
        public void onRating(Rating rating) {
            // In a transaction, add the new rating and update the aggregate totals
            addRating(mRestaurantRef, rating)
                    .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Rating added");

                            // Hide keyboard and scroll to top
                            hideKeyboard();
                            mRatingsRecycler.smoothScrollToPosition(0);
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

        private void hideKeyboard() {
            View view = getCurrentFocus();
            if (view != null) {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }*/
    }

