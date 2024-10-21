package org.example.test;

import java.util.Scanner;

public class Test001 {
    public static void main(String[] args) {


        int i = 0;
        while (i < 10) {
            i++;
            Scanner scanner = new Scanner(System.in);
            String s = scanner.nextLine();

//            String[] split = s.split(" ", -1);
//            List<String> list = Arrays.asList(split);
//            for (int i1 = 0; i1 < list.size(); i1++) {
//                System.out.println(">" + list.get(i1) + "<");
//            }
            long l = System.currentTimeMillis();
            trans(s, s.length());
            System.out.println(System.currentTimeMillis() - l);
        }


    }

    public static String trans(String s, int n) {
        String[] split = s.split(" ", -1);
        String o = "";
        for (int i = split.length - 1; i >= 0; i--) {
            String r = trans1(split[i], s.length());
            o = o + r + (i == 0 ? "" : " ");
        }
        System.out.println(o + "|");
        return o;
    }

    public static String trans1(String s, int n) {
        char t;
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < s.length(); i++) {
            t = s.charAt(i);
//            int i1 = s.charAt(i);
//            if (i1 >= 97 && i1 <= 122) {
//                int i2 = i1 - 32;
//                t = (char) i2;
//            }
//            if (i1 >= 65 && i1 <= 90) {
//                int i2 = i1 + 32;
//                t = (char) i2;
//            }
            if (Character.isUpperCase(t)) {
                t = Character.toLowerCase(t);
            } else {
                t = Character.toUpperCase(t);
            }

            stringBuilder.append(t);
        }
        return stringBuilder.toString();
    }


}
