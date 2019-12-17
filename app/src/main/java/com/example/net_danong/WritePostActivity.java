package com.example.net_danong;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.os.Bundle;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class WritePostActivity extends BasicActivity {
    private static final String TAG = "WritePostActivity";

    //상품대표이미지
    private static final int PICK_FROM_ALBUM = 10;
    private Uri imageUri;
    private ImageView productImage;


    private String profilePath;
    public static String category;
    private FirebaseUser user;
    /*private ArrayList<String> pathList = new ArrayList<>(); *//*이미지경로만들기*/
    private LinearLayout parent;
    private int pathCount, successCount;
    private static FirebaseAuth mAuth;/*선언하기*/
    private static FirebaseFirestore mdb;/*바뀐부분*//*선언하기*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post_1);

        findViewById(R.id.check).setOnClickListener(onClickListener);
        /*findViewById(R.id.gallery).setOnClickListener(onClickListener);*/
        productImage = findViewById(R.id.iv_productImage);
        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
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
    public void onBackPressed() {
        super.onBackPressed();
            finish();
        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK) {
            productImage.setImageURI(data.getData()); // 가운데 뷰를 바꿈
            imageUri = data.getData();// 이미지 경로 원본
        }
    }

        //일단 주석처리할게용
       /* public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {
            case 0: {
                    if (resultCode == Activity.RESULT_OK) {
                         profilePath = data.getStringExtra("profilePath");
                         Bitmap bmp = BitmapFactory.decodeFile(profilePath);
                            profileImageView.setImageBitmap(bmp);
                    }
                break;
            }
        }*/
    //}//profileImageView에 이미지 넣기




    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.check:
                    profileUpdate();
                    break;
               /* case R.id.profileImageView:
                    if (ContextCompat.checkSelfPermission(WritePostActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)*//*갤러리접근권한얻기*//*
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(WritePostActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    1);*//*권한이 없을 떄 나오는 작업*//*
                        if (ActivityCompat.shouldShowRequestPermissionRationale(WritePostActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        } else {
                            startToast("권한을 허용해 주세요");
                        }*//*권한다시 묻는 작업*//*
                    }else{
                        *//*myStartActivity(GalleryActivity.class);*//*
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        startActivityForResult(intent, PICK_FROM_ALBUM);
                    }*//*권한 허용했을 때 나오는 작업*//*
                    break;*/
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
        }
    }


    private void profileUpdate() {
        final String title = ((EditText) findViewById(R.id.titleEditText)).getText().toString();
        final String product = ((EditText) findViewById(R.id.productEditText)).getText().toString();
        final String price = ((EditText) findViewById(R.id.priceEditText)).getText().toString();
        final String location = ((EditText) findViewById(R.id.locationEditText)).getText().toString();
        final String contents = ((EditText) findViewById(R.id.contentsEditText)).getText().toString();
        final Object createdAt = ServerValue.TIMESTAMP;
       /* String contents = ((EditText) findViewById(R.id.contentsEditText)).getText().toString();*/

        if (title.length() > 0 && product.length() > 0 && price.length() > 0 && location.length() > 0 && contents.length() > 0) {
//            final ArrayList<String> contentsList = new ArrayList<>();//내용 하나씩 추가 위해서
            user = FirebaseAuth.getInstance().getCurrentUser();
            final StorageReference REF =  FirebaseStorage.getInstance().getReference().child("productImages").child(FirebaseAuth.getInstance().getCurrentUser().getUid()+ createdAt.toString());

            REF.putFile(imageUri)
                    .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return REF.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    String imageUrl = task.getResult().toString();
                    ProductWriteInfo productWriteInfo = new ProductWriteInfo(user.getUid(),title,product,price,location,contents, user.getEmail().substring(0,user.getEmail().lastIndexOf("@")), createdAt,category,imageUrl);
                    String city = productWriteInfo.getLocation();
                    productWriteInfo.setLatitude(glatitude(city));
                    productWriteInfo.setLongitude(glongitude(city));
                    uploader(productWriteInfo);
                }
            });
        } else {
            startToast("상품정보를 입력해주세요.");
        }
    }

    public double glatitude(String city){
        Geocoder geocoder = new Geocoder(this);
        //getCity 작업 필요
        List<Address> addressList = null;
        try {
            addressList = geocoder.getFromLocationName(city, 10);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
        }
        if (addressList != null) {
            if (addressList.size() == 0) {
                Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
            }
        }

        double latitude = addressList.get(0).getLatitude(); //위도
        return latitude;
    }

    public double glongitude(String city){
        Geocoder geocoder = new Geocoder(this);
        //getCity 작업 필요
        List<Address> addressList = null;
        try {
            addressList = geocoder.getFromLocationName(city, 10);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
        }
        if (addressList != null) {
            if (addressList.size() == 0) {
                Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
            }
        }
        double longitude = addressList.get(0).getLongitude(); //경도
        return longitude;
    }



    private void uploader(ProductWriteInfo productWriteInfo){
        mdb = FirebaseFirestore.getInstance();//mdb가져다 쓰기
        mAuth = FirebaseAuth.getInstance();//제발!! 없으면 안됨!!
        mdb.collection("products").add(productWriteInfo)/*바뀐부분*/
            /*db.collection("product").add(productWriteInfo)*/
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(WritePostActivity.this, "업로드 완료", Toast.LENGTH_SHORT).show();
                        finish();//추가
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
