package com.zerobug.unite.mybike;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    private DashboardFragment dashboardFragment;
    private ProfileFragment profileFragment;
    private ContactFragment contactFragment;

    private BottomNavigationView mNavView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dashboardFragment = new DashboardFragment();
        profileFragment = new ProfileFragment();
        contactFragment = new ContactFragment();

        mNavView = findViewById(R.id.bottomNavigationView);
        
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
