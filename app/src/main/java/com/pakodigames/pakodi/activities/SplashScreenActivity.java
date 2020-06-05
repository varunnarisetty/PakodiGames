package com.pakodigames.pakodi.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.pakodigames.pakodi.R;
import com.pakodigames.pakodi.model.Player;

public class SplashScreenActivity extends Activity {
    private static int SPLASH_SCREEN_TIME_OUT=1500;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.splash);
        mAuth = FirebaseAuth.getInstance();
        Intent i;
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            //First time login
            i = new Intent(SplashScreenActivity.this, SignInActivity.class);
            //Intent is used to switch from one activity to another.

            startActivity(i);
            //invoke the SecondActivity.
            finish();
            //the current activity will get finished.
        }else {
            String playerId = currentUser.getUid();
            getPlayerWithPlayerId(playerId);
        }


    }

    private void getPlayerWithPlayerId(String playerId) {
        mFirestore = FirebaseFirestore.getInstance();
        CollectionReference playersRef = mFirestore.collection("players");
        playersRef.whereEqualTo("playerId", playerId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    Player.currentPlayer = queryDocumentSnapshots.getDocuments().get(0).toObject(Player.class);
                    openGameDashboardActivity();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void openGameDashboardActivity() {
        Intent i = new Intent(SplashScreenActivity.this, GameDashboardActivity.class);
        startActivity(i);
        finish();
    }
}
