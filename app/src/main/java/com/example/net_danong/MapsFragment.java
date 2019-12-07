package com.example.net_danong;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.clustering.ClusterManager;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback, ProductListAdapter.OnProductSelectedListener {
    View rootView;
    MapView mapView;
    SlidingUpPanelLayout mLayout;
    GoogleMap mMap;
    Context context;

    private static final int REQUEST_CODE_PERMISSIONS = 1000;//현재위치
    private FusedLocationProviderClient mFusedLocationClient;//현재위치 얻는 코드

    ClusterManager<MyItem> mClusterManager;
    private static final String TAG = "MapsActivity";

    private static final int RC_SIGN_IN = 9001;
    private static final int LIMIT = 50;
    private RecyclerView mProductRecycler;
    private TextView mPdtCntNum;
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private ProductListAdapter mAdapter;
    private MapFragmentViewModel mViewModel;

    String productName = null;

    //내부 전환 (menu1-search결과) newInstance 필수
    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        if(getArguments() != null){
            productName = getArguments().getString("pdtName");
        }

        View view  = inflater.inflate(R.layout.fragment_maps, container, false);
        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        mPdtCntNum = view.findViewById(R.id.txt_pdtCntNum);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());//현재위치
        mProductRecycler = view.findViewById(R.id.recycler_product);

        // View model
        mViewModel = ViewModelProviders.of(this).get(MapFragmentViewModel.class);
        //map 버튼
        Button button = view.findViewById(R.id.btn_lastLocation);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                onLastLocationButtonClicked(rootView);
            }
        });


        //writeposst클래스 오류
        Button post = (Button) view.findViewById(R.id.btn_post);
        post.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), WritePostActivity.class);
                startActivity(intent);
            }
        });


        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);

        // Initialize Firestore and the main RecyclerView
        initFirestore();

        //리사이클러뷰 초기화
        if (mQuery == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }

        mAdapter = new ProductListAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    mProductRecycler.setVisibility(View.GONE);

                } else {
                    mProductRecycler.setVisibility(View.VISIBLE);
                    mPdtCntNum.setText(Integer.toString(getItemCount()));
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(getActivity().findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };
        mProductRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mProductRecycler.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
        if(productName == null) {
            //초기화면
            mQuery = mFirestore.collectionGroup("products")
                    .orderBy("avgRating", Query.Direction.DESCENDING)
                    .limit(LIMIT);
        }else{
            mQuery = mFirestore.collectionGroup("products").whereEqualTo("product", productName);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    @Override
    public void OnProductSelected(DocumentSnapshot product) {
        // Go to the details page for the selected restaurant
        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.KEY_PRODUCT_ID, product.getId());
        startActivity(intent);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mClusterManager = new ClusterManager<>(getActivity(), mMap);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem myItem) {
                Toast.makeText(getActivity(), myItem.getPosition().toString(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        //기본 화면 (초기 위치 설정 + 카메라 줌)
        LatLng main = new LatLng(37.5613682, 126.9941803);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(main));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
        // Add a marker in Sydney and move the camera

//        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick(Marker marker) {
//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tell:0212341234"));
//                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
//                    startActivity(intent);
//                }
//            }//클릭한 마커 정보 들어와서 이 경우 전화걸기
//        });
        initMark();
    }

    //진심 비효율적인 코드......리사이클러뷰랑 한 번에 연동시킬 수 있는 방법 찾기....ㅠㅠ
    private void initMark(){
        mFirestore = FirebaseFirestore.getInstance();
        if(productName == null) {
            //초기
            mFirestore.collection("products")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //Log.d(TAG, document.getId() + " => " + document.getData());
                                    String pdt = document.get("product").toString();
                                    String pdtID = document.getId();

                                    double lat = Double.valueOf(document.get("latitude").toString());
                                    double lng = Double.valueOf(document.get("longitude").toString());
                                    LatLng latLng = new LatLng(lat, lng);

                                    MyItem offsetItem = new MyItem(lat, lng);
                                    mClusterManager.addItem(offsetItem);

                                    mMap.addMarker(new MarkerOptions().position(latLng).title(pdtID));
                                    mMap.setOnMarkerClickListener(markerClickListener);
                                }
                            } else {
                                Log.d(TAG, "에러 발생 :  ", task.getException());
                            }
                        }
                    });
        }else{
            //검색 후 결과
            mFirestore.collectionGroup("products").whereEqualTo("product", productName)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //Log.d(TAG, document.getId() + " => " + document.getData());
                                    String pdt = document.get("product").toString();
                                    double lat = Double.valueOf(document.get("latitude").toString());
                                    double lng = Double.valueOf(document.get("longitude").toString());
                                    LatLng latLng = new LatLng(lat, lng);

                                    MyItem offsetItem = new MyItem(lat, lng);
                                    mClusterManager.addItem(offsetItem);

                                    mMap.addMarker(new MarkerOptions().position(latLng).title(pdt));
                                    mMap.setOnMarkerClickListener(markerClickListener);
                                }
                            } else {
                                Log.d(TAG, "에러 발생 :  ", task.getException());
                            }
                        }
                    });

        }
    }

    //마커 클릭 리스너 (마커 클릭 시, 상품 세부 정보로 이동)
    GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            marker.hideInfoWindow();
            String pdtID = marker.getTitle();
            // Go to the details page for the selected restaurant
            Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
            intent.putExtra(ProductDetailActivity.KEY_PRODUCT_ID, pdtID);
            startActivity(intent);
            return true;
        }
    };

    private void addItem() {

        double lat = 37.5609739;
        double lng = 126.99134;

        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;
            MyItem offsetItem = new MyItem(lat, lng);
            mClusterManager.addItem(offsetItem);
        }
    }


    public void onLastLocationButtonClicked(View view) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.
                PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_PERMISSIONS);

            return;
        }

        mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions()
                            .position(myLocation)
                            .title("현재 위치"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSIONS:
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "권한 체크 거부 됨", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.getActivity().onPrepareOptionsMenu(menu);
    }


    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.getActivity().onBackPressed();
        }
    }
}

/*MapsFragment, fragment_maps만 새로 만든 파일*/
