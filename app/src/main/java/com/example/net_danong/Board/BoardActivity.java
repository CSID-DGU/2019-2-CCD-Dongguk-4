package com.example.net_danong.Board;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.Menu;

import android.view.MenuItem;

import android.widget.LinearLayout;



import com.example.net_danong.BasicActivity;
import com.example.net_danong.Board.listener.OnBoardListener;
import com.example.net_danong.Board.view.ReadContentsVIew;
import com.example.net_danong.R;


import javax.annotation.Nullable;
public class BoardActivity extends BasicActivity {
    private BoardInfo boardInfo;
    private Board_FirebaseHelper firebaseHelper;
    private ReadContentsVIew readContentsVIew;
    private LinearLayout contentsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        boardInfo = (BoardInfo) getIntent().getSerializableExtra("boardInfo");
        contentsLayout = findViewById(R.id.contentsLayout);
        readContentsVIew = findViewById(R.id.readContentsView);

        firebaseHelper = new Board_FirebaseHelper(this);
        firebaseHelper.setOnBoardListener(onBoardListener);
        uiUpdate();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    boardInfo = (BoardInfo) data.getSerializableExtra("boardinfo");
                    contentsLayout.removeAllViews();
                    uiUpdate();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        setToolbarTitle(boardInfo.getTitle());
        readContentsVIew.setBoardInfo(boardInfo);
    }

    private void myStartActivity(Class c, BoardInfo postInfo) {
        Intent intent = new Intent(this, c);
        intent.putExtra("boardInfo", boardInfo);
        startActivityForResult(intent, 0);
    }
}
