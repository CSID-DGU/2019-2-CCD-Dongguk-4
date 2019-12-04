package com.example.net_danong;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.maps.android.clustering.ClusterManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

public class WritePostActivity extends BasicActivity  implements OnMapReadyCallback {
    private static final String TAG = "WritePostActivity";
    View view;
    private ImageView profileImageView;
    private String profilePath;
    public static String category;
    private FirebaseUser user;
    /*private ArrayList<String> pathList = new ArrayList<>(); *//*이미지경로만들기*/
    private LinearLayout parent;
    private int pathCount, successCount;
    private static FirebaseAuth mAuth;/*선언하기*/
    private static FirebaseFirestore mdb;/*바뀐부분*//*선언하기*/
    private static LatLng pdtLocation;
    MapView mapView;
    GoogleMap mMap;
    private static final int REQUEST_CODE_PERMISSIONS = 1000;//현재위치
    private FusedLocationProviderClient mFusedLocationClient;//현재위치 얻는 코드
    private MapFragmentViewModel mViewModel;

    Bundle extra;
    Double addressX;
    Double addressY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post_1);

        findViewById(R.id.check).setOnClickListener(onClickListener);
        findViewById(R.id.gallery).setOnClickListener(onClickListener);
        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);//현재위치
        mViewModel = ViewModelProviders.of(this).get(MapFragmentViewModel.class);

        Button button = findViewById(R.id.btn_lastLocation);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                onLastLocationButtonClicked();
            }
        });

        Spinner spinner = findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.spinnerArray, android.R.layout.simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String str = adapterView.getItemAtPosition(i).toString();
                if (str != "")
                    category = str;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }//클릭한 마커 정보 들어와서 이 경우 전화걸기
        });
    }



    public void onLastLocationButtonClicked() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.
                PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_PERMISSIONS);

            return;
        }

        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    pdtLocation = myLocation;
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
    public void onBackPressed() {
        super.onBackPressed();
            finish();
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {
            case 0: {
                if (resultCode == Activity.RESULT_OK) {
                    /*pathList.add(profilePath);*/ /*경로 생성될 때마다 추가*/
                    profilePath = data.getStringExtra("profilePath");
                    Glide.with(this).load(profilePath).centerCrop().override(500).into(profileImageView);
                }
                break;
            }
        }
    }//profileImageView에 이미지 넣기

