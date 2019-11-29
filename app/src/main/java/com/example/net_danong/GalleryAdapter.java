package com.example.net_danong;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {
    private ArrayList<String> mDataset;
    private Activity activity;

    public static class GalleryViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public GalleryViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GalleryAdapter(Activity activity, ArrayList<String> myDataset) {
        mDataset = myDataset;
        this.activity=activity;
    }

    @NonNull
    @Override
    public GalleryAdapter.GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false);//갤러리 자동으로 보이게
        return new GalleryViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull final GalleryViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent resultIntent = new Intent();
                resultIntent.putExtra("profilePath", mDataset.get(holder.getAdapterPosition()));
                activity.setResult(Activity.RESULT_OK, resultIntent);
                activity.finish();
            }
                                    });

        ImageView imageView = holder.cardView.findViewById(R.id.imageView);
        Glide.with(activity).load(mDataset.get(position)).centerCrop().override(500).into(imageView);
    }//데이터(mDataset)가 하나씩 들어오는 곳

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}