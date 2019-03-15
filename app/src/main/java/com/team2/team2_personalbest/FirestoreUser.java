package com.team2.team2_personalbest;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

public class FirestoreUser extends IUser {

    // Make sure to init User
    protected Friend User;
    protected FirebaseFirestore db;

    /*
        Constructor
     */
    public FirestoreUser(String name, String email){

        Friend me = new Friend(name, email);

        //get Firestore
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        User = me;

        // Check if User needs to be added to firestore
        // (Usually if its their first time)
        if (!isUser(User.userID))
            addUser();

        if (!hasWalks(User.userID))
            setWalks(getDummyWalks());


        //Testing Add Friend
        //Friend myFriendToAdd = new Friend("joey", "joey@gmail.com");
        //addFriend(myFriendToAdd.userID);


        //Testing getWalksfor this user
//        List<Pair<Integer, Integer>> walkList = getWalks(User.userID);
//        for (int i=0; i<walkList.size(); i++){
//            String date = DateHelper.dayDateToString(DateHelper.previousDay(i));
//            Log.d("GET WALKS FOR THIS USER", "\nDate: "+date+"\nPlanned:"+walkList.get(i).first
//                                                        +"\nUnplanned:"+walkList.get(i).second+"\nXXXXXX\n");
//        }
    }

    /*
        Takes two UserID's and gives their absolute difference,
        Used to get ChatID for two users
        returns ChatId as integer
    */
    private int getChatID(Friend friendToChat){
        int user1 = User.userID;
        int user2 = friendToChat.userID;

        if(user1 >= user2)
            return user1-user2;
        else
            return user2-user1;
    }

