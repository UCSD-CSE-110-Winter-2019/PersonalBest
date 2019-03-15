package com.team2.team2_personalbest;

import android.util.Pair;

import com.team2.team2_personalbest.IUser;

import java.util.List;

public class TestUser extends IUser {

    /*
        pre: ID is a user
        Used to add an app user to another App users FriendList given ID
     */
    boolean addFriend(int ID) {

    }

    /*
        Used to check if given friend's ID is already a friend of user
        ie: is in Users/UserID/Friends
     */
    boolean isFriend(int ID) {

    }

    /*
        pre: ID is a user
        Pre: Given ID is Users friend
        Used to remove a friend
        NOTE: delayed for this milestone, ignore unless everything else is done
     */
    boolean removeFriend(int ID) {

    }

    /*
        pre: ID is a user and is a friend
        Used to get a friends History of walks
     */
    List<Pair<Integer, Integer>> getWalks(int ID) {

    }

    /*
        true if user has init walks.
     */
    boolean hasWalks(int ID) {

    }

    /*
        Set 30 days Walks for this user in firebase
     */
    void setWalks(List<Pair<Integer, Integer>> walks) {

    }

    /*
        Used to get users most current friend list
     */
    List<Friend> getFriendList() {

    }

    /*
        Scans list of users and returns true or false
        Should be Used for checkers before add/remove friend
     */
    boolean isUser(int ID) {

    }

    /*
        Scans our list of users and returns user given ID
     */
    Friend getAppUser(int ID) {

    }
}
