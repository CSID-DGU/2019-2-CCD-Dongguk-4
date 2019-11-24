package com.example.net_danong;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.RequestManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class Adapter extends PagerAdapter {

    private List<Model> models;
    private LayoutInflater layoutInflater;
    private Context context;
    private RequestManager mRequestManager;

    public Adapter(List<Model> models, Context context, RequestManager requestManager) {
        this.models = models;
        this.context = context;
        this.mRequestManager = requestManager;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item, container, false);
        final ImageView imageView;
        TextView title, desc;
        imageView = view.findViewById(R.id.image);
        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.desc);

        //클라우드에서 imageview에 이미지 불러오기 (adapter용)
        models.get(position).getImage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {@Override
        public void onComplete(@NonNull Task<Uri> task) {
            if (task.isSuccessful()) {
                // Glide 이용하여 이미지뷰에 로딩
                mRequestManager.load(task.getResult()).into(imageView);

            } else {
                // URL을 가져오지 못하면 토스트 메세지
                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        });

        title.setText(models.get(position).getTitle());
        desc.setText(models.get(position).getDesc());

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
