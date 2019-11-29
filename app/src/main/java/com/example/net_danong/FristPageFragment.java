package com.example.net_danong;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FristPageFragment extends Fragment {


    public static FristPageFragment newInstance() {
        return new FristPageFragment();
    }

    ViewPager viewPager;
    Adapter adapter;
    List<Model> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    FirebaseStorage storage;
    RequestManager mRequestManager;
    StorageReference storageRef;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storage = FirebaseStorage.getInstance();
        mRequestManager = Glide.with(this);
        storageRef = storage.getReference();
        StorageReference image1 = storageRef.child("images/lettuce.jpeg");
        StorageReference image2 = storageRef.child("images/grape.jpeg");
        StorageReference image3 = storageRef.child("images/orange.jpeg");


        models = new ArrayList<>();
        models.add(new Model(image1, "상추", "싱싱한 상추"));
        models.add(new Model(image2, "포도", "맛있는 포도"));
        models.add(new Model(image3, "오렌지", "상큼한 오렌지"));


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_firstpage, container, false);

        adapter = new Adapter(models, getContext(), mRequestManager);
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);

        Button btn_search = (Button) view.findViewById(R.id.btn_search);
        EditText editText_main = (EditText) view.findViewById(R.id.edit_main);
//        editText_main.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), SearchFragment.class);
//                startActivity(intent);
//            }
//        });

        //우선 SearchFrag, 다른 화면으로 연결하는거 말고 버튼 클릭시 바로 결과 연동되도록..
        btn_search.setOnClickListener(new View.OnClickListener() {
            //            Fragment NewFragment;
            @Override
            public void onClick(View v) {
                // fragment search_frag로 전환
                // ((MainActivity)getActivity()).replaceSearchFrag(Menu1Fragment.newInstance());
                String search = editText_main.getText().toString();
                ((MainActivity)getActivity()).searchWord(search);
            }
        });

        Integer[] colors_temp = {
                getResources().getColor(R.color.white),

        };

        colors = colors_temp;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < (adapter.getCount() - 1) && position < (colors.length - 1)) {
                    viewPager.setBackgroundColor(
                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                } else {
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
        return view;

    }
}
