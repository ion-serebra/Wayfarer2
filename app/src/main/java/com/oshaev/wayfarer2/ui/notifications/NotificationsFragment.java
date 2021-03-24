package com.oshaev.wayfarer2.ui.notifications;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.oshaev.wayfarer2.Article;
import com.oshaev.wayfarer2.Country;
import com.oshaev.wayfarer2.R;

import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    EditText editText;
    EditText summaryEditText;
    EditText paperEditText;
    Button addFlag;
    Button addCountry;
    Button addArticle;
    DatabaseReference reference;
    DatabaseReference articlesReference;


    Country country;
    Article article;

    ToggleButton openButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        editText = root.findViewById(R.id.countryNameEditText);
        summaryEditText =  root.findViewById(R.id.summaryEditText);
        paperEditText =  root.findViewById(R.id.paperEditText);

        addFlag = root.findViewById(R.id.addFlag);
        addCountry = root.findViewById(R.id.addCountry);
        openButton = root.findViewById(R.id.openButton);
        addArticle = root.findViewById(R.id.addArticle);


        country = new Country();
        article = new Article();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        reference = db.getReference().child("countries");
        articlesReference = db.getReference().child("articles");


        addCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                country.setName(editText.getText().toString());
                country.setOpen(openButton.isChecked());

            reference.push().setValue(country);

            country.setImgUrl("");
                editText.setText("");
                summaryEditText.setText("");
                paperEditText.setText("");
                //article = null;
                //country= null;

            }
        });

        addArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                article.setTitle(editText.getText().toString());
                article.setSummary(summaryEditText.getText().toString());
                article.setPaper(paperEditText.getText().toString());

                articlesReference.push().setValue(article);
                article = null;
                country= null;
                editText.setText("");
                summaryEditText.setText("");
                paperEditText.setText("");

            }
        });


        addFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(intent, "Выберите картинку"),
                        111);
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 111 && resultCode == RESULT_OK) { // загружаем картинки на сервер

            Uri selectedImageUri = data.getData();
            Uri filePath=data.getData();
            //Uri filePath = data.getData();
            //addSocialEventViewModel.uploadImage(selectedImageUri);

            if(filePath != null)
            {
                Toast.makeText(getContext(), "Start upload task", Toast.LENGTH_SHORT).show();
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("Uploading...");
                //progressDialog.show();
                //progressBar1.setVisibility(View.VISIBLE);
                //progressText1.setVisibility(View.VISIBLE);
                FirebaseStorage storage = FirebaseStorage.getInstance();
                final StorageReference eventStorageReference = storage.getReference()
                        .child("countriesImage").child(editText.getText().toString());
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
                            Toast.makeText(getContext(), "Complete", Toast.LENGTH_SHORT).show();

                            country.setImgUrl(downloadUri.toString());
                            article.setImgUrl(downloadUri.toString());
                            Log.e("jopa", downloadUri.toString());
                        }
                        else{}
                    }
                }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
}
