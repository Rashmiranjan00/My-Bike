package com.zerobug.unite.mybike;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private MaterialButton mSignoutBtn;
    private View mProfileView;
    private FirebaseAuth mAuth;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mProfileView =  inflater.inflate(R.layout.fragment_profile, container, false);

        mSignoutBtn = mProfileView.findViewById(R.id.signOutBtn);

        mAuth = FirebaseAuth.getInstance();
        
        mSignoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        return mProfileView;
    }

    private void signOut() {
        mAuth.signOut();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

}
