package com.gate.gateashram.Helpers;

public class Helper {
    public static String reformatString(String s) {
        s = s.trim();
        String s1 = " ";
        boolean z = true;
        for (int i = 0; i < s.length(); i++) {
            if (z) {
                s1 = s1 + Character.toString(s.charAt(i)).toUpperCase();
                z = false;
            } else {
                if (s.charAt(i) == ' ' || s.charAt(i) == '_') {
                    s1 = s1 + " ";
                    z = true;
                } else
                    s1 = s1 + Character.toString(s.charAt(i)).toLowerCase();
            }
        }
        s1 = s1.trim();
        return s1;
    }

}
