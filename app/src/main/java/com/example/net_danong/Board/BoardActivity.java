package com.example.net_danong.Board;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.net_danong.Board.listener.OnBoardListener;
import com.example.net_danong.Board.view.ReadContentsVIew;
import com.example.net_danong.Menu5Fragment;
import com.example.net_danong.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.annotation.Nullable;

public class BoardActivity extends Fragment {
    private BoardInfo boardInfo;
    private Board_FirebaseHelper firebaseHelper;
    private ReadContentsVIew readContentsVIew;
    private LinearLayout contentsLayout;
    private FirebaseAuth mAuth;
    public FirebaseUser currentUser;


    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_board, container, false);

        boardInfo = (BoardInfo) getIntent().getSerializableExtra("boardInfo");
        contentsLayout = view.findViewById(R.id.contentsLayout);
        readContentsVIew = view.findViewById(R.id.readContentsView);

        firebaseHelper = new Board_FirebaseHelper(this);
        firebaseHelper.setOnBoardListener(onBoardListener);
        currentUser = mAuth.getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        uiUpdate();

        Button writeBtn = (Button)view.findViewById(R.id.BoardWritdBtn);
        writeBtn.setOnClickListener(new Button.OnClickListener(){
           @Override
           public void onClick(View view) {
               if (currentUser != null) {
                   Intent intent1 = new Intent(getActivity(), WriteBoardActivity.class);
               } else {
                   Intent intent2 = new Intent(getActivity(), Menu5Fragment.class);
               }


           }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    boardInfo = (BoardInfo)data.getSerializableExtra("boardinfo");
                    contentsLayout.removeAllViews();
                    uiUpdate();
                }
                break;
        }
    }

    public void onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.board, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                firebaseHelper.storageDelete(boardInfo);
                return true;
            case R.id.modify:
                myStartActivity(WriteBoardActivity.class, boardInfo);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    OnBoardListener onBoardListener = new OnBoardListener() {
        @Override
        public void onDelete(BoardInfo boardInfo) {
            Log.e("로그 ","삭제 성공");
        }

        @Override
        public void onModify() {
            Log.e("로그 ","수정 성공");
        }
    };

    private void uiUpdate(){

        readContentsVIew.setBoardInfo(boardInfo);
    }

    private void myStartActivity(Class c, BoardInfo boardInfo) {
        Intent intent = new Intent(this, c);
        intent.putExtra("boardInfo", boardInfo);
        startActivityForResult(intent, 0);
    }
}
