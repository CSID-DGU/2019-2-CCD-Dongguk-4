package com.example.net_danong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//navBar 관련 항목
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    //로그인 상태변화 확인
    private FirebaseAuth.AuthStateListener mAuthListener;
    //firestore TAG
    private static final String TAG = "DocSnippets";

    //필요한 변수들 선언하기
    Map<String, Object> ProductList; //검색+지도에서 사용할 결과값 저장공간
    Map<String, Object> UserList;    //검색+지도에서 사용할 결과값 저장공간2

    // FrameLayout 관련 초기화
    private FragmentManager fragmentManager = getSupportFragmentManager();
    // 5개의 메뉴에 들어갈 Fragment들 (변수명 변경 필요)
    private Menu1Fragment menu1Fragment = new Menu1Fragment();
    private Menu2Fragment menu2Fragment = new Menu2Fragment();
    private Menu3Fragment menu3Fragment = new Menu3Fragment();
    private Menu4Fragment menu4Fragment = new Menu4Fragment();
    private Menu5Fragment menu5Fragment = new Menu5Fragment();
    //추가 기능별 Fragment (아이디비번찾기, 회원가입, 등 기능 및 페이지 관련해서 추가 필요)
    private SearchFragment searchFragment = new SearchFragment();
    private LoginFragment loginFragment = new LoginFragment();
    private JoinFragment JoinFragment = new JoinFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Acitivity_main페이지 초기화면
        setContentView(R.layout.activity_main);

        //Fragment Layout 초기 설정 (menu1화면)
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, menu1Fragment.newInstance()).commitAllowingStateLoss();

        //하단 navigation bar 지정
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

        db = FirebaseFirestore.getInstance();

        //Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        //startActivity(intent);

        /* 테스트데이터 추가 관련 함수들
        addNewUsers("jc9avahj", Arrays.asList("충남 부여군 만지동로 182-38", "36.194088", "126.8583308"), "10");
        addNewProduct("토마토", "2019-10-28", "10");
        readUsers();
        readProduct("11");
        searchProduct();
        searchQuery("토마토");
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

    // 유저 데이터 READ
    private void readUsers(){
        DocumentReference docRef = db.collection("users").document("1");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "유저 DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    // 상품 데이터 READ (문서 번호 입력 시 반환)
    private void readProduct(String docNum){
       // DocumentReference docRef = db.collection("product").document("5YyVblkgkC7gMcslfy7L");
        DocumentReference docRef = db.collection("users").document(docNum).collection("product").document("5YyVblkgkC7gMcslfy7L");

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "상품 DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    // 상품 데이터 출력 query (document에 직접 접근)
    private void searchProduct(){
        db.collection("users").document("11").collection("product")
                .whereEqualTo("pdtName", "토마토")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    // 상품 데이터 그룹 query (컬렉션그룹쿼리)
    public void searchQuery(String productName){
        db.collectionGroup("product").whereEqualTo("pdtName", productName).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(final QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                            //Log.d(TAG, "PRODUCT= " + snap.getId() + " => " + snap.getData());
                            ProductList=snap.getData();

                            //상위 Collection(user) 정보로 접근
                            DocumentReference docRef = snap.getReference().getParent().getParent();
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot document = task.getResult();
                                        UserList=document.getData();
                                        //Log.d(TAG, "USER= " + document.getData());
                                        ProductList.putAll(UserList); //USER+PRODUCT병합

                                        //MAP<>에 들어갔는지 test용 출력 (LOG확인)
                                        for ( String key : ProductList.keySet() ) {
                                            System.out.println("key : " + key +" / value : " + ProductList.get(key));
                                        }
                                        System.out.println("<=========================================>");
                                }
                            });

//                            //String a = ProductList.get("pdtName").toString();
//                            //String b = ProductList.get("pdtEnrollDate").toString();
//                            //System.out.println("상품명? " + a + " 등록날짜? " + b );

                            /*
                             추후 지도기능, MAP Activity로 연결할 때 데이터 적절히 변환해서 넘기는 과정 필요
                             Map <Key, Value> 형태에서 ProductList <pdtName, 토마토> <pdtEnrollDate, 2019-10-31> 이런 식으로 저장되는 구조..
                            */
                    }
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
                    transaction.replace(R.id.frame_layout, loginFragment).commitAllowingStateLoss();
                    break;
                }
            }
            return true;
        }
    }

    //Fragment 교체
    public void replaceSearchFrag(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, searchFragment).commitAllowingStateLoss();
        // 검색 searchFragment 연결
    }
    public void replaceLoginFrag(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, loginFragment).commitAllowingStateLoss();
        // 회원가입 Fragment 연결 (추후 변경)
    }
    public void replaceJoinFrag(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, JoinFragment).commitAllowingStateLoss();
        // 회원가입 Fragment 연결 (추후 변경)
    }
    public void replaceFindFrag(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, menu2Fragment).commitAllowingStateLoss();
        // 추후에 아이디 비밀번호찾기 페이지 만들면 그 fragment로 연결하기
    }
    public void replaceMenu5Frag(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, menu5Fragment).commitAllowingStateLoss();
        // 추후에 아이디 비밀번호찾기 페이지 만들면 그 fragment로 연결하기
    }

}
