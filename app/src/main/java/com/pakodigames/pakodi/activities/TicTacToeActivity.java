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
import com.pakodigames.pakodi.model.Room;
import com.pakodigames.pakodi.model.TicTacToeRoom;
import com.pakodigames.pakodi.views.TicTacToeView;

public class TicTacToeActivity extends Activity implements TicTacToeView.IOnGridClickListener {

    private String TAG = TicTacToeActivity.class.getName();
    private TicTacToeView ticTacToeView;

    private TicTacToeRoom mTicTacToeRoom;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private ListenerRegistration registration;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        ticTacToeView = findViewById(R.id.tictactoe_view);

        ticTacToeView.onAddGridClickListener(this);
    }

    private void updateRoomData() {
        final DocumentReference docRef = mFirestore.collection("rooms").document(Room.currentRoomId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                mTicTacToeRoom = documentSnapshot.toObject(TicTacToeRoom.class);
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
                mTicTacToeRoom = snapshot.toObject(TicTacToeRoom.class);
                updateUI();
            } else {
                Log.d(TAG, "Current data: null");
            }
        }
    };

    private void updateUI() {
        ticTacToeView.updateUI(mTicTacToeRoom);
        if(mTicTacToeRoom.isWon() != null){
            if(mTicTacToeRoom.isWon().equals(Player.currentPlayer.getPlayerId())) {
                Toast.makeText(TicTacToeActivity.this, "You Won!!", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(TicTacToeActivity.this, "You Lost!!", Toast.LENGTH_SHORT).show();
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
                .set(mTicTacToeRoom, SetOptions.merge());
    }

    private boolean isUserTurn() {
        return Player.currentPlayer.getPlayerId().equals(mTicTacToeRoom.getPlayerTurn());
    }

    public void registerListener(){
        mFirestore.collection("rooms").document(Room.currentRoomId);
        final DocumentReference docRef = mFirestore.collection("rooms").document(Room.currentRoomId);
        registration = docRef.addSnapshotListener(roomListener);
    }

    public void unregisterListener(){
        registration.remove();           //<-- This is the key
    }

    @Override
    public void onGridClicked(int position) {
        if(isUserTurn() && mTicTacToeRoom.getGameState().get(position) == 0){
            mTicTacToeRoom.getGameState().set(position, mTicTacToeRoom.getPlayerPosition(Player.currentPlayer.getPlayerId()));
            mTicTacToeRoom.nextTurn();
            updateRoomDb();
        }
    }
}
