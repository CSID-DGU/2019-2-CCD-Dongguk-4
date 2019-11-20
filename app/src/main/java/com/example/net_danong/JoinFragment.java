package com.example.net_danong;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.regex.Pattern;

// firebaseAuth..? 받아오는 과정에서 바로 앱다운되길래 일단 주석 처리 했습니다,,ㅠ_ㅠ

public class JoinFragment extends Fragment {

    //비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");
    //이메일 정규식
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9]@[a-zA-Z0-9].[a-zA-Z0-9]");
    //파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;
    DatabaseReference mdatabase;

    //이메일과 비밀번호
    private EditText editName, editTextEmail, editTextPassword, editPhoneNumber;
    private String name, email, password, phoneNumber;
    private Context context;

    public int TERMS_AGREE_1 = 0; //No Check = 0, Check = 1
    public int TERMS_AGREE_2 = 0;
    public int TERMS_AGREE_3 = 0;

    AppCompatCheckBox checkBox1; //첫번째 동의
    AppCompatCheckBox checkBox2; //두번째 동의
    AppCompatCheckBox checkBox3; //모두 동의

    // 각각의 Fragment마다 Instance를 반환해 줄 메소드를 생성
    public static JoinFragment newInstance() {
        return new JoinFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_join, container, false);

        //파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();

        context = container.getContext();
        editName = view.findViewById(R.id.nameText);
        editTextEmail = view.findViewById(R.id.emailText);
        editTextPassword = view.findViewById(R.id.passwordText);
        editPhoneNumber = view.findViewById(R.id.phonenumberText);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        final CheckBox checkBox1 = (CheckBox) view.findViewById(R.id.register_check_1);
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    TERMS_AGREE_1 = 1;
                } else {
                    TERMS_AGREE_1 = 0;
                }
            }
        });
        final CheckBox checkBox2 = (CheckBox) view.findViewById(R.id.register_check_2);
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    TERMS_AGREE_2 = 1;
                } else {
                    TERMS_AGREE_2 = 0;
                }
            }
        });
        CheckBox checkBox3 = (CheckBox)view.findViewById(R.id.register_check_3);
        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    checkBox1.setChecked(true);
                    checkBox2.setChecked(true);
                    TERMS_AGREE_3 = 1;
                } else {
                    checkBox1.setChecked(false);
                    checkBox2.setChecked(false);
                    TERMS_AGREE_3 = 0;
                }
            }
        });
        Button button_finish = (Button)view.findViewById(R.id.register_btn_finish);
        button_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //전체 약관 체크 여부
                if(TERMS_AGREE_3 !=1) {
                    //첫번째 약관 체크 여부
                    if(TERMS_AGREE_2 == 1) {
                        //두번째 약관 체크 여부
                        if(TERMS_AGREE_1 == 1){
                            UserRegister();
                        }else {
                            Toast.makeText(context, "약관을 체크해주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        Toast.makeText(context, "약관을 체크해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } //전체 약관 체크된 경우
                else {
                    UserRegister();
                }
            }

        });

        return view;
    }

    private void UserRegister() {
        name = editName.getText().toString().trim();
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        phoneNumber = editPhoneNumber.getText().toString().trim();

        if(TextUtils.isEmpty(name)) {
            Toast.makeText(context, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(context, "이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(context, "전화번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            Toast.makeText(context, "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else if(!PASSWORD_PATTERN.matcher(password).matches()) {
            Toast.makeText(context, "비밀번호 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    OnAuth(task.getResult().getUser());
                    firebaseAuth.signOut();
                    Toast.makeText(context, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
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

