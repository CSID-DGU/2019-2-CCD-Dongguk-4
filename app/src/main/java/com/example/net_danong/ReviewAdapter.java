package com.example.net_danong;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.Query;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ReviewAdapter extends FirestoreAdapter<ReviewAdapter.ViewHolder> {

    public ReviewAdapter(Query query) {
        super(query);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detail_review, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position).toObject(Review.class));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView idView;
        TextView contents;

        MaterialRatingBar ratingBar;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_userProfile);
            idView = itemView.findViewById(R.id.txt_userId);
            contents = itemView.findViewById(R.id.txt_Contents);
            ratingBar = itemView.findViewById(R.id.rating_pdtRating);
            textView = itemView.findViewById(R.id.txt_pdtRating);
        }

        public void bind(Review review) {
            //ImageView glide 로 가져오기 db uid로 user 쿼리, url 긁어오기
            idView.setText(review.getUserName());
            contents.setText(review.getText());
            ratingBar.setRating((float) review.getRating());
            textView.setText(review.getText());
        }
    }

}
