package com.team2.team2_personalbest;

import android.util.Pair;

import java.util.List;

public abstract class IUser {


    protected List<Friend> friendlist;

//    //Interface for DB requirements
//    getFriends(): return list of Friend objects that contain {gmail address, name, boolean of whether or not the request is “pending”}
//
//    addFriend(gmail address): adds Friend obj to current user’s Friend list in DB
//
//    removeFriend(gmail address): remove Friend obj from current user’s Friend list in DB
//
//    getWalks(gmail address): return the last 30 days in an array of pair values (planned, unplanned)
//
    abstract boolean addFriend(String name);
    abstract boolean removeFriend(String name);
    abstract List<Pair<Float, Float>> getWalks(String name);
    abstract List<Friend> getFriendlist();
    abstract Friend getAppUser(String email);

    static class Friend {
        String name;
        String address;
        String isPending;

        public Friend(String name, String address, String isPending){
            this.name = name;
            this.address = address;
            this.isPending = isPending;
        }

        public Friend(String name, String address, boolean isPending) {
            this.name = name;
            this.address = address;
            this.isPending = isPending;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setPending(String pending) {
            isPending = pending;
        }

        // Added by Daniel to be able to print the Friend info in the friends list
        public String toString() {
            return "  Name: " + this.name + "   |     Email: " + this.address;
        }
    }
}
