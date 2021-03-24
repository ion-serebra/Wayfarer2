package com.oshaev.wayfarer2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserProfileViewModel extends ViewModel {

    public ArrayList<String> userPhotosUrlList;
    //public ArrayList<String> userPhotosUrlList;
    private MutableLiveData<User> userData;
    private MutableLiveData<ArrayList<String>> userPhotosData;
    private DatabaseReference userReference;

    public static User user;

    public UserProfileViewModel() {
        userPhotosUrlList = new ArrayList<>();

        //user = new User();

        userData = new MutableLiveData<>();

        userReference = FirebaseDatabase.getInstance().getReference().child("users");
        getUserData();
    }

    public MutableLiveData<User> getUserData()
    {
        userReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                user = snapshot.getValue(User.class);
                user.setUserKey(snapshot.getKey());
                userPhotosUrlList = user.getUserPhotosList();
                userData.postValue(user);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return userData;
    }

    public MutableLiveData<ArrayList<String>> getUserPhotosData() {



            userReference.child(user.getUserKey()).child("userPhotosList").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    userPhotosData.postValue(snapshot.getValue(ArrayList.class));
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    userPhotosData.postValue(snapshot.getValue(ArrayList.class));
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            return userPhotosData;


    }
    public void updateUser()
    {
        userReference.child(user.getUserKey()).setValue(user);
    }




}
