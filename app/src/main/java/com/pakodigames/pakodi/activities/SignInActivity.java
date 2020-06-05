package com.pakodigames.pakodi.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pakodigames.pakodi.R;
import com.pakodigames.pakodi.model.Player;

public class SignInActivity extends Activity {

    private static final String TAG = SignInActivity.class.getName();
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private EditText nick_et;
    private Button create_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout);
        mAuth = FirebaseAuth.getInstance();
        nick_et = (EditText) findViewById(R.id.editText);
        create_btn = (Button)findViewById(R.id.button);
        create_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!nick_et.getText().toString().isEmpty()){
                    addPlayer();
                }else{
                    Toast.makeText(SignInActivity.this, "Please enter a valid nick name.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addPlayer(){
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if(user !=null) {
            String userId = user.getUid();
            Player player = new Player(userId, nick_et.getText().toString());
            Player.currentPlayer = player;
            insetPlayerInFireDB(player);
        }
    }

    private void insetPlayerInFireDB(Player player) {
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection("players").add(player)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                openGameDashboardActivity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignInActivity.this, "Unable to create Player profile on the cloud.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGameDashboardActivity() {
        Intent i = new Intent(SignInActivity.this, GameDashboardActivity.class);
        startActivity(i);
        finish();
    }
}
