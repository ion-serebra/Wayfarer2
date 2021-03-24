package com.oshaev.wayfarer2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.oshaev.wayfarer2.ui.home.CountriesAdapter;

import java.util.UUID;

public class UserProfileActivity extends AppCompatActivity {

    private RecyclerView photosRecyclerView;
    private RecyclerView userCountriesRecyclerView;
    private FloatingActionButton addPhotoButton;
    private LinearLayoutManager photosManager;
    private LinearLayoutManager countriesManager;
    private UserPhotosAdapter photosAdapter;
    private CountriesAdapter countriesAdapter;

    private TextView userName;
    private TextView userSurname;

    DatabaseReference userPhotosRef;




    private UserProfileViewModel model;

    private User user;

    private static final int REQUEST_CODE = 121;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        UserProfileViewModel model = ViewModelProviders.of(this).get(UserProfileViewModel.class);

        user = new User();

        model.getUserData().observe(UserProfileActivity.this, new Observer<User>() {
            @Override
            public void onChanged(User currentUser) {
                user = currentUser;
                photosAdapter.photosUrlList = user.getUserPhotosList();
                countriesAdapter.countries = user.getUserCountries();
                countriesAdapter.setCountriesCopy(user.getUserCountries());
                userName.setText(user.getName());
                //Toast.makeText(UserProfileActivity.this, user.getUserCountries().get(0).getName(), Toast.LENGTH_SHORT).show();
                userSurname.setText(user.getSurname());
                countriesAdapter.notifyDataSetChanged();
                photosAdapter.notifyDataSetChanged();
            }
        });

        photosRecyclerView = findViewById(R.id.userPhotosRecyclerView);
        userCountriesRecyclerView = findViewById(R.id.userCountriesRecyclerView);
        userName = findViewById(R.id.userName);
        userSurname = findViewById(R.id.userSurname);

        countriesManager = new LinearLayoutManager(this);
        countriesManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        countriesAdapter = new CountriesAdapter(user.getUserCountries());
        userCountriesRecyclerView.setLayoutManager(countriesManager);
        userCountriesRecyclerView.setAdapter(countriesAdapter);
        countriesAdapter.notifyDataSetChanged();

        photosManager = new LinearLayoutManager(this);
        photosManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        photosAdapter = new UserPhotosAdapter(model.userPhotosUrlList, this);
        photosRecyclerView.setLayoutManager(photosManager);
        photosRecyclerView.setAdapter(photosAdapter);
        photosAdapter.notifyDataSetChanged();

        /*
        model.getUserPhotosData().observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                photosAdapter.photosUrlList = strings;
                photosAdapter.notifyDataSetChanged();
            }
        });
         */

      //  userPhotosRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUserKey()).child("userPhotosList");








        addPhotoButton = findViewById(R.id.addPhotoButton);

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(intent, "Выберите картинку"),
                        REQUEST_CODE);
            }
        });



    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK) { // загружаем картинки на сервер

            Uri selectedImageUri = data.getData();
            Uri filePath=data.getData();
            //Uri filePath = data.getData();
            //addSocialEventViewModel.uploadImage(selectedImageUri);

            if(filePath != null)
            {
                Toast.makeText(getApplicationContext(), "Start upload task", Toast.LENGTH_SHORT).show();
                final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
                progressDialog.setTitle("Uploading...");
                //progressDialog.show();
                //progressBar1.setVisibility(View.VISIBLE);
                //progressText1.setVisibility(View.VISIBLE);
                FirebaseStorage storage = FirebaseStorage.getInstance();
                final StorageReference eventStorageReference = storage.getReference()
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(filePath.getLastPathSegment());
                final String currentUUid = UUID.randomUUID().toString();
                final StorageReference ref = eventStorageReference.child(currentUUid);

                //Uri selectedImageUri = data.getData();
                //UploadTask uploadTask2 = eventStorageReference.putFile(selectedImageUri);

                UploadTask uploadTask = eventStorageReference.putFile(filePath);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return eventStorageReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            // if(imagesCount==0)
                            // {
                            //     titleImgUrl = downloadUri.toString();
                            // }
                            Toast.makeText(getApplicationContext(), "Complete", Toast.LENGTH_SHORT).show();

                            //user.setImgUrl(downloadUri.toString());

                            user.getUserPhotosList().add(downloadUri.toString());
                            Toast.makeText(UserProfileActivity.this,
                                    user.getName()+"\n"
                                            , Toast.LENGTH_SHORT).show();

                            DatabaseReference userRef = FirebaseDatabase.getInstance().
                                    getReference().child("users").child(user.getUserKey());
                            userRef.setValue(user);
                            //торопился, поэтому сделал не через ViewModel
                            //знаю, это плохая практика, но времени в обрез, до сдачи несколько часов
                            //зато работает

                            Log.e("jopa", downloadUri.toString());
                        }
                        else{}
                    }
                }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                    }
                });


                ref.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
/*
                                progressDialog.dismiss();
                                progressBar1.setVisibility(View.INVISIBLE);
                                progressText1.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplication(), "Uploaded", Toast.LENGTH_SHORT).show();
 */
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploaded "+(int)progress+"%");
                                //progressBar1.setProgress((int)progress);
                                //progressText1.setText((int)progress+"%");
                                Log.d("progress","Uploaded "+(int)progress+"%");
                            }
                        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        // item.setImageUrlString(task.getResult().toString());
                        //task.getResult();
                    }
                });


            }
        }

    }

    public void LogOut(View view) {
        FirebaseAuth.getInstance().signOut();
    }
}
