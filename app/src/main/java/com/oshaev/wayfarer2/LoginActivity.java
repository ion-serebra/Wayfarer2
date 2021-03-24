package com.oshaev.wayfarer2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "taggg";

    FirebaseDatabase database;
    DatabaseReference usersDatabaseReference;

    Button loginButton;
    private FirebaseAuth auth;
    private EditText nameEditText;
    private EditText surnameEditText;
    private EditText groupEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private TextView changeLogRegTextView;

    boolean loginModeActive = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);

        database = FirebaseDatabase.getInstance();
        usersDatabaseReference = database.getReference().child("users");

        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null)
        {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            //usersDatabaseReference.child("users")
        }


        nameEditText = findViewById(R.id.nameEditText);
        surnameEditText = findViewById(R.id.surnameEditText);
        groupEditText = findViewById(R.id.groupEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        changeLogRegTextView = findViewById(R.id.changeLogRegTextView);
        loginButton = findViewById(R.id.loginButton);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(emailEditText.getText().toString()!=""
                        && passwordEditText.getText().toString()!="")
                loginSignUpUser(emailEditText.getText().toString().trim(),
                        passwordEditText.getText().toString().trim());


                //startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });



    }

    private void loginSignUpUser(String email, String password){

        try {


            if (loginModeActive) {

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = auth.getCurrentUser();
                                    //createUser(user);
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("userName", nameEditText.getText().toString().trim());
                                    intent.putExtra("userSurname", surnameEditText.getText().toString().trim());
                                    intent.putExtra("userGroup", groupEditText.getText().toString().trim());

                                    //intent.putExtra("userEmail", emailEditText.getText().toString().trim());
                                    startActivity(intent);
                                    //updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                    // ...
                                }

                                // ...
                            }
                        });
            } else {
                if (passwordEditText.getText().toString() != null && confirmPasswordEditText.
                        getText().toString() != null) {
                    if (passwordEditText.getText().toString().trim().equals(confirmPasswordEditText.
                            getText().toString().trim())) {
                        auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "createUserWithEmail:success");
                                            FirebaseUser user = auth.getCurrentUser();
                                            createUser(user);
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            intent.putExtra("userName", nameEditText.getText().toString().trim());
                                            intent.putExtra("userSurname", surnameEditText.getText().toString().trim());
                                            intent.putExtra("userGroup", groupEditText.getText().toString().trim());
                                            //intent.putExtra("userEmail", emailEditText.getText().toString().trim());
                                            startActivity(intent);
                                            //  updateUI(user);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                            //updateUI(null);
                                        }

                                        // ...
                                    }
                                });
                    } else {
                        Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                    }
                }
            }
//}
        } catch (Exception e){

        }

    }

    private void createUser(FirebaseUser firebaseUser)
    {

        User user = new User();
        user.setUid(firebaseUser.getUid());
        user.setEmail(firebaseUser.getEmail());
        user.setName(nameEditText.getText().toString().trim());
        user.setSurname(surnameEditText.getText().toString().trim());
        user.setImgUrl(null);
        usersDatabaseReference.push().setValue(user);

    }


    public void changeForm(View view) {

        if(loginModeActive)
        {
            loginModeActive = false;
            confirmPasswordEditText.setVisibility(View.VISIBLE);
            nameEditText.setVisibility(View.VISIBLE);
            surnameEditText.setVisibility(View.VISIBLE);
            //groupEditText.setVisibility(View.VISIBLE);
            loginButton.setText("Зарегистрироваться");
            changeLogRegTextView.setText("или войти");
        } else {
            loginModeActive = true;
            confirmPasswordEditText.setVisibility(View.GONE);
            nameEditText.setVisibility(View.GONE);
            surnameEditText.setVisibility(View.GONE);
            groupEditText.setVisibility(View.GONE);
            loginButton.setText("Войти");
            changeLogRegTextView.setText("или зарегистрироваться");
        }

    }
}
