package com.team2.team2_personalbest;

import android.util.Pair;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedList;
import java.util.List;

public class FirestoreUser extends IUser {

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    boolean addFriend(int ID){

        boolean ret = true;
        return ret;
    }
    boolean removeFriend(int ID){


        boolean ret = true;
        return ret;
    }


    /*
        pre: ID is a user and is a friend
        Used to get a friends History of walks
    */
    List<Pair<Integer, Integer>> getWalks(int ID){

        List<Pair<Integer, Integer>> walks = new LinkedList<>();
        return walks;
    }

    /*
        Used to get users most current friend list
     */
    List<Friend> getFriendList(){

        List<Friend> friendList = new LinkedList<>();
        return friendList;
    }

    /*
       Scans our list of users and returns user given ID
    */
    Friend getAppUser(int ID){

        Friend user = new Friend("", "", "");
        return user;
    }

    /*
        Scans list of users and returns true or false
        Should be Used for checkers before add/remove friend
    */
    boolean isUser(int ID){


        boolean ret = true;
        return ret;
    }

    /*
        Adds a user to our total list of users
        Meant to be used on sign up
    */
    void addUser(Friend user){


    }


}
