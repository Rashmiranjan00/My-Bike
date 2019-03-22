package com.zerobug.unite.mybike;


import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    private DashboardFragment dashboardFragment;
    private ProfileFragment profileFragment;
    private ContactFragment contactFragment;

    private BottomNavigationView mNavView;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    private String playData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();

        dashboardFragment = new DashboardFragment();
        profileFragment = new ProfileFragment();
        contactFragment = new ContactFragment();

        mNavView = findViewById(R.id.bottomNavigationView);

        mAuth = FirebaseAuth.getInstance();


        if (mAuth.getCurrentUser() != null) {
            initializeFragment();

            mNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.mainContainer);

                    switch (item.getItemId()) {

                        case R.id.bottomActionHome:
                            replaceFragment(dashboardFragment, currentFragment);
                            return true;

                        case R.id.bottomActionProfile:
                            replaceFragment(profileFragment, currentFragment);
                            return true;

                        case R.id.bottomActionContact:
                            replaceFragment(contactFragment, currentFragment);
                            return true;

                        default:
                            return false;


                    }
                }
            });

        }

        playMusic();

    }

    private void playMusic() {

        mRef.child("noti").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                playData = dataSnapshot.getValue().toString();
                Log.d("PlayMusic", playData);
                if (!playData.equals("null")) {
                    int myMusic = getResources().getIdentifier(playData, "raw", "com.zerobug.unite.mybike");

                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), myMusic);
                    mediaPlayer.start();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null) {
            sendToLogin();
        }
        super.onStart();
    }

    private void sendToLogin() {

        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();

    }

    private void initializeFragment() {


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mainContainer, dashboardFragment);
        fragmentTransaction.add(R.id.mainContainer, profileFragment);
        fragmentTransaction.add(R.id.mainContainer, contactFragment);

        fragmentTransaction.hide(profileFragment);
        fragmentTransaction.hide(contactFragment);
        fragmentTransaction.commit();

    }

    private void replaceFragment(Fragment fragment, Fragment currentFragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (fragment == dashboardFragment) {

            fragmentTransaction.hide(profileFragment);
            fragmentTransaction.hide(contactFragment);

        }

        if (fragment == profileFragment) {

            fragmentTransaction.hide(dashboardFragment);
            fragmentTransaction.hide(contactFragment);

        }

        if (fragment == contactFragment) {

            fragmentTransaction.hide(dashboardFragment);
            fragmentTransaction.hide(profileFragment);

        }

        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();

    }

}
