package com.example.flutter_comm.utils;

import java.util.Random;

public class Generating {
    private static String capitalCaseLetters = "LMNOPQRYZABCDEFGHIJKSTUVWX";
    private static String lowerCaseLetters = "ajklmnopqrstuvbcdefghiwxyz";
    private static String specialCharacters = "_@";
    private static String numbers = "1673459082";

    public static char[] generatePassword(int length, boolean isSpecialChar) {
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] password = new char[length];
        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        if (isSpecialChar) {
            password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        }else {
            password[2] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        }
        password[3] = numbers.charAt(random.nextInt(numbers.length()));
        for (int i = 4; i < length; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return password;
    }

    public static String generateUsername(String fullname) {
        String[] names = fullname.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String name : names) {
            sb.append(name.toLowerCase().charAt(0));
        }
        return sb.toString().concat("_"+ String.valueOf(generatePassword(4,false)));
    }
}