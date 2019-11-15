package com.example.net_danong;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity implements  GoogleApiClient.OnConnectionFailedListener{

    //이메일 비밀번호 로그인 모듈 변수
    private FirebaseAuth mAuth;
    //현재 로그인 된 유저 정보를 담을 변수
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance(); //이메일 비밀번호 로그인 모듈 변수

        //이메일
        final EditText userEmail = findViewById(R.id.et_eamil);        //비밀번호
        final EditText userPw = findViewById(R.id.et_password);
        //가입버튼
        Button btnSignUp = findViewById(R.id.btn_signUp);
        //로그인버튼
        Button btnSignIn = findViewById(R.id.btn_logIn);

        //로그인 버튼이 눌렀을 때
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = userEmail.getText().toString();
                String password = userPw.getText().toString();

                //로그인 성공
                loginStart(email, password);
            }
        });

        //가입하러 가기 버튼 눌렀을 때
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(LogInActivity.this,"가입하러 가기 버튼 눌렀을 때",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LogInActivity.this, JoinActivity.class));
                finish();
            }
        });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(LogInActivity.this,"연결이 해제되었습니다",Toast.LENGTH_SHORT).show();
    }

    //public Boolean check;
    public void loginStart(String email, String password){
        Toast.makeText(LogInActivity.this,"loginStart 함수 안으로" ,Toast.LENGTH_SHORT).show();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                Toast.makeText(LogInActivity.this,"mAuth. onComplete 함수" ,Toast.LENGTH_SHORT).show();
                if (!task.isSuccessful()) {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        Toast.makeText(LogInActivity.this,"존재하지 않는 id 입니다." ,Toast.LENGTH_SHORT).show();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        Toast.makeText(LogInActivity.this,"이메일 형식이 맞지 않습니다." ,Toast.LENGTH_SHORT).show();
                    } catch (FirebaseNetworkException e) {
                        Toast.makeText(LogInActivity.this,"Firebase NetworkException" ,Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(LogInActivity.this,"Exception" ,Toast.LENGTH_SHORT).show();
                    }

                }else{


                    currentUser = mAuth.getCurrentUser();

                    Toast.makeText(LogInActivity.this, "로그인 성공" + "/" + currentUser.getEmail() + "/" + currentUser.getUid() ,Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(LogInActivity.this, MainActivity.class));
                    finish();
                }

            }
        });
    }

    //로그아웃 안했으면, 즉 로그인 되어있으면 자동으로 메인페이지로 이동시키기
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(LogInActivity.this, MainActivity.class));
            finish();
        }
    }



}


