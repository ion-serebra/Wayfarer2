package com.oshaev.wayfarer2.ui.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.oshaev.wayfarer2.Article;
import com.oshaev.wayfarer2.Country;
import com.oshaev.wayfarer2.User;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {


    User currentUser;

    private DatabaseReference databaseReference;
    private DatabaseReference articlesReference;
    private DatabaseReference usersReference;
    private MutableLiveData<ArrayList<Country>> countriesMutableLiveData;
    private MutableLiveData<ArrayList<Article>> articlesMutableLiveData;
    private MutableLiveData<User> userData;
    public ArrayList<Country> countries;
    public ArrayList<Country> allCountries;
    public ArrayList<Article> articles;

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        currentUser = new User();
        mText = new MutableLiveData<>();
        countriesMutableLiveData = new MutableLiveData<>();
        articlesMutableLiveData = new MutableLiveData<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("countries");
        articlesReference = FirebaseDatabase.getInstance().getReference("articles");
        usersReference = FirebaseDatabase.getInstance().getReference("users");
        userData = new MutableLiveData<>();
        countries = new ArrayList<>();
        allCountries = new ArrayList<>();
        articles = new ArrayList<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<ArrayList<Country>> getCountriesMutableLiveData()
    {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Country country = snapshot.getValue(Country.class);

                    allCountries.add(country);
                if (country.isOpen()) {
                    countries.add(country);
                }
                countriesMutableLiveData.setValue(countries);
                //Log.e("countries", country.getName()+" ; "+ country.getImgUrl());
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

        return countriesMutableLiveData;
    }

    public LiveData<ArrayList<Article>> getArticlesMutableLiveData()
    {
        articlesReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Article article = snapshot.getValue(Article.class);
                articles.add(article);
                articlesMutableLiveData.postValue(articles);

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

        return articlesMutableLiveData;
    }

    public User getUser(final String Uid)
    {
        usersReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(User.class).getUid().equals(FirebaseAuth.getInstance().getUid()))
                {
                    currentUser = snapshot.getValue(User.class);
                    currentUser.setUserKey(snapshot.getKey());
                }
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
        return currentUser;
    }

    public MutableLiveData<User> getUser(final String Uid, boolean b)
    {
        usersReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(User.class).getUid().equals(FirebaseAuth.getInstance().getUid()))
                {
                    currentUser = snapshot.getValue(User.class);
                    currentUser.setUserKey(snapshot.getKey());
                    userData.postValue(currentUser);
                }
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

    public void getUserInModel(final String Uid)
    {
        usersReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(User.class).getUid().equals(FirebaseAuth.getInstance().getUid()))
                {
                    currentUser = snapshot.getValue(User.class);
                    currentUser.setUserKey(snapshot.getKey());
                }
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
    }

    public void updateUserCountries(String userKey, ArrayList<Country> userCountries)
    {
        usersReference.child(userKey).child("userCountries").setValue(userCountries);
    }

    public LiveData<String> getText() {
        return mText;
    }
}