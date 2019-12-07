package com.example.net_danong;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Dialog Fragment containing rating form.
 */
public class ReviewFragment extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "RatingDialog";

    private MaterialRatingBar mRatingBar;
    private EditText mReviewText;

    private static final int PICK_FROM_ALBUM = 10;
    private ImageView mReviewPhoto;
    static Uri imageUri;
    static String imageUrl2;

    interface ReviewListener {

        void onReview(Review review);

    }

    private ReviewListener mReviewListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_review, container, false);
        mRatingBar = v.findViewById(R.id.product_form_rating);
        mReviewText = v.findViewById(R.id.product_form_text);

        v.findViewById(R.id.product_form_button).setOnClickListener(this);
        v.findViewById(R.id.product_form_cancel).setOnClickListener(this);
        mReviewPhoto = v.findViewById(R.id.iv_rvPhoto);
        mReviewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

        return v;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FROM_ALBUM && resultCode == getActivity().RESULT_OK) {
            mReviewPhoto.setImageURI(data.getData()); // 가운데 뷰를 바꿈
            imageUri = data.getData();// 이미지 경로 원본
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ReviewListener) {
            mReviewListener = (ReviewListener) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.product_form_button:
                onSubmitClicked(v);
                break;
            case R.id.product_form_cancel:
                onCancelClicked(v);
                break;
        }
    }

    public void onSubmitClicked(View view) {
        imageUrl2 = imageUri.toString();
        Review review = new Review(
                FirebaseAuth.getInstance().getCurrentUser(),
                mRatingBar.getRating(),
                mReviewText.getText().toString(),
                imageUrl2);
        review.setRvPhoto(imageUrl2);

        if (mReviewListener!= null) {
            mReviewListener.onReview(review);
        }
        dismiss();
    }

    public void onCancelClicked(View view) {
        dismiss();
    }
}
