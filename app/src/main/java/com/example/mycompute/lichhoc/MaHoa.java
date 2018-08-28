package com.example.mycompute.lichhoc;

import java.lang.reflect.Array;

public class MaHoa {

    public static String encode(String s){

        char[] c = s.toCharArray();
        for(int i=0;i<c.length;i++){
            c[i]= (char) (c[i]+i);
        }
        return String.valueOf(c);
    }
    public static String decode(String s){

        char[] c = s.toCharArray();
        for(int i=c.length-1;i>=0;i--){
            c[i]= (char) (c[i]-i);
        }
        return String.valueOf(c);
    }


}
