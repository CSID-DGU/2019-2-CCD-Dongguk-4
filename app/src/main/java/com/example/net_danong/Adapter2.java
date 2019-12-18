package com.example.net_danong;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class Adapter2 extends FirestoreAdapter<Adapter2.ViewHolder> {

    public interface OnModelSelectedListener {

        void OnModelSelected(DocumentSnapshot model);

    }

    private Adapter2.OnModelSelectedListener mListener;

    public Adapter2(Query query, Adapter2.OnModelSelectedListener listener) {
        super(query);
        mListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_firstpage_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView productView;
        TextView titleView;
        MaterialRatingBar ratingBar;
        TextView numRatingsView;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.image);
            productView = view.findViewById(R.id.first_product);
            titleView = view.findViewById(R.id.first_title);
            ratingBar = itemView.findViewById(R.id.first_pdtRating);
            numRatingsView = itemView.findViewById(R.id.txt_Rating);
        }
        public void bind(final DocumentSnapshot snapshot,
                         final Adapter2.OnModelSelectedListener listener) {

            ProductWriteInfo product = snapshot.toObject(ProductWriteInfo.class);
            Resources resources = itemView.getResources();

            // Load image
            Glide.with(imageView.getContext())
                    .load(product.getPhotoUrl())
                    .into(imageView);

            titleView.setText(product.getTitle());
            productView.setText(product.getProduct());
            ratingBar.setRating((float) product.getAvgRating());
            numRatingsView.setText(resources.getString(R.string.fmt_num_ratings,
                    product.getNumRatings()));

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.OnModelSelected(snapshot);
                    }
                }
            });
        }

    }

}

