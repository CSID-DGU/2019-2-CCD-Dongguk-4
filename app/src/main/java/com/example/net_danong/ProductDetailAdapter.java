/*
package com.example.net_danong;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ProductDetailAdapter extends FirestoreAdapter<ProductDetailAdapter.ViewHolder> {
    public interface OnProductDetailSelectedListener {

        void OnProductDetailSelected(DocumentSnapshot product);

    }
    private ProductDetailAdapter.OnProductDetailSelectedListener mListener;

    public ProductDetailAdapter(Query query, ProductDetailAdapter.OnProductDetailSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView userProfile;
        private TextView userId;
        private TextView userContexts;
        private MaterialRatingBar pdtRating;
        private TextView pdtRatingNum;

        public ViewHolder(View itemView) {
            super(itemView) ;
            userProfile = itemView.findViewById(R.id.img_userProfile);
            userId= itemView.findViewById(R.id.txt_userId);
            userContexts= itemView.findViewById(R.id.txt_userContexts);
            pdtRating= itemView.findViewById(R.id.rating_pdtRating);
            pdtRatingNum= itemView.findViewById(R.id.txt_pdtRating);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnProductDetailSelectedListener mListener) {

            Review review= snapshot.toObject(Review.class);
            Resources resources = itemView.getResources();

            // Load image
            Glide.with(userProfile.getContext())
                    .load(review.getPhoto())
                    .into(userProfile);

            idView.setText(product.getPublisher());
            nameView.setText(product.getProduct());
            ratingBar.setRating((float) product.getAvgRating());
            regionView.setText(product.getLocation());
            categoryView.setText(product.getCategory());
            numRatingsView.setText(resources.getString(R.string.fmt_num_ratings,
                    product.getNumRatings()));
            priceView.setText(product.getPrice());

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

    // 생성자에서 데이터 리스트 객체를 전달받음.
    ProductDetailAdapter(ArrayList<String> list) {
        mData = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public ProductDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.item_detailreview, parent, false) ;
        ProductDetailAdapter.ViewHolder vh = new ProductDetailAdapter.ViiewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(ProductDetailAdapter.ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}*/
