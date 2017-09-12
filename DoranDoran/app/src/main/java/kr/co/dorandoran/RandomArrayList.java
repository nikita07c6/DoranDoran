package kr.co.dorandoran;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by HanAYeon on 2016-09-06.
 */

public class RandomArrayList {

    private static ArrayList<Integer> imageResources;

    static {
        imageResources = new ArrayList<Integer>();
    imageResources.add(R.drawable.kkott);
    imageResources.add(R.drawable.cheongju);
    imageResources.add(R.drawable.cheongju_education);
    imageResources.add(R.drawable.chungbuk);
    imageResources.add(R.drawable.jungwon);
    imageResources.add(R.drawable.u1);
    imageResources.add(R.drawable.transportation);
    imageResources.add(R.drawable.chungbuk_health_science);
    imageResources.add(R.drawable.konkuk);
    imageResources.add(R.drawable.chunkbuk_provincial);
    imageResources.add(R.drawable.knue);
    imageResources.add(R.drawable.daewon);
    imageResources.add(R.drawable.gangdong);
    imageResources.add(R.drawable.semyung);
    imageResources.add(R.drawable.seowon);

    }

    private static HashMap<Integer, String> nameMaps;

    static {
        nameMaps = new HashMap<Integer, String>();
        nameMaps.put(R.drawable.kkott, "김건국");
        nameMaps.put(R.drawable.gangdong, "김건국");
        nameMaps.put(R.drawable.daewon, "김건국");
        nameMaps.put(R.drawable.knue, "구기");
        nameMaps.put(R.drawable.chunkbuk_provincial, "구기");
        nameMaps.put(R.drawable.konkuk, "비비비");
        nameMaps.put(R.drawable.chungbuk_health_science, "비비비");
        nameMaps.put(R.drawable.cheongju, "님밍");
        nameMaps.put(R.drawable.cheongju_education, "님밍");
        nameMaps.put(R.drawable.chungbuk, "님밍");
        nameMaps.put(R.drawable.jungwon, "님밍");
        nameMaps.put(R.drawable.u1, "님밍");
        nameMaps.put(R.drawable.transportation, "님밍");
        nameMaps.put(R.drawable.semyung, "님밍");
        nameMaps.put(R.drawable.seowon, "님밍");

    }

    private static Random random = new Random(System.currentTimeMillis());

    public static ArrayList<Integer> getSuffleArrayList() {
       // Collections.shuffle(imageResources, random);
        return imageResources;
    }

    public static String getGirlGroupName(Integer key){
        return nameMaps.get(key);
    }
}
