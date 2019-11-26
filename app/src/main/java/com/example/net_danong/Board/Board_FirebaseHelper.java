package com.example.net_danong.Board;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.example.net_danong.Board.listener.OnBoardListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static com.example.net_danong.Board.Util.isStorageUrl;
import static com.example.net_danong.Board.Util.showToast;
import static com.example.net_danong.Board.Util.storageUrlToName;

public class Board_FirebaseHelper {
    private Activity activity;
    private OnBoardListener onBoardListener;
    private int successCount;

    public Board_FirebaseHelper(Activity activity) {
        this.activity = activity;
    }

    public void setOnBoardListener(OnBoardListener onBoardListener){
        this.onBoardListener = onBoardListener;
    }

    public void storageDelete(final BoardInfo boardInfo){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        final String id = boardInfo.getId();
        ArrayList<String> contentsList = boardInfo.getContents();
        for (int i = 0; i < contentsList.size(); i++) {
            String contents = contentsList.get(i);
            if (isStorageUrl(contents)) {
                successCount++;
                StorageReference desertRef = storageRef.child("posts/" + id + "/" + storageUrlToName(contents));
                desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        successCount--;
                        storeDelete(id, boardInfo);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        showToast(activity, "Error");
                    }
                });
            }
        }
        storeDelete(id, boardInfo);
    }

    private void storeDelete(final String id, final BoardInfo postInfo) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        if (successCount == 0) {
            firebaseFirestore.collection("posts").document(id)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showToast(activity, "게시글을 삭제하였습니다.");
                            onBoardListener.onDelete(postInfo);
                            //postsUpdate();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showToast(activity, "게시글을 삭제하지 못하였습니다.");
                        }
                    });
        }
    }

}
