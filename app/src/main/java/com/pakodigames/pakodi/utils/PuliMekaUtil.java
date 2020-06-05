package com.pakodigames.pakodi.utils;

import android.graphics.Point;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PuliMekaUtil {

    private static HashMap<Integer, List<Integer>> adjacentList = new HashMap(){{
        put(0, Arrays.asList(2,3,4,5));
        put(1,Arrays.asList(2,7));
        put(2,Arrays.asList(0,1,3,8));
        put(3,Arrays.asList(0,2,4,9));
        put(4,Arrays.asList(0,3,5,10));
        put(5,Arrays.asList(0,4,6,11));
        put(6,Arrays.asList(5,12));
        put(7,Arrays.asList(1,8,13));
        put(8,Arrays.asList(2,7,9,14));
        put(9,Arrays.asList(3,8,10,15));
        put(10,Arrays.asList(4,9,11,16));
        put(11,Arrays.asList(5,10,12,17));
        put(12,Arrays.asList(6,11,18));
        put(13,Arrays.asList(7,14));
        put(14,Arrays.asList(8,13,15,19));
        put(15,Arrays.asList(9,14,16,20));
        put(16,Arrays.asList(10,15,17,21));
        put(17,Arrays.asList(11,16,18,22));
        put(18,Arrays.asList(12,17));
        put(19,Arrays.asList(14,20));
        put(20,Arrays.asList(15,19,21));
        put(21,Arrays.asList(16,20,22));
        put(22,Arrays.asList(17,21));
    }};

    private static HashMap<Point, Integer> possibleJumpMap = new HashMap(){{
        put(new Point(0,8), 2);
        put(new Point(0,9), 3);
        put(new Point(0,10), 4);
        put(new Point(0,11), 5);

        put(new Point(1,3), 2);
        put(new Point(1,13), 7);

        put(new Point(2,4), 3);
        put(new Point(2,14), 8);

    }};

    public static List<Integer> getAdjacentList(int position){
        return adjacentList.get(position);
    }

    public static int getJumpOverPosition(int start, int end){
        return -1;
    }
}
