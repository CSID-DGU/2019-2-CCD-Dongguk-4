package com.example.net_danong;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.maps.android.clustering.ClusterManager;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback,  ProductListAdapter.OnProductSelectedListener {
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
    RecyclerView mProductRecycler;
    FirebaseUser user;
    FirebaseFirestore mFirestore;
    Query mQuery;
    ProductListAdapter mAdapter;
    MapActivityViewModel mViewModel;

    //검색 후 결과값 저장해서 날아오는 변수들,,
    Bundle extra;
    String addressX;
    String addressY;
    String userID;
    String pdtName;
    String pdtEnrolldate;


    //내부 전환 (menu1-search결과) newInstance 필수
    public static MapsFragment newInstance() {
        return new MapsFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        //MainActivity에서 검색 결과값 받아오기 (있을 때)
        extra = this.getArguments();
        if(extra != null) {
            extra = getArguments();


            List list = extra.getParcelableArrayList("list");
            System.out.println("제발 으아아아ㅏㄱ" + list);
            /*
            HashMap<String, Object> hMap = (HashMap<String, Object>)extra.getSerializable("hashmap");
            String address =  hMap.get("userAddress").toString();
            addressX = address.split(",")[1];
            addressY = address.split(",")[2];
            addressY = addressY.substring(0, addressY.length()-1);
            userID = hMap.get("userID").toString();
            pdtName = hMap.get("pdtName").toString();
            pdtEnrolldate = hMap.get("pdtEnrolldate").toString();
            */

            addressX = extra.getString("addressx");
            addressY = extra.getString("addressy");
            userID = extra.getString("userid");
            pdtName = extra.getString("pdtname");
            pdtEnrolldate = extra.getString("pdtenrolldate");

            Toast.makeText(getActivity(),addressX+addressY+userID,Toast.LENGTH_SHORT).show();

            //확인용
            System.out.println("지도 결과" + addressX +" / "+ addressY +" / "+  userID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view  = inflater.inflate(R.layout.fragment_maps, container, false);
        mapView = (MapView)view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());//현재위치

        // View model
        mViewModel = ViewModelProviders.of(this).get(MapActivityViewModel.class);
        //map 버튼
        Button button = (Button) view.findViewById(R.id.btn_lastLocation);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                onLastLocationButtonClicked(rootView);
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

        mProductRecycler = (RecyclerView) view.findViewById(R.id.recycler_product);
        mAdapter = new ProductListAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    mProductRecycler.setVisibility(View.GONE);
                } else {
                    mProductRecycler.setVisibility(View.VISIBLE);
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
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }


    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void OnProductSelected(DocumentSnapshot restaurant) {
        // Go to the details page for the selected restaurant
        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.KEY_PRODUCT_UID, restaurant.getId());

        startActivity(intent);
    }

    private void showTodoToast() {
        Toast.makeText(getActivity(), "TODO: Implement", Toast.LENGTH_SHORT).show();
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


        if(addressX != null) { //검색 후 뜨는 화면
            searchItem();
        } else{
            //기본 화면 (하단 버튼 map 누르면 뜨는 초기 화면)
            // Add a marker in Sydney and move the camera
            LatLng sydney = new LatLng(37.5609417, 126.9911718);
            mMap.addMarker(new MarkerOptions().position(sydney).title("충무로역"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
            // Add a marker in Sydney and move the camera
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tell:0212341234"));
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }//클릭한 마커 정보 들어와서 이 경우 전화걸기
            });
            addItem();
        }
    }

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

    private void searchItem() {
        if(addressX != null) {

            //검색 결과로 받아온 주소 좌표값 대입
            // (보완필요) = for문이나 []배열값으로 여러개 뜨게 하기
            double lat = Double.parseDouble(addressX) ;
            double lng = Double.parseDouble(addressY) ;

            // Add a marker in Sydney and move the camera
            LatLng search = new LatLng(lat, lng);
            mMap.addMarker(new MarkerOptions().position(search).title("결과값"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(search));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

            // Add a marker in Sydney and move the camera
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tell:0212341234"));
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }//클릭한 마커 정보 들어와서 이 경우 전화걸기
            });


//            for (int i = 0; i < 10; i++) {
//                double offset = i / 60d;
//                lat = lat + offset;
//                lng = lng + offset;
//                MyItem offsetItem = new MyItem(lat, lng);
//                mClusterManager.addItem(offsetItem);
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
    public void onCreateOptionsMenu(Menu menu,  MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.map, menu);
        MenuItem item = menu.findItem(R.id.action_toggle);
        if (mLayout != null) {
            if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN) {
                item.setTitle(R.string.action_show);
            } else {
                item.setTitle(R.string.action_hide);
            }
        }
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.getActivity().onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_toggle: {
                if (mLayout != null) {
                    if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN) {
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                        item.setTitle(R.string.action_show);
                    } else {
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        item.setTitle(R.string.action_hide);
                    }
                }
                return true;
            }
            case R.id.action_anchor: {
                if (mLayout != null) {
                    if (mLayout.getAnchorPoint() == 1.0f) {
                        mLayout.setAnchorPoint(0.7f);
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                        item.setTitle(R.string.action_anchor_disable);
                    } else {
                        mLayout.setAnchorPoint(1.0f);
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        item.setTitle(R.string.action_anchor_enable);
                    }
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
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
