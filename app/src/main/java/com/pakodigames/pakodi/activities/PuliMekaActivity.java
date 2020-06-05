package com.pakodigames.pakodi.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;
import com.pakodigames.pakodi.R;
import com.pakodigames.pakodi.model.Player;
import com.pakodigames.pakodi.model.PuliMekaRoom;
import com.pakodigames.pakodi.model.Room;
import com.pakodigames.pakodi.views.PuliMekaView;

public class PuliMekaActivity extends Activity implements PuliMekaView.IPositionClickListener {

    private static final String TAG = PuliMekaActivity.class.getName();
    private PuliMekaView mPuliMekaView;
    private PuliMekaRoom mPuliMekaRoom;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private ListenerRegistration registration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puli_meka_layout);
        mPuliMekaView = findViewById(R.id.puli_meka_view);
        mPuliMekaView.setOnPositionClickListener(this);
    }

    @Override
    public void onPositionClickListener(int position) {
        mPuliMekaRoom.nextTurn();
        updateRoomDb();
    }

    private void updateRoomData() {
        final DocumentReference docRef = mFirestore.collection("rooms").document(Room.currentRoomId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                mPuliMekaRoom = documentSnapshot.toObject(PuliMekaRoom.class);
                updateUI();
            }
        });
    }

    private EventListener roomListener = new EventListener<DocumentSnapshot>() {
        @Override
        public void onEvent(@Nullable DocumentSnapshot snapshot,
                            @Nullable FirebaseFirestoreException e) {
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }
            if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, "Current data: " + snapshot.getData());
                mPuliMekaRoom = snapshot.toObject(PuliMekaRoom.class);
                updateUI();
            } else {
                Log.d(TAG, "Current data: null");
            }
        }
    };



    private void updateUI() {
        mPuliMekaView.setPuliMekaRoom(mPuliMekaRoom);
        if(mPuliMekaRoom.getLambsDead() >= 5){
            Toast.makeText(PuliMekaActivity.this, "Tigers Won!!",Toast.LENGTH_SHORT).show();
        }
        if(mPuliMekaRoom.getLastMoveJump() != -1){

        }else{
            if(mPuliMekaRoom.getLastMoveStart() != -1 && mPuliMekaRoom.getLastMoveEnd() != -1){

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerListener();
        updateRoomData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterListener();
    }

    private void updateRoomDb() {
        mFirestore.collection("rooms").document(Room.currentRoomId)
                .set(mPuliMekaRoom, SetOptions.merge());
    }

    private boolean isUserTurn() {
        return Player.currentPlayer.getPlayerId().equals(mPuliMekaRoom.getPlayerTurn());
    }

    public void registerListener() {
        mFirestore.collection("rooms").document(Room.currentRoomId);
        final DocumentReference docRef = mFirestore.collection("rooms").document(Room.currentRoomId);
        registration = docRef.addSnapshotListener(roomListener);
    }

    public void unregisterListener() {
        registration.remove();
    }
}
