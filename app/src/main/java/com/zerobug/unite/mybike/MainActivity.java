package com.zerobug.unite.mybike;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        FirebaseApp.initializeApp(this);

//        Intent loginIntent = new Intent(this, LoginActivity.class);
//        startActivity(loginIntent);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        //Dashboard instantiate
        DashboardFragment dashboardFragment = new DashboardFragment();

        fragmentTransaction.replace(R.id.container, dashboardFragment);
        fragmentTransaction.commit();

    }
}
