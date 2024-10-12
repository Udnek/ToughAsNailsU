package me.udnek.toughasnailsu.util;

public class Utils {

    public static boolean isSameSign(double a, double b){
        return a*b >= 0;
    }

    public static String roundForDebug(Double v){
        return String.format("%.2f", v);
    }

}
