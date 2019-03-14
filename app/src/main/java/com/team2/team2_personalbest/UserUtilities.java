package com.team2.team2_personalbest;

public class UserUtilities {
    public static int emailToUniqueId(String email){
        int hashVal = 7;
        for (int i = 0; i < (email.length()-10); i++) {
            hashVal = hashVal*31 + email.charAt(i);
        }
        return hashVal;
    }
}
