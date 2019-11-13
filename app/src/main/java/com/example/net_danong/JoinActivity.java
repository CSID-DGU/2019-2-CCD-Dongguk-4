package com.example.net_danong;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;

import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    //다음 진행 버튼
    public Button nextBtn;

    //체크박스 선택여부
    public int TERM_AGREE_1 = 0;  //No check = 0, Check = 1
    public int TERM_AGREE_2 = 0;
    public int TERM_AGREE_3 = 0;
    public int TERM_AGREE_4 = 0;
    //체크박스
    AppCompatCheckBox check1;
    AppCompatCheckBox check2;
    AppCompatCheckBox check3;
    AppCompatCheckBox check4;

    //비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");
    //이메일 정규식
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9]@[a-zA-Z0-9].[a-zA-Z0-9]");
    //파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;

    //이메일과 비밀번호
    private EditText editName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editPhoneNumber;

    private String name = "";
    private String email ="";
    private String password = "";
    private String phoneNumber = "";

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        //파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.emailText);
        editTextPassword = findViewById(R.id.passwordText);
        editName = findViewById(R.id.nameText);
        editPhoneNumber = findViewById(R.id.phonenumberText);

        check1 = (AppCompatCheckBox) findViewById(R.id.register_check_1);
        check2 = (AppCompatCheckBox) findViewById(R.id.register_check_2);
        check3 = (AppCompatCheckBox) findViewById(R.id.register_check_3);
        check4 = (AppCompatCheckBox) findViewById(R.id.register_check_4);

        //첫번째 동의
        check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    TERM_AGREE_1 = 1;
                } else {
                    TERM_AGREE_1 = 0;
                }
            }
        });

        //두번째 동의
        check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    TERM_AGREE_2 = 1;
                } else {
                    TERM_AGREE_2 = 0;
                }
            }
        });

        //세번째 동의
        check3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    TERM_AGREE_3 = 1;
                } else {
                    TERM_AGREE_3 = 0;
                }
            }
        });
        //전체동의
        check4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if (isChecked) {
                    check1.setChecked(true);
                    check2.setChecked(true);
                    check3.setChecked(true);
                    TERM_AGREE_4 = 1;
                } else {
                    check1.setChecked(false);
                    check2.setChecked(false);
                    check3.setChecked(false);
                    TERM_AGREE_4 = 0;
                }
            }
        });
        //회원가입 버튼
        nextBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //전체 약관 체크여부
                if(TERM_AGREE_4 != 1) {
                    if(TERM_AGREE_3 == 1) {
                        if(TERM_AGREE_2 == 1) {
                            if(TERM_AGREE_1 == 1) {
                                startActivity(new Intent(JoinActivity.this, MainActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), "약관을 체크해주세요", Toast.LENGTH_SHORT).show();

                                return;
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "약관을 체크해주세요", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    //전체 약관 체크된 경우
                    else {
                        Intent intent = new Intent(JoinActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    public void signUp(View view) {
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();

        if(isValidEmail() && isValidPasswd()) {
            createUser(email, password);
        }
    }

    //이메일 유효성 조사
    private boolean isValidEmail() {
        if(email.isEmpty()) {
            //이메일 공백
            return false;
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            //이메일 형식 불일치
            return false;
        } else {
            return true;
        }
    }

    //비밀번호 유효성 검사
    private boolean isValidPasswd() {
        if(password.isEmpty()) {
            //비밀번호 공백
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            //비밀번호 형식 불일치
            return false;
        } else {
            return true;
        }
    }

    //회원가입
    private void createUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            //회원가입 성공
                            Toast.makeText(JoinActivity.this, R.string.success_signup, Toast.LENGTH_SHORT);
                        } else {
                            //회원가입 실패
                            Toast.makeText(JoinActivity.this, R.string.failed_signup,Toast.LENGTH_SHORT);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}