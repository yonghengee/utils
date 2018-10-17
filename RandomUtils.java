package com.yqh.shop.utils;

import java.util.Random;

public class RandomUtils {

    /**
     * 获取8位随机字母
     * @return
     */
    public static String getRandomLetter(){
        int  maxNum = 26;
        int i;
        int count = 0;
        char[] str = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z' };
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while(count < 8){
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count ++;
            }
        }
        return pwd.toString();
    }

    /**
     * 获取16位随机数字
     * @return
     */
    public static String getRandomNumber(){
        int  maxNum = 10;
        int i;
        int count = 0;
        char[] str = { '1' , '2' , '3' , '4' , '5' , '6' , '7' , '8' , '9' , '0' };
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while(count < 12){
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count ++;
            }
        }
        return pwd.toString();
    }

    public static void main(String[] args) {
        System.out.println(getRandomNumber());
//        System.out.println(getRandomLetter());
    }
}
