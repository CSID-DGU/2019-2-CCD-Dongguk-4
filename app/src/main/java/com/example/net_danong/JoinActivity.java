package com.example.net_danong;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

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
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

      /*  check1 = (AppCompatCheckBox) findViewById(R.id.register_check_1);
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
        });*/
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}