/*    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0: {
                if (resultCode == Activity.RESULT_OK) {
                    String profilePath = data.getStringExtra("profilePath");
                    pathList.add(profilePath);

                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    ImageView imageView = new ImageView(WritePostActivity.this);
                    imageView.setLayoutParams(layoutParams);
                    Glide.with(this).load(profilePath).override(1000).into(imageView);
                    parent.addView(imageView);

                    EditText editText = new EditText(WritePostActivity.this);
                    editText.setLayoutParams(layoutParams);
                    editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                    parent.addView(editText);
                }
                break;
            }
        }
    }*///이미지가 내용에 들어가도록하는 코드

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.check:
                    profileUpdate();
                    break;
                case R.id.gallery:
                    if (ContextCompat.checkSelfPermission(WritePostActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)/*갤러리접근권한얻기*/
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(WritePostActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    1);/*권한이 없을 떄 나오는 작업*/
                        if (ActivityCompat.shouldShowRequestPermissionRationale(WritePostActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        } else {
                            startToast("권한을 허용해 주세요");
                        }/*권한다시 묻는 작업*/
                    }else{
                        myStartActivity(GalleryActivity.class);
                    }/*권한 허용했을 때 나오는 작업*/
                    break;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    myStartActivity(GalleryActivity.class);
                } else {
                    startToast("권한을 허용해 주세요");
                }
            }
            case REQUEST_CODE_PERMISSIONS:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "권한 체크 거부 됨", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
    private void profileUpdate() {
        final String title = ((EditText) findViewById(R.id.titleEditText)).getText().toString();
        final String product = ((EditText) findViewById(R.id.productEditText)).getText().toString();
        final String price = ((EditText) findViewById(R.id.priceEditText)).getText().toString();
        final String location = ((EditText) findViewById(R.id.locationEditText)).getText().toString();
        final String contents = ((EditText) findViewById(R.id.contentsEditText)).getText().toString();
       /* String contents = ((EditText) findViewById(R.id.contentsEditText)).getText().toString();*/

        if (title.length() > 0 && product.length() > 0 && price.length() > 0 && location.length() > 0 && contents.length() > 0) {
//            final ArrayList<String> contentsList = new ArrayList<>();//내용 하나씩 추가 위해서
            user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            final StorageReference mountainImagesRef = storageRef.child("users/"+user.getUid()+"/profileImage.jpg");

            if(profilePath == null){
                ProductWriteInfo productWriteInfo = new ProductWriteInfo(title, product, price, location, contents);
                productWriteInfo.setUserUid(user.getUid());
                productWriteInfo.setAvgRating(0);
                productWriteInfo.setGoogleLocation(pdtLocation);
                productWriteInfo.setNumRatings(0);
                uploader(productWriteInfo);
            }else{
                try {
                    InputStream stream = new FileInputStream(new File(profilePath));
                    UploadTask uploadTask = mountainImagesRef.putStream(stream);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return mountainImagesRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                ProductWriteInfo productWriteInfo = new ProductWriteInfo(title, product, price, location, contents, new Date(), category, downloadUri.toString());
                                productWriteInfo.setGoogleLocation(pdtLocation);
                                uploader(productWriteInfo);
                                startToast("상품등록을 성공하였습니다.");
                                finish();
                            } else {
                                startToast("등록정보를 보내는데 실패하였습니다.");
                            }
                        }
                    });
                } catch (FileNotFoundException e) {
                    Log.e("로그", "에러: " + e.toString());
                }
            }
        } else {
            startToast("상품정보를 입력해주세요.");
        }
    }

            /*for (int i = 0; i < parent.getChildCount(); i++) {
                View view = parent.getChildAt(i);
                if (view instanceof EditText) {
                    String text = ((EditText) view).getText().toString();
                    if (text.length() > 0) {
                        contentsList.add(text);
                    }
                } else {//이미지뷰의 경우
                    contentsList.add(pathList.get(pathCount));
                    final StorageReference mountainImagesRef = storageRef.child("users/" + user.getUid() + "/" + pathCount + ".jpg");
                    try {
                        InputStream stream = new FileInputStream(new File(pathList.get(pathCount)));
                        StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("index", "" + (contentsList.size() - 1)).build();
                        UploadTask uploadTask = mountainImagesRef.putStream(stream, metadata);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                final int index = Integer.parseInt(taskSnapshot.getMetadata().getCustomMetadata("index"));
                                mountainImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        contentsList.set(index, uri.toString());
                                        successCount++;
                                        if (pathList.size() == successCount) {
                                            //완료
                                            ProductWriteInfo productWriteInfo = new ProductWriteInfo(title, product, price, location, contentsList, user.getUid(), new Date(), category);
                                            storeUpload(productWriteInfo);
                                            for (int a = 0; a < contentsList.size(); a++) {
                                                Log.e("로그: ", "콘덴츠: " + contentsList.get(a));
                                            }
                                        }
                                    }
                                });
                            }
                        });
                    } catch (FileNotFoundException e) {
                        Log.e("로그", "에러: " + e.toString());
                    }
                    pathCount++;
                }
            }
        } else {
            startToast("상품정보를 입력해주세요.");
        }
    }*/

   /* private void storeUpload(ProductWriteInfo productWriteInfo) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").add(productWriteInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }*///db에 넣기


    private void uploader(ProductWriteInfo productWriteInfo){
        mdb = FirebaseFirestore.getInstance();//mdb가져다 쓰기
        mAuth = FirebaseAuth.getInstance();//제발!! 없으면 안됨!!
        mdb.collection("products").add(productWriteInfo)/*바뀐부분*/
            /*db.collection("product").add(productWriteInfo)*/
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 0);
    }
}
//주소>위도,경도 변환 코드 추가,,
//데이터베이스에 입력되는 것은 주소>그 주소를 받아 위도, 경도 변환값 반환>반환값(위도, 경도)데이터베이스에 넣기

/**
 * 주소로부터 위치정보 취득
 *
 * @param address
 * 주소
 */
/*    public static Location findGeoPoint(Context mcontext, String address) {
        Location loc = new Location("");
        Geocoder coder = new Geocoder(mcontext);
        List<Address> addr = null;// 한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 설정

        try {
            addr = coder.getFromLocationName(address, 5);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }// 몇개 까지의 주소를 원하는지 지정 1~5개 정도가 적당
        if (addr != null) {
            for (int i = 0; i < addr.size(); i++) {
                Address lating = addr.get(i);
\\

                 double lon = lating.getLongitude(); // 경도가져오기
                loc.setLatitude(lat);
                loc.setLongitude(lon);
            }
        }
        return loc;
    }*/
/*TO do:갤러리 접근, 파이어스토어 연결*/
