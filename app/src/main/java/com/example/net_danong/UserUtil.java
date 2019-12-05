/*
package com.example.net_danong;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Random;
import java.util.concurrent.Executor;T=
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class UserUtil{

    private static final String TAG = "ProductUtil";
    static FirebaseAuth firebaseAuth;
    static FirebaseFirestore mdb;
    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(2, 4, 60,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    static FieldValue time;
    private static final int MAX_IMAGE_NUM = 22;
    static Random random;
    private static final String[] EMAIL_FIRST_WORDS = {
            "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y",
            "Z","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"
    };
    private static final String[] EMAIL_SECOND_WORDS = {
        "naver.com", "daum.com", "google.com"
    };
    private static final String[] NUM_WORDS = "1,2,3,4,5,6,7,8,9,0".split(",");
    private static final String[] USER_NAME_WORLD = {
            "사용자1", "사용자2", "사용자3", "사용자4", "사용자5", "사용자6", "사용자7",
            "사용자8", "사용자9", "사용자10"
    };
    static String email;
    static String id;
    static String password;
    */
/**
     * Create a random User POJO.
     *//*

    public static User addUserUtil() {
        id = "";
        email = "";
        password = "";

        random = new Random();
        time = FieldValue.serverTimestamp();

        id = getRandomID(random);
        email = id + getRandomEmail(random);

        password = id;
        firebaseAuth = FirebaseAuth.getInstance();
        mdb = FirebaseFirestore.getInstance();

        User user = new User(firebaseAuth.getCurrentUser().getUid(), getDisplayName(random), email, getRandomPhoneNum(random));
        return user;

        }

    private static String getRandomID(Random random) {
        for(int i=1;i<5;i++){
            id += getRandomString(EMAIL_FIRST_WORDS, random);
        }
        for(int i=1;i<5;i++){
            id += getRandomString(NUM_WORDS, random);
        }
        return id;
    }
    private static String getRandomEmail(Random random) {
        return "@" + getRandomString(EMAIL_SECOND_WORDS,random);
    }

    private static String getDisplayName(Random random) {
        return getRandomString(USER_NAME_WORLD, random);

    }


    private static String getRandomPhoneNum(Random random) {
        String phoneNumber = "010";
        for(int i=0;i<8;i++){
            phoneNumber += getRandomString(NUM_WORDS, random);
        }
        return phoneNumber;
    }



    */
/**
     * Get price represented as dollar signs.
     *//*



    private static String getRandomString(String[] array, Random random) {
        int ind = random.nextInt(array.length);
        return array[ind];
    }
}*/
