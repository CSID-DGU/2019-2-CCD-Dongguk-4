package com.example.net_danong;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.net_danong.Board.BoardFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//navBar 관련 항목

public class MainActivity extends AppCompatActivity{
    private static FirebaseFirestore mdb;
    private static FirebaseAuth mAuth;
    //로그인 상태변화 확인
    private FirebaseAuth.AuthStateListener mAuthListener;
    //firestore TAG
    private static final String TAG = "DocSnippets";

    //필요한 변수들 선언하기
    Map<String, Object> ProductList; //검색+지도에서 사용할 결과값 저장공간
    Map<String, Object> UserList;    //검색+지도에서 사용할 결과값 저장공간2
    Bundle mapbundle; //검색Fragment로 전달할 때 사용할 것,,
    List pdtlist = new ArrayList();
    int loopNum; //검색 관련 loop문 변수

    // FrameLayout 관련 초기화
    private FragmentManager fragmentManager = getSupportFragmentManager();
    // 5개의 메뉴에 들어갈 Fragment들 (변수명 변경 필요)
    private FirstPageFragment FirstPageFragment = new FirstPageFragment();
    private BoardFragment boardFragment = new BoardFragment();
    private Menu5Fragment menu5Fragment = new Menu5Fragment();
    //추가 기능별 Fragment (아이디비번찾기, 회원가입, 등 기능 및 페이지 관련해서 추가 필요)
    private SearchFragment searchFragment = new SearchFragment();
    private LoginFragment loginFragment = new LoginFragment();
    private JoinFragment JoinFragment = new JoinFragment();
    private MapsFragment mapsFragment = new MapsFragment();
    private ChatFragment ChatFragment= new ChatFragment();
    private MyPageFragment mypageFragment= new MyPageFragment();






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Acitivity_main페이지 초기화면
        setContentView(R.layout.activity_main);

        //Fragment Layout 초기 설정 (menu1화면)
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, FirstPageFragment.newInstance()).commitAllowingStateLoss();

        //하단 navigation bar 지정
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

        mdb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();




        /*Intent intent = new Intent(MainActivity.this, FirstPageFragment.class);
        startActivity(intent);*/
       /* findViewById(R.id.floatingActionButton).setOnClickListener(onClickListener);//더보기 화면의 +버튼 클릭시 작동할 모드 설정*/
    }

    //검색 기능 (검색어 전달 기능)
    public void searchWord(String productName){
        String pdtName = productName;

        mapbundle = new Bundle();
        mapbundle.putString("pdtName", pdtName);
        mapsFragment.setArguments(mapbundle);

        //MAP화면으로 이동
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, mapsFragment).commitAllowingStateLoss();
    }

    //상품 검색 쿼리  (안 쓰는데 쿼리문때문에 일단 안 지웠ㅅ음,,)
    public void searchQ(String productName){
        mdb.collectionGroup("products").whereEqualTo("product", productName).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(final QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size()>0){ //검색값이 존재할 때
                            loopNum=0;
                            for (QueryDocumentSnapshot snap : queryDocumentSnapshots) {
                                //Product 컬렉션 값 받아옴
                                ProductList=snap.getData();
                                System.out.println("Product정보"+ProductList);

                                //유저 정보 연결
                                String userID = ProductList.get("userUid").toString();
                                System.out.println("userUid값은? "+userID);

                                DocumentReference docRef = mdb.collection("users").document(userID);
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                UserList=document.getData();
                                                System.out.println("유저 정보"+UserList);

                                                //마지막 query문 돌고 화면 전환 / 데이터 전송
                                                if(loopNum==queryDocumentSnapshots.size()-1){
                                                    System.out.println("노 에러.. plz... ");
                                                   // FragmentTransaction transaction = fragmentManager.beginTransaction();
                                                   // transaction.replace(R.id.frame_layout, mapsFragment).commitAllowingStateLoss();
                                                }
                                                loopNum = loopNum+1;
                                            } else {
                                                Log.d(TAG, "No such document");
                                            }
                                        } else {
                                            Log.d(TAG, "get failed with ", task.getException());
                                        }
                                    }
                                });

                                // 하위 컬렉션 연결 시키는 방법 찾기.. product와 일치하는 review 정보 따오기
                            } //for문 종료
                        } else{
                            Log.d(TAG, "Error getting documents/2 ");
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
                    transaction.replace(R.id.frame_layout, FirstPageFragment).commitAllowingStateLoss();
                    break;
                }
                case R.id.navigation_menu2: {
                    transaction.replace(R.id.frame_layout, ChatFragment).commitAllowingStateLoss();
                    break;
                }
                case R.id.navigation_menu3: {
                    //bundle 초기화 (바로 지도 클릭하면 초기화면 뜨도록)
                    if(mapbundle!=null){ mapbundle.clear(); }
                    transaction.replace(R.id.frame_layout, mapsFragment).commitAllowingStateLoss();
                    break;
                }
                case R.id.navigation_menu4: {
                    transaction.replace(R.id.frame_layout, boardFragment).commitAllowingStateLoss();
                    break;
                }
                case R.id.navigation_menu5: {
                    transaction.replace(R.id.frame_layout, mypageFragment).commitAllowingStateLoss();
                    break;
                }
            }
            return true;
        }
    }


    //Fragment 교체 .addToBackStack(null).commitAllowingStateLoss(); 사용해야 닫을때 뒤로 돌아감 ! ㅎㅁㅎ 닫을때도 마찬가지 pop사용
    public void replaceSearchFrag(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, searchFragment).addToBackStack(null).commitAllowingStateLoss();
        // 검색 searchFragment 연결
    }
    public void replaceLoginFrag(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, loginFragment).addToBackStack(null).commitAllowingStateLoss();
    }
    public void replaceJoinFrag(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, JoinFragment).addToBackStack(null).commitAllowingStateLoss();
    }
    public void replaceFindFrag(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
/*
        fragmentTransaction.replace(R.id.frame_layout, menu2Fragment).commitAllowingStateLoss();
*/
        // 추후에 아이디 비밀번호찾기 페이지 만들면 그 fragment로 연결하기
    }
    public void replaceMenu5Frag(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, menu5Fragment).addToBackStack(null).commitAllowingStateLoss();
    }
    public void replaceMapFrag(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, mapsFragment).addToBackStack(null).commitAllowingStateLoss();
        // 검색 쿼리 완료 후에 mapsFragment로 교체
    }
    // 로그인 완료 후 닫을 때 사용
    public void removeLogFrag(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(loginFragment).commitAllowingStateLoss();
        fragmentManager.popBackStack();
    }

}
