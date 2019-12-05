package com.example.net_danong;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;


import com.example.net_danong.R;
import com.example.net_danong.ProductWriteInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Utilities for Restaurants.
 */
public class ProductUtil {

    private static final String TAG = "ProductUtil";

    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(2, 4, 60,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    private static final String RESTAURANT_URL_FMT = "https://storage.googleapis.com/firestorequickstarts.appspot.com/food_%d.png";

    private static final int MAX_IMAGE_NUM = 22;

    private static final String[] TITLE_FIRST_WORDS = {
            "맛있는 농작물",
            "싱싱한 농작물",
            "초록초록한 농작물",
            "방금 딴 농작물",
            "황금 농작물",
            "진짜 존맛탱 농작물",
            "Goooood Product",
            "나만 먹고싶은 농작물",
            "몸에 좋은 농작물"
    };
    private static final String[] TITLE_SECOND_WORDS = {
            "팔아요",
            "팜",
            "팝니다",
            "팔게요",
            "ㅍㅍ",
            "파니까 연락주세요"
    };
    private static final String[] PRODUCT_WORDS = {
            "과일1",
            "과일2",
            "과일3",
            "과일4",
            "채소1",
            "채소2",
            "채소3",
            "채소4",
            "약초1",
            "약초2",
            "약초3",
            "약초4"
    };
    private static final String[] PUBLICHER_WORD = {
            "판매자1",
            "판매자2",
            "판매자3",
            "판매자4",
            "판매자5",
            "판매자6",
            "판매자7",
            "판매자8",
    };

    /**
     * Create a random Restaurant POJO.
     */
    public static ProductWriteInfo getRandom(Context context) {
        ProductWriteInfo product = new ProductWriteInfo();
        Random random = new Random();
        Geocoder geocoder = new Geocoder(context);

        // Cities (first elemnt is 'Any')
        //String[] cities = context.getResources().getStringArray(R.array.cities);
        //cities = Arrays.copyOfRange(cities, 1, cities.length);

        //Cities 예시
        String[] cities = new String[]{"충무로역", "동국대학교 서울캠퍼스", "을지로3가역","동대입구역","대한극장"};

        // Categories (first element is 'Any')
        String[] categories = context.getResources().getStringArray(R.array.categories);
        categories = Arrays.copyOfRange(categories, 1, categories.length);
        String[] prices = new String[]{"10000", "20000", "30000"};

        product.setTitle(getRandomTitle(random));
        product.setProduct(getRandomProduct(random));
        product.setPrice(getPriceString(prices, random));
        //product.setLocation(getRandomString(cities, random));
        product.setLocation(getCityString(cities, random));
        product.setContents(getRandomContents(random));
        product.setPublisher(getRandomPublisher(random));
        product.setCategory(getRandomString(categories, random));
        product.setAvgRating(getRandomRating(random));
        product.setNumRatings(random.nextInt(20));

        //주소 이름으로 위도, 경도 변환해서 저장하는 거
        String city = product.getLocation();
        System.out.println("주목!!!!!!!!!!!!주목!!!!!!!!"+city);
        List<Address> addressList = null;
        try {
            addressList = geocoder.getFromLocationName(city, 10);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
        }
        if (addressList != null) {
            if (addressList.size() == 0) {
                Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
            }
        }
        System.out.println(addressList.get(0).toString());

        double latitude = addressList.get(0).getLatitude(); //위도
        double longitude = addressList.get(0).getLongitude(); //경도

        //위도, 경도 설정
        product.setLatitude(latitude);
        product.setLongitude(longitude);


        return product;
    }

    private static String getRandomLoc(Random random) {
        return getRandomString(PUBLICHER_WORD, random);

    }

    private static String getRandomPublisher(Random random) {
        return getRandomString(PUBLICHER_WORD, random);

    }

    private static String getRandomContents(Random random) {
        return getRandomString(TITLE_FIRST_WORDS, random) + " "
                + getRandomString(TITLE_SECOND_WORDS, random) + " "
                + getRandomString(TITLE_FIRST_WORDS, random) + " "
                + getRandomString(TITLE_SECOND_WORDS, random);

    }


    private static String getRandomTitle(Random random) {
        return getRandomString(TITLE_FIRST_WORDS, random) + " "
                + getRandomString(TITLE_SECOND_WORDS, random);
    }
    private static String getRandomProduct(Random random) {
        return getRandomString(PRODUCT_WORDS, random);
    }
    public static String getPriceString(String[] array, Random random) {
        int ind = random.nextInt(array.length);
        return array[ind];
    }

    public static String getCityString(String[] array, Random random) {
        int ind = random.nextInt(array.length);
        return array[ind];
    }

    private static String getRandomImageUrl(Random random) {
        // Integer between 1 and MAX_IMAGE_NUM (inclusive)
        int id = random.nextInt(MAX_IMAGE_NUM) + 1;

        return String.format(Locale.getDefault(), RESTAURANT_URL_FMT, id);
    }

    /**
     * Get price represented as dollar signs.
     */


    private static double getRandomRating(Random random) {
        double min = 1.0;
        return min + (random.nextDouble() * 4.0);
    }
    private static String getRandomString(String[] array, Random random) {
        int ind = random.nextInt(array.length);
        return array[ind];
    }
}
