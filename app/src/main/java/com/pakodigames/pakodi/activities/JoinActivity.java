package com.pakodigames.pakodi.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.pakodigames.pakodi.R;
import com.pakodigames.pakodi.adapters.PlayerAdapter;
import com.pakodigames.pakodi.model.Player;
import com.pakodigames.pakodi.model.Room;
import com.pakodigames.pakodi.utils.Util;

import java.util.List;

public class JoinActivity extends AppCompatActivity implements EventListener<QuerySnapshot> {

    private static final String TAG = JoinActivity.class.getName();

    private EditText editTv;
    private Button joinRoom;
    private ListView mPlayerListView;
    private PlayerAdapter mPlayerAdapter;

    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private ListenerRegistration mRegistration;
    private String roomDocumentId;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_layout);
        editTv = findViewById(R.id.edit_tv);
        joinRoom = findViewById(R.id.join_bt);
        mPlayerListView = findViewById(R.id.player_list);

        joinRoom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String roomId = editTv.getText().toString();
                initFirestore(roomId);
            }
        });
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
        // Handle errors
        if (e != null) {
            Log.w(TAG, "onEvent:error", e);
            return;
        }

        // Dispatch the event
        for (DocumentChange change : documentSnapshots.getDocumentChanges()) {
            // Snapshot of the changed document
            DocumentSnapshot snapshot = change.getDocument();
            switch (change.getType()) {
                case ADDED:
                    onDocumentAdded(change);
                    break;
                case MODIFIED:
                    onDocumentModified(change);
                    break;
                case REMOVED:
                    onDocumentRemoved(change);
                    break;
            }
            //mFirestore.collection("rooms").add(room);
        }
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
                Room room = snapshot.toObject(Room.class);
                updatePlayerList(room.getPlayers());
            } else {
                Log.d(TAG, "Current data: null");
            }
        }
    };

    protected void onDocumentAdded(DocumentChange change) {
        Log.i(TAG,"onDocumentAdded()");
    }

    protected void onDocumentModified(DocumentChange change) {
        Log.i(TAG,"onDocumentModified()");

    }

    protected void onDocumentRemoved(DocumentChange change) {
        Log.i(TAG,"onDocumentRemoved()");

    }

    private void initFirestore(String roomId) {
        mFirestore = FirebaseFirestore.getInstance();
        CollectionReference roomsRef = mFirestore.collection("rooms");
        mQuery = roomsRef.whereEqualTo("roomId", roomId);
        mQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for( DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                    roomDocumentId = doc.getDocument().getId();
                    Room room = doc.getDocument().toObject(Room.class);
                    Log.i(TAG,"room id -->"+roomDocumentId);
                    if(roomDocumentId != null) {
                        startListening();
                        addPlayer(room);
                    }
                }
            }
        });


    }

    private void addPlayer(Room room) {
        Player player = new Player(Util.getDeviceId(getContentResolver()), "Varun");
        room.addPlayer(player);
        mFirestore.collection("rooms").document(roomDocumentId)
                .set(room, SetOptions.merge());
    }

    public void startListening() {
        final DocumentReference docRef = mFirestore.collection("rooms").document(roomDocumentId);
        docRef.addSnapshotListener(roomListener);
    }

    private void updatePlayerList(List<Player> players){
        for(Player player : players) {
            Log.i(TAG, "change -->" + player.toString());
        }
        if(mPlayerAdapter == null) {
            mPlayerAdapter = new PlayerAdapter(players);
            mPlayerListView.setAdapter(mPlayerAdapter);
            mPlayerListView.setVisibility(View.VISIBLE);
        }else{
            mPlayerAdapter.updatePlayers(players);
        }
    }
}
