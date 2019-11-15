package com.example.net_danong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//navBar 관련 항목
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    //로그인 상태변화 확인
    private FirebaseAuth.AuthStateListener mAuthListener;

    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();
    // 5개의 메뉴에 들어갈 Fragment들
    private Menu1Fragment menu1Fragment = new Menu1Fragment();
    private Menu2Fragment menu2Fragment = new Menu2Fragment();
    private Menu3Fragment menu3Fragment = new Menu3Fragment();
    private Menu4Fragment menu4Fragment = new Menu4Fragment();
    private Menu5Fragment menu5Fragment = new Menu5Fragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, menu1Fragment).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

        db = FirebaseFirestore.getInstance();
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(intent);
/* 테스트데이터 추가 11/06 5:04 am
        addNewUsers("ps5f1868", Arrays.asList("충북 영동군 영동읍 상가 1길 6-7", "36.1823534", "127.8632923"), "1");
        addNewUsers("tbfxdn80", Arrays.asList("전북 임실군 덕치면 일중리", "35.4928087", "127.1220321"), "2");
        addNewUsers("dol4vpz4", Arrays.asList("경기 하남시 덕풍서로65", "37.5531092", "127.2009618"), "3");
        addNewUsers("ylggw0t7", Arrays.asList("충북 충주시 노은면 가신리765-8", "37.0624677", "127.7119894"), "4");
        addNewUsers("88vi9b1c", Arrays.asList("경북 영천시 북안면 북안서당길 85-42", "35.8979102", "128.9985232"), "5");
        addNewUsers("cpw032z1", Arrays.asList("경북 안동시 은행나무로106-6", "36.5668203", "128.6928981"), "6");
        addNewUsers("tu5hp7qc", Arrays.asList("인천 강화군 하점면 창후로 20", "37.7650304", "126.3776273"), "7");
        addNewUsers("tlaamln8", Arrays.asList("전북 익산시 망성면 여강로 1173번지", "36.1416674", "127.0167843"), "8");
        addNewUsers("ps5f1870", Arrays.asList("충남 부여군 남면 539", "36.2140921", "126.7762129"), "9");
        addNewUsers("jc9avahj", Arrays.asList("충남 부여군 만지동로 182-38", "36.194088", "126.8583308"), "10");
        addNewProduct("사과", "2019-10-20", "1");
        addNewProduct("포도", "2019-10-21", "2");
        addNewProduct("사과", "2019-09-30", "3");
        addNewProduct("포도", "2019-10-30", "4");
        addNewProduct("상추", "2019-10-24", "5");
        addNewProduct("깻잎", "2019-10-11", "6");
        addNewProduct("포도", "2019-10-16", "7");
        addNewProduct("깻잎", "2019-10-05", "8");
        addNewProduct("토마토", "2019-10-31", "9");
        addNewProduct("토마토", "2019-10-28", "10");
*/


    }

    //새로운 유저 등록, 추후에 가입/로그인 구현 후 document 이름  docNum -> uid로 변경
    private void addNewUsers(String userId, List<String> userAdress, String docNum) {
        newUser newUser = new newUser(userId, userAdress);
        db.collection("users").document(docNum).set(newUser);
    }

    //제품 등록, 추후에 상품 등록 구현 후 유저가 상품을 등록했을 때 함수 실행
    private void addNewProduct(String pdtName, String pdtEnrollDate, String docNum) {
        Map<String, Object> newProduct = new HashMap<>();
        newProduct.put("pdtName", pdtName);
        newProduct.put("pdtEnrollDate", pdtEnrollDate);
        //docNum -> 해당 유저를 찾아 수정,
        db.collection("users").document(docNum)
          .collection("product")
                .add(newProduct)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("", "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("", "Error adding document", e);
                    }
                });
    }

    // navBar 클래스 navBar 클릭 시 이동
    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.navigation_menu1: {
                    transaction.replace(R.id.frame_layout, menu1Fragment).commitAllowingStateLoss();
                    break;
                }
                case R.id.navigation_menu2: {
                    transaction.replace(R.id.frame_layout, menu2Fragment).commitAllowingStateLoss();
                    break;
                }
                case R.id.navigation_menu3: {
                    transaction.replace(R.id.frame_layout, menu3Fragment).commitAllowingStateLoss();
                    break;
                }
                case R.id.navigation_menu4: {
                    transaction.replace(R.id.frame_layout, menu4Fragment).commitAllowingStateLoss();
                    break;
                }
                case R.id.navigation_menu5: {
                    transaction.replace(R.id.frame_layout, menu5Fragment).commitAllowingStateLoss();
                    break;
                }
            }
            return true;
        }
    }
}
