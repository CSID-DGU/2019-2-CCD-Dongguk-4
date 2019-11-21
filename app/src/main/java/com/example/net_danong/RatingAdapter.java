package com.example.net_danong;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.Query;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;


public class RatingAdapter extends FirestoreAdapter<RatingAdapter.ViewHolder> {

    public RatingAdapter(Query query) {
        super(query);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rating, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position).toObject(Rating.class));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;
        MaterialRatingBar ratingBar;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.rating_userId);
            ratingBar = itemView.findViewById(R.id.rating_pdtRating);
            textView = itemView.findViewById(R.id.rating_pdtText);
        }

        public void bind(Rating rating) {
            nameView.setText(rating.getUserName());
            ratingBar.setRating((float) rating.getRating());
            textView.setText(rating.getText());
        }
    }

}