    /*
        Input is a List of Integer Pairs
        Adds 30 Days Walk Data to Firestore under User History
     */
    void setWalks(List<Pair<Integer, Integer>> walks) {
        for(int i=0; i<walks.size(); i++) {

            Date day = DateHelper.previousDay(i);
            Pair<Integer, Integer> stepsPair = walks.get(i);

            //Key to store under
            String date = DateHelper.dayDateToString(day);


            Map<String, Object> dayDataMap = new HashMap<>();
            dayDataMap.put("Planned", stepsPair.first);
            dayDataMap.put("Unplanned", stepsPair.second);

            db.collection("Users")
                    .document(Integer.toString(User.userID))
                    .collection("/Walks/")
                    .document(date)
                    .set(dayDataMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
        }
    }

    /*
        pre: ID is a user
        Used to add an app user to another App users FriendList given ID
        Returns True if successful Add, False otherwise
        TODO Maybe add a Toast when user tries to add incorrect friend or pre-existing friend
    */
    boolean addFriend(int ID){

        //TODO replace this log with a Toast!, giving the right activity context
        if(isFriend(ID)) {
            Log.d("ADD", "Trying to add :" + ID + "\nWho is already a friend of yours!");
            return false;
        }

        //TODO replace this log with a Toast!, giving the right activity context
        if(!isUser(ID)){
            Log.d("ADD", "Trying to add :"+ ID +"\nWho is not an App User (yet) !");
            return false;
        }


        // If this Friend is not already a Friend
        // And this Friend is a User
        Map<String, Object> friends = new HashMap<>();
        friends.put("FriendID", ID);

        Log.d("ADD", "Adding Friend:"+ID+" to User:"+User.userID);
        // Add a new document with a generated ID
        db.collection("Users/" + User.userID + "/Friends")
                .add(friends)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        return true;
    }


    /*
        Used to check if given friend's ID is already a friend of user
        ie: is in Users/UserID/Friends
     */
    boolean isFriend(int ID){

        // Create a reference to the UsersFriends collection
        CollectionReference usersFriendsRef = db.collection("Users/"+User.userID+"/Friends");

        // Create a query against the collection to get This User
        Query query = usersFriendsRef.whereEqualTo("FriendID", ID);

        Log.d("IS_FRIEND", "Executing Query Task");
        Task<QuerySnapshot> task = query.get();
        try{
            Log.d("IS_FRIEND", "Awaiting Task");
            Tasks.await(task);
            Log.d("IS_FRIEND", "Task Done");
            if (task.isSuccessful()) {
                Log.d("IS_FRIEND", "Task was successful");
                QuerySnapshot document = task.getResult();
                List<DocumentSnapshot> docList = document.getDocuments();

                if (!docList.isEmpty())
                    return true;
                else
                    return false;
            }
        } catch (ExecutionException e) {
            Log.d("IS_FRIEND", "Query Failed to execute");
            e.printStackTrace();
        } catch (InterruptedException e) {
            Log.d("IS_FRIEND", "Query Failed due to interruption");
            e.printStackTrace();
        }

        Log.d("IS_FRIEND", "Task Failed");
        return false;
    }


    boolean removeFriend(int ID){

        boolean ret = true;
        return ret;
    }

    /*
        Private helper method to populate dummy users made for testing
     */
    private List<Pair<Integer, Integer>> getDummyWalks(){
        List<Pair<Integer, Integer>> retList = new ArrayList<>();
        for (int i=0; i<30; i++) {
            Pair<Integer, Integer> initPair = new Pair<>(-1,-1);
            retList.add(initPair);
        }
        return retList;
    }


    /*
        pre: ID is a user and is a friend
        Used to get a friends History of walks
    */
    List<Pair<Integer, Integer>> getWalks(int ID){

        if(!isUser(ID)){
            Log.d("GET_WALKS", "Getting walks for :" + ID + "\nWho is not an App User");
            return null;
        }

        // ID is a user and is a friend of this user
        // Create a reference to This Friends Walk History collection
        CollectionReference friendsWalkHistoryRef = db.collection("Users/"+ID+"/Walks");

        List<Pair<Integer, Integer>> walksList = new LinkedList<>();

        //Iterate for 30 Days
        for (int i=0; i<30; i++) {

            //Date is the name of the document which contains planned and unplanned
            String date = DateHelper.dayDateToString(DateHelper.previousDay(i));

            // Create a query against the collection to get Friends Walks on this Date
            Task<DocumentSnapshot> task = friendsWalkHistoryRef.document(date).get();
            Log.d("GET_WALKS", "Executing Query Task");
            try {
                Log.d("GET_WALKS", "Awaiting Task");
                Tasks.await(task);
                Log.d("GET_WALKS", "Task Done");
                if (task.isSuccessful()) {
                    Log.d("GET_WALKS", "Task was successful");
                    DocumentSnapshot document = task.getResult();
                    long plannedLong;
                    long unplannedLong;
                    try {
                        plannedLong = (long) document.get("Planned");
                    } catch (NullPointerException e) {
                        plannedLong = 0;
                    }
                    try {
                        unplannedLong = (long) document.get("Unplanned");
                    } catch (NullPointerException e) {
                        unplannedLong = 0;
                    }


                    int planned = Math.toIntExact(plannedLong);
                    int unplanned = Math.toIntExact(unplannedLong);

                    //add to walkList
                    walksList.add(i, new Pair<>(planned, unplanned));
                }
            } catch (ExecutionException e) {
                Log.d("GET_WALKS", "Query Failed to execute");
                e.printStackTrace();
            } catch (InterruptedException e) {
                Log.d("GET_WALKS", "Query Failed due to interruption");
                e.printStackTrace();
            }
        }
        Log.d("GET_WALKS", "GET_WALKS successful with size - "+walksList.size());
        return walksList;
    }


    /*
        True if user has walks
    */
    boolean hasWalks(int ID){

        if(!isUser(ID)){
            Log.d("GET_WALKS", "Getting walks for :" + ID + "\nWho is not an App User");
            return false;
        }

        CollectionReference UserWalkRef = db.collection("Users").document(Integer.toString(ID)).collection("Walks");

        Log.d("HAS_WALKS", "Executing Query Task");
        Task<QuerySnapshot> task = UserWalkRef.get();
        try{
            Log.d("HAS_WALKS", "Awaiting Task");
            Tasks.await(task);
            Log.d("HAS_WALKS", "Task Done");
            if (task.isSuccessful()) {
                Log.d("HAS_WALKS", "Task was successful");
                QuerySnapshot document = task.getResult();
                List<DocumentSnapshot> docList = document.getDocuments();

                if (!docList.isEmpty())
                    return true;
                else
                    return false;
            }
        } catch (ExecutionException e) {
            Log.d("IS_USER", "Query Failed to execute");
            e.printStackTrace();
        } catch (InterruptedException e) {
            Log.d("IS_USER", "Query Failed due to interruption");
            e.printStackTrace();
        }

        Log.d("IS_USER", "Task Failed");
        return false;
    }

    /*
        Used to get users most current friend list
     */
    List<Friend> getFriendList(){

        // Create a reference to this Users Friend
        CollectionReference friendsListRef = db.collection("Users/"+User.userID+"/Friends");

        List<Friend> friendList = new LinkedList<>();

        // Create a query against the collection to get Friends Walks on this Date
        Task<QuerySnapshot> task = friendsListRef.get();
        //Task<QuerySnapshot> task = query.get();
        try{
            Log.d("GET_FRIEND_LIST", "Awaiting Task");
            Tasks.await(task);
            Log.d("GET_FRIEND_LIST", "Task Done");
            if (task.isSuccessful()) {
                Log.d("GET_FRIEND_LIST", "Task was successful");
                QuerySnapshot querySnapshot = task.getResult();

                // List of all Documents in Friends
                // Each Doc corresponds to a UserID
                List<DocumentSnapshot> docList = querySnapshot.getDocuments();
                for(int i=0; i< docList.size(); i++){

                    DocumentSnapshot document = docList.get(i);
                    long friendIDLong = (long)document.get("FriendID");
                    int friendID = Math.toIntExact(friendIDLong);

                    // Call to method which gets Friend given ID from DB
                    Friend thisFriend = getAppUser(friendID);

                    // Add thisFriend to FriendList
                    friendList.add(thisFriend);
                }
            }
        } catch (ExecutionException e) {
            Log.d("GET_FRIEND_LIST", "Query Failed to execute");
            e.printStackTrace();
        } catch (InterruptedException e) {
            Log.d("GET_FRIEND_LIST", "Query Failed due to interruption");
            e.printStackTrace();
        }

        Log.d("GET_FRIEND_LIST", "Task Failed");
        return friendList;
    }

    /*
       TODO
       Scans our list of users and returns user as Friend Type given ID
    */
    Friend getAppUser(int ID){
        Friend appUser = new Friend("", "");
        CollectionReference UsersRef = db.collection("Users");
        // Create a query against the collection to get This User
        Query query = UsersRef.whereEqualTo("UserID", ID);
        Log.d("GET_USER", "Executing Query Task to get user: "+ID);
        Task<QuerySnapshot> task = query.get();
        try{
            Log.d("GET_USER", "Awaiting Task");
            Tasks.await(task);
            Log.d("GET_USER", "Task Done");
            if (task.isSuccessful()) {
                Log.d("GET_USER", "Task was successful for getting user: "+ID);
                QuerySnapshot document = task.getResult();
                List<DocumentSnapshot> docList = document.getDocuments();
                String email = docList.get(0).get("email").toString();
                String name = docList.get(0).get("name").toString();
                appUser = new Friend(name, email);

                Log.d("GETTING_USER_DEB", "Size:"+docList.size());
            }
        } catch (ExecutionException e) {
            Log.d("GET_FRIEND_LIST", "Query Failed to execute");
            e.printStackTrace();
        } catch (InterruptedException e) {
            Log.d("GET_FRIEND_LIST", "Query Failed due to interruption");
            e.printStackTrace();
        }
        Log.d("GET_FRIEND_LIST", "Task Failed");
        return appUser;
    }

    /*
        Scans list of users and returns true or false
        Should be Used for checkers before add/remove friend
    */
    boolean isUser(int ID){

        // Create a reference to the Users collection
        CollectionReference UsersRef = db.collection("Users");

        // Create a query against the collection to get This User
        Query query = UsersRef.whereEqualTo("UserID", ID);

        Log.d("IS_USER", "Executing Query Task");
        Task<QuerySnapshot> task = query.get();
        try{
            Log.d("IS_USER", "Awaiting Task");
            Tasks.await(task);
            Log.d("IS_USER", "Task Done");
            if (task.isSuccessful()) {
                Log.d("IS_USER", "Task was successful");
                QuerySnapshot document = task.getResult();
                List<DocumentSnapshot> docList = document.getDocuments();

                if (!docList.isEmpty())
                    return true;
                else
                    return false;
            }
        } catch (ExecutionException e) {
            Log.d("IS_USER", "Query Failed to execute");
            e.printStackTrace();
        } catch (InterruptedException e) {
            Log.d("IS_USER", "Query Failed due to interruption");
            e.printStackTrace();
        }

        Log.d("IS_USER", "Task Failed");
        return false;
    }


    /*
        Adds a user to our total list of users
        Meant to be used on sign up for each User
    */
    private void addUser(){
        // Create a new user To add to Firestore userList
        Map<String, Object> user = new HashMap<>();
        user.put("name", User.name);
        user.put("email", User.address);
        user.put("UserID", User.userID);

        // Add a new document with a generated ID
        db.collection("Users").document(Integer.toString(User.userID))
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
}
