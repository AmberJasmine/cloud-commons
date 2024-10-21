package org.example.until.sign;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5 {

    public static String md5(String text, String key) throws Exception {
        return DigestUtils.md5Hex(text + key);
    }

    public static String md52(String text) throws Exception {
        return DigestUtils.md5Hex(text);
    }

    public static boolean verify(String text, String key, String md5) throws Exception {
        String md5str = md5(text, key);
        return md5str.equalsIgnoreCase(md5);
    }

    public static void main(String[] args) throws Exception {
        String str = "陈陆";
        String s = MD5.md5(str, "何会敏");
        System.out.println("s = " + s);
        boolean verify = verify(str, "何会敏", s);
        System.out.println("verify = " + verify);

//        String str = "3104";
//        String asciiResult = stringToAscii(str);
//        System.out.println(asciiResult);
//        String stringResult = asciiToString(asciiResult);
//        System.out.println(stringResult);
    }

    public static String stringToAscii(String value) {
        StringBuilder sbu = new StringBuilder();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i != chars.length - 1) {
                sbu.append((int) chars[i]).append(",");
            } else {
                sbu.append((int) chars[i]);
            }
        }
        return sbu.toString();
    }

    public static String asciiToString(String value) {
        StringBuilder sbu = new StringBuilder();
        String[] chars = value.split(",");
        for (String aChar : chars) {
            sbu.append((char) Integer.parseInt(aChar));
        }
        return sbu.toString();
    }


}

