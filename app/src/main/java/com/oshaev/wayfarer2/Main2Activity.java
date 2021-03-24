package com.oshaev.wayfarer2;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.oshaev.wayfarer2.ui.dashboard.DashboardFragment;
import com.oshaev.wayfarer2.ui.home.HomeFragment;
import com.oshaev.wayfarer2.ui.notifications.NotificationsFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class Main2Activity extends AppCompatActivity {

    final Fragment fragment11 = new HomeFragment();
    final Fragment fragment22 = new DashboardFragment();
    final Fragment fragment33 = new NotificationsFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment11;

    DatabaseReference userRef;

    UserProfileViewModel profileModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        fm.beginTransaction().add(R.id.nav_host_fragment, fragment33, "3").hide(fragment33).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment22, "2").hide(fragment22).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment11, "1").commit();

        UserProfileViewModel model = ViewModelProviders.of(this).get(UserProfileViewModel.class);

        userRef = FirebaseDatabase.getInstance().getReference("users");
        userRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(User.class).getUid().equals(FirebaseAuth.getInstance().getUid()))
                {
                    UserProfileViewModel.user = snapshot.getValue(User.class);
                    Toast.makeText(Main2Activity.this, "Authentication Success", Toast.LENGTH_SHORT).show();

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


//подписываемся на FirebaseMessaging чтобы иметь возможность получать уведомления прямо с сервера в любое время
        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed";
                        if (!task.isSuccessful()) {
                            msg ="Subscribe failed";
                        }
                        Log.d("tagmsg", msg);
                        Toast.makeText(Main2Activity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });



        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("logTag", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        //Log.d("logTag", msg);
                        //Toast.makeText(Main2Activity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


        navView.setSelectedItemId(R.id.navigation_home);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        fm.beginTransaction().hide(active).show(fragment11).commit();
                        active = fragment11;
                        return true;

                    case R.id.navigation_dashboard:
                        fm.beginTransaction().hide(active).show(fragment22).commit();
                        active = fragment22;
                        return true;

                    case R.id.navigation_notifications:
                        fm.beginTransaction().hide(active).show(fragment33).commit();
                        active = fragment33;
                        return true;
                }
                return false;
            }
        });

        Article article1 = new Article();

        navView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                //Toast.makeText(MainActivity.this, "Reselected", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
