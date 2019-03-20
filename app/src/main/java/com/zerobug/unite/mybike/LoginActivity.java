package com.zerobug.unite.mybike;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private MaterialButton mLoginBtn;
    private TextInputEditText mId, mPassword;
    private ProgressBar mProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mLoginBtn = findViewById(R.id.tv_loginBtn);
        mId = findViewById(R.id.tv_id);
        mPassword = findViewById(R.id.tv_password);
        mProgressbar = findViewById(R.id.loginProgressbar);

        mAuth = FirebaseAuth.getInstance();



        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginEmail = mId.getText().toString();
                String loginPassword = mPassword.getText().toString();

                if (!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPassword)) {

                    mProgressbar.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(loginEmail, loginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                sendToMain();
                            } else {

                                String errorMessage = task.getException().getMessage();
                                //Log.i("Error: ", "errorMessage"  + errorMessage);
                                Toast.makeText(LoginActivity.this, "ERROR : " + errorMessage, Toast.LENGTH_LONG).show();

                            }

                            mProgressbar.setVisibility(View.INVISIBLE);

                        }
                    });
                }
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            sendToMain();
        }
    }

    private void sendToMain() {

        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();

    }

}
