package com.example.net_danong;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

// firebaseAuth..? 받아오는 과정에서 바로 앱다운되길래 일단 주석 처리 했습니다,,ㅠ_ㅠ

public class JoinFragment extends Fragment {
    //비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");
    //이메일 정규식
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9]@[a-zA-Z0-9].[a-zA-Z0-9]");
    //파이어베이스 인증 객체 생성
    //private FirebaseAuth firebaseAuth;
    //DatabaseReference mdatabase;

    //이메일과 비밀번호
    private EditText editName, editTextEmail, editTextPassword, editPhoneNumber;
    private Button mRegisterbtn;
    private String name, email, password, phoneNumber;

    // 각각의 Fragment마다 Instance를 반환해 줄 메소드를 생성
    public static JoinFragment newInstance() {
        return new JoinFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_join, container, false);

        //파이어베이스 인증 객체 선언
        //firebaseAuth = FirebaseAuth.getInstance();

        editName = (EditText) view.findViewById(R.id.nameText);
        editTextEmail = (EditText) view.findViewById(R.id.emailText);
        editTextPassword = (EditText) view.findViewById(R.id.passwordText);
        editPhoneNumber = (EditText) view.findViewById(R.id.phonenumberText);
        mRegisterbtn = (Button) view.findViewById(R.id.register_btn_finish);
        //mdatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mRegisterbtn.setOnClickListener(new View.OnClickListener() {
            //            Fragment NewFragment;
            @Override
            public void onClick(View v) {
                //이벤트 처리
                //UserRegister();
            }
        });
        return view;
    }

//    @Override


    /*
    private void UserRegister() {
        name = editName.getText().toString().trim();
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        phoneNumber = editPhoneNumber.getText().toString().trim();

        if(TextUtils.isEmpty(name)) {
            Toast.makeText(getActivity(),"이름을 입력하세요",Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity(), "이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(getActivity(), "전화번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            Toast.makeText(getActivity(), "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else if(!PASSWORD_PATTERN.matcher(password).matches()) {
            Toast.makeText(getActivity(), "비밀번호 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    //OnAuth(task.getResult().getUser());
                    firebaseAuth.signOut();
                    Toast.makeText(getActivity(), "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
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

    */
}
