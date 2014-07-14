package com.edwardyu.dtwfirstexperiment.app;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * Created by edwardyu on 11/07/14.
 */
public class TimeDataContainer implements Comparable<TimeDataContainer>{

    private long time;
    private double x;
    private double y;
    private double z;


//    private TimeDataContainer(long time) {
//        this.time = time;
//    }

    public TimeDataContainer(JSONObject entry) throws JSONException {
        time = Long.parseLong(entry.getString("st"));
        x = Double.parseDouble(entry.getString("x"));
        y = Double.parseDouble(entry.getString("y"));
        z = Double.parseDouble(entry.getString("z"));
    }

    public long getTime() {
        return time;
    }

    public double[] getData() {
        return new double[] {x, y, z};
    }


    @Override
    public int compareTo(TimeDataContainer other) {

        if (this.time < other.time) {
            return -1;
        } else if (time == other.time) {
            return 0;
        } else {
            return 1;
        }
    }

    public boolean equals (Object other) {
        if (!(other instanceof TimeDataContainer)) return false;
        if (this.compareTo((TimeDataContainer)other) == 0) return true;
        return false;
    }

    public String toString() {
        return "<" + time + ": " + x + " " + y + " " + z + ">";
    }

//    public static void main(String[] args) {
//        TimeDataContainer a = new TimeDataContainer(100);
//        TimeDataContainer b = new TimeDataContainer(100);
//        System.out.println("a is " + a);
//        System.out.println("b is " + b);
//        System.out.println("a = b? " + a.equals(b));
//        ArrayList<TimeDataContainer> list = new ArrayList<TimeDataContainer>();
//        list.add(a);
//        list.add(b);
//        TreeSet<TimeDataContainer> set = new TreeSet<TimeDataContainer>(list);
//
//        set.add(new TimeDataContainer(100));
//
//        System.out.println("Printing set");
//        for (TimeDataContainer c: set) {
//            System.out.println(c);
//        }
//
//        list = new ArrayList<TimeDataContainer>(set);
//        System.out.println("Printing list");
//        for (TimeDataContainer c: list) {
//            System.out.println(c);
//        }
//
//        ArrayList<Integer> ints = new ArrayList<Integer>();
//
//        ints.add(150);
//        ints.add(200);
//        ints.add(150);
//        HashSet<Integer> intSet = new HashSet<Integer>(ints);
//        for (Integer i: intSet) {
//            System.out.println(i);
//        }
//        System.out.println("Yep");
//    }
}
