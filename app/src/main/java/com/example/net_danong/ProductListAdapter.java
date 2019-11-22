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


public class ProductListAdapter extends FirestoreAdapter<ProductListAdapter.ViewHolder> {

    public interface OnProductSelectedListener {

        void OnProductSelected(DocumentSnapshot product);

    }

    private OnProductSelectedListener mListener;

    public ProductListAdapter(Query query, OnProductSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nameView;
        TextView idView;
        MaterialRatingBar ratingBar;
        TextView numRatingsView;
        TextView priceView;
        TextView categoryView;
        TextView regionView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_pdt);
            nameView = itemView.findViewById(R.id.txt_pdtName);
            idView = itemView.findViewById(R.id.txt_userId);
            ratingBar = itemView.findViewById(R.id.rating_pdtRating);
            numRatingsView = itemView.findViewById(R.id.txt_pdtRating);
            priceView = itemView.findViewById(R.id.txt_pdtPrice);
            categoryView = itemView.findViewById(R.id.txt_pdtCategory);
            regionView = itemView.findViewById(R.id.txt_pdtRegion);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnProductSelectedListener listener) {

            Product product = snapshot.toObject(Product.class);
            Resources resources = itemView.getResources();

            // Load image
            Glide.with(imageView.getContext())
                    .load(product.getPhoto())
                    .into(imageView);

            idView.setText(product.getUserName());
            nameView.setText(product.getName());
            ratingBar.setRating((float) product.getAvgRating());
            regionView.setText(product.getRegion());
            categoryView.setText(product.getCategory());
            numRatingsView.setText(resources.getString(R.string.fmt_num_ratings,
                    product.getNumRatings()));
            priceView.setText(ProductUtil.getPriceString(product));

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.OnProductSelected(snapshot);
                    }
                }
            });
        }

    }
}
