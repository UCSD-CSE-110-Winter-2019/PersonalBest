package com.team2.team2_personalbest;

import android.util.Pair;

import java.util.List;

public abstract class IUser {

    //protected List<Friend> friendlist;

    // Make sure to init User
    protected Friend User;

    /*
        pre: ID is a user
        Used to add an app user to another App users FriendList given ID
     */
    abstract boolean addFriend(int ID);

    /*
        pre: ID is a user
        Pre: Given ID is Users friend
        Used to remove a friend
        NOTE: delayed for this milestone, ignore unless everything else is done
     */
    abstract boolean removeFriend(int ID);

    /*
        pre: ID is a user and is a friend
        Used to get a friends History of walks
     */
    abstract List<Pair<Integer, Integer>> getWalks(int ID);

    /*
        Used to get users most current friend list
     */
    abstract List<Friend> getFriendList();

    /*
        Scans list of users and returns true or false
        Should be Used for checkers before add/remove friend
     */
    abstract boolean isUser(int ID);

    /*
        Scans our list of users and returns user given ID
     */
    abstract Friend getAppUser(int ID);

    /*
       Adds a user to our total list of users
       Meant to be used on sign up
     */
    abstract void addUser(Friend user);


    /*
        Takes two UserID's and gives their absolute difference,
        Used to get ChatID for two users
        returns ChatId as integer
     */
    public int getChatwithFriend(Friend friendToChat){
        int user1 = User.userID;
        int user2 = friendToChat.userID;

        if(user1 >= user2)
            return user1-user2;
        else
            return user2-user1;
    }

    /*
        Private class Friend
        Note: User is also a 'Friend' datatype and shoukld be initialized as such
     */
    class Friend{
        String name;
        String address;
        String isPending;
        int userID;

        public Friend(String name, String address, String isPending){
            this.name = name;
            this.address = address;
            this.isPending = isPending;
            this.userID = emailToUniqueId(address);
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }

        public String getIsPending(){
            return isPending;
        }

        public int getUserID(){
            return userID;
        }



//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public void setAddress(String address) {
//            this.address = address;
//        }
//
//        public void setPending(String pending) {
//            isPending = pending;
//        }

        private int emailToUniqueId(String email){
            int hashVal = 7;
            for (int i = 0; i < email.length(); i++) {
                hashVal = hashVal*31 + email.charAt(i);
            }
            return hashVal;
        }

    }
}
