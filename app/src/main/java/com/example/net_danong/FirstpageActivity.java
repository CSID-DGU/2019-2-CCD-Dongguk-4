package com.example.net_danong;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FirstpageActivity extends AppCompatActivity {

    ViewPager viewPager;
    Adapter adapter;
    List<Model> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        RequestManager mRequestManager= Glide.with(this);

        StorageReference storageRef = storage.getReference();
        StorageReference image1 = storageRef.child("images/lettuce.jpg");
        StorageReference image2 = storageRef.child("images/grape.jpg");
        StorageReference image3 = storageRef.child("images/orange.jpg");


        models = new ArrayList<>();
        models.add(new Model(image1, "상추", "싱싱한 상추"));
        models.add(new Model(image2, "포도", "맛있는 포도"));
        models.add(new Model(image3, "오렌지", "상큼한 오렌지"));

        adapter = new Adapter(models, this, mRequestManager);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);

        EditText editText_main = (EditText) findViewById(R.id.edit_main);
        editText_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchFragment.class);
                startActivity(intent);
            }
        });

        Integer[] colors_temp = {
                getResources().getColor(R.color.white),

        };

        colors = colors_temp;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < (adapter.getCount() - 1) && position < (colors.length -1)) {
                    viewPager.setBackgroundColor(
                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                }else {
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
