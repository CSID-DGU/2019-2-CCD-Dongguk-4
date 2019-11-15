package com.example.net_danong;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



import java.util.Date;
import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity  implements  View.OnClickListener{

    //비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");
    //이메일 정규식
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9]@[a-zA-Z0-9].[a-zA-Z0-9]");
    //파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;
    DatabaseReference mdatabase;

    //이메일과 비밀번호
    private EditText editName, editTextEmail, editTextPassword, editPhoneNumber;
    private Button mRegisterbtn;
    private String name, email, password, phoneNumber;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        //파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();

        editName = findViewById(R.id.nameText);
        editTextEmail = findViewById(R.id.emailText);
        editTextPassword = findViewById(R.id.passwordText);
        editPhoneNumber = findViewById(R.id.phonenumberText);
        mRegisterbtn = (Button)findViewById(R.id.register_btn_finish);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @Override
    public void onClick(View v) {
        if(v == mRegisterbtn) {
            UserRegister();
        }
    }

    private void UserRegister() {
        name = editName.getText().toString().trim();
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        phoneNumber = editPhoneNumber.getText().toString().trim();

        if(TextUtils.isEmpty(name)) {
            Toast.makeText(JoinActivity.this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(JoinActivity.this, "이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(JoinActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(JoinActivity.this, "전화번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            Toast.makeText(JoinActivity.this, "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else if(!PASSWORD_PATTERN.matcher(password).matches()) {
            Toast.makeText(JoinActivity.this, "비밀번호 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    OnAuth(task.getResult().getUser());
                    firebaseAuth.signOut();
                    Toast.makeText(JoinActivity.this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(JoinActivity.this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void OnAuth(FirebaseUser user) {
        createAnewUser(user.getUid());
    }
    private void createAnewUser(String uid) {
        User user = BuildNewuser();
        mdatabase.child(uid).setValue(user);
    }
    private User BuildNewuser() {
        return new User(
                getDisplayName(),
                getUserEmail(),
                getUserPhone(),
                new Date().getTime()
        );
    }
    public String getDisplayName() {
        return name;
    }
    public String getUserEmail() {
        return email;
    }
    public String getUserPhone() {
        return phoneNumber;
    }
}

