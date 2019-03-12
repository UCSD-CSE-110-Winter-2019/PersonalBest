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
        //Log.d("ISUSER RESULT", "Val:"+ isUser(User.userID));
        if (!isUser(User.userID))
            addUser();

        Friend myFriendToAdd = new Friend("joey", "joey@gmail.com");
        addFriend(myFriendToAdd.userID);
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

    void setWalks(List<Pair<Integer, Integer>> walks) {
        db.collection("Users/"+User.userID+"/Walks")
                .add(walks)
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
    }

    /*
        pre: ID is a user
        Used to add an app user to another App users FriendList given ID
    */
    boolean addFriend(int ID){

        Map<String, Object> friends = new HashMap<>();
        friends.put("ID", ID);

        Log.d("ADD", "Adding Friend:"+ID+" to User:"+User.userID);
        if (isUser(ID)) {
            Log.d("ADD", "Friend:"+ID+" Is a User");
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
        return false;
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

        Friend user = new Friend("", "");
        return user;
    }

    /*
        Scans list of users and returns true or false
        Should be Used for checkers before add/remove friend
    */
    boolean isUser(int ID){

        // Create a reference to the cities collection
        CollectionReference UsersRef = db.collection("Users");

        // Create a query against the collection.
        Query query = UsersRef.whereEqualTo("UserID", ID);
        //Query query2 = UsersRef.whereEqualTo("/", ID);

        Log.d("ISUSER", "Executing Query Task");
        Task<QuerySnapshot> task = query.get();
        try{
            Log.d("ISUSER", "Awaiting Task");
            Tasks.await(task);
            Log.d("ISUSER", "Task Done");
            if (task.isSuccessful()) {
                Log.d("ISUSER", "Task was succesfull");
                QuerySnapshot document = task.getResult();
                List<DocumentSnapshot> docList = document.getDocuments();

                if (!docList.isEmpty())
                    return true;
                else
                    return false;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d("ISUSER", "Task Failed ?");
        return false;
    }

    /*
        Adds a user to our total list of users
        Meant to be used on sign up
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
