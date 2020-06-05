package com.pakodigames.pakodi.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.pakodigames.pakodi.R;
import com.pakodigames.pakodi.adapters.PlayerAdapter;
import com.pakodigames.pakodi.model.ConnectFourRoom;
import com.pakodigames.pakodi.model.Player;
import com.pakodigames.pakodi.model.PuliMekaRoom;
import com.pakodigames.pakodi.model.Room;
import com.pakodigames.pakodi.model.TicTacToeRoom;
import com.pakodigames.pakodi.utils.Util;

import java.security.SecureRandom;
import java.util.List;

public class GameDashboardActivity extends Activity {

    private static final String TAG = GameDashboardActivity.class.getName();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference roomsRef = db.collection("rooms");
    private TextView mCreateTextView;
    private TextView mJoinTextView;
    private TextView mRoomIdTv;
    private ImageView tictacIv;
    private ImageView connect4Iv;
    private ImageView puliMekaIv;
    private EditText roomIdEt;
    private Button joinBtn;
    private ListView mListView;
    private Button mStartBtn;
    private PlayerAdapter mPlayerAdapter;
    private Room mRoom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_dashboard);
        tictacIv = findViewById(R.id.tictac_iv);
        connect4Iv = findViewById(R.id.connect_4_iv);
        puliMekaIv = findViewById(R.id.puli_meka_iv);
        roomIdEt = findViewById(R.id.room_id_et);
        joinBtn = findViewById(R.id.join_btn);
        mCreateTextView = findViewById(R.id.textView);
        mJoinTextView = findViewById(R.id.textView2);
        mListView = findViewById(R.id.players_list);
        mStartBtn = findViewById(R.id.start_btn);
        mRoomIdTv = findViewById(R.id.room_id_tv);

        roomIdEt.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            if(!roomIdEt.getText().toString().isEmpty()) {
                                String roomId = roomIdEt.getText().toString();
                                addPlayerToRoom(roomId);
                                Util.hideKeyboard(GameDashboardActivity.this);
                            }else{
                                Toast.makeText(GameDashboardActivity.this,"Please enter a valid room id",Toast.LENGTH_LONG).show();
                            }
                            return true;
                        }
                        return false;
                    }
                });
        joinBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(!roomIdEt.getText().toString().isEmpty()){
                    String roomId = roomIdEt.getText().toString();
                    addPlayerToRoom(roomId);
                    Util.hideKeyboard(GameDashboardActivity.this);
                }
            }
        });

        tictacIv.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                createNewRoom(Room.TIC_TAC_TOE);
            }
        });

        connect4Iv.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                createNewRoom(Room.CONNECT_FOUR);
            }
        });

        puliMekaIv.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
//                Toast.makeText(GameDashboardActivity.this,"Coming soon...",Toast.LENGTH_LONG).show();
//                Intent i = new Intent(GameDashboardActivity.this, PuliMekaActivity.class);
//                startActivity(i);
                createNewRoom(Room.PULI_MEKA);
            }
        });

        mStartBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(mRoom != null){
                    mRoom.setState(Room.PROGRESS);
                    mRoom.setPlayerTurn(Player.currentPlayer.getPlayerId());
                    db.collection("rooms").document(Room.currentRoomId)
                            .set(mRoom, SetOptions.merge());
                }
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
                mRoom = snapshot.toObject(Room.class);
                if(mRoom.getState() == Room.PROGRESS){
                    startGameActivity();
                }else {
                    showPlayersList(mRoom.getPlayers());
                }
            } else {
                Log.d(TAG, "Current data: null");
            }
        }
    };

    private void startGameActivity() {
        Intent i = null;
        if(mRoom.getRoomType() == Room.TIC_TAC_TOE){
            i = new Intent(GameDashboardActivity.this, TicTacToeActivity.class);
        }else if(mRoom.getRoomType() == Room.CONNECT_FOUR){
            i = new Intent(GameDashboardActivity.this, ConnectFourActivity.class);
        }else if(mRoom.getRoomType() == Room.PULI_MEKA){
            i = new Intent(GameDashboardActivity.this, PuliMekaActivity.class);
        }
        startActivity(i);
        finish();
    }

    private void createNewRoom(int roomType) {
        final String roomId = getRandomId();
       if(roomType == Room.TIC_TAC_TOE){
           roomsRef.add(new TicTacToeRoom(roomId, Player.currentPlayer))
                   .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                       @Override
                       public void onSuccess(DocumentReference documentReference) {
                           Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                           Log.d(TAG, "Room created with RoomId: " + roomId);
                           Toast.makeText(GameDashboardActivity.this,"Success",Toast.LENGTH_LONG).show();
                           Room.currentRoomId = documentReference.getId();
                           startListening();
                       }
                   })
                   .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Log.w(TAG, "Error adding document", e);
                           Toast.makeText(GameDashboardActivity.this,"Failed to create a new Room",Toast.LENGTH_LONG).show();
                       }
                   });
       }else if(roomType == Room.CONNECT_FOUR){
           roomsRef.add(new ConnectFourRoom(roomId, Player.currentPlayer))
                   .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                       @Override
                       public void onSuccess(DocumentReference documentReference) {
                           Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                           Log.d(TAG, "Room created with RoomId: " + roomId);
                           Toast.makeText(GameDashboardActivity.this,"Success",Toast.LENGTH_LONG).show();
                           Room.currentRoomId = documentReference.getId();
                           startListening();
                       }
                   })
                   .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Log.w(TAG, "Error adding document", e);
                           Toast.makeText(GameDashboardActivity.this,"Failed to create a new Room",Toast.LENGTH_LONG).show();
                       }
                   });
       }else if(roomType == Room.PULI_MEKA){
           roomsRef.add(new PuliMekaRoom(roomId, Player.currentPlayer))
                   .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                       @Override
                       public void onSuccess(DocumentReference documentReference) {
                           Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                           Log.d(TAG, "Room created with RoomId: " + roomId);
                           Toast.makeText(GameDashboardActivity.this,"Success",Toast.LENGTH_LONG).show();
                           Room.currentRoomId = documentReference.getId();
                           startListening();
                       }
                   })
                   .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Log.w(TAG, "Error adding document", e);
                           Toast.makeText(GameDashboardActivity.this,"Failed to create a new Room",Toast.LENGTH_LONG).show();
                       }
                   });
       }

    }

    private void addPlayerToRoom(String roomId) {
        CollectionReference roomsRef = db.collection("rooms");
        Query mQuery = roomsRef.whereEqualTo("roomId", roomId);
        mQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty()){
                    Toast.makeText(GameDashboardActivity.this,"Invalid Room ID",Toast.LENGTH_LONG).show();
                }
                for( DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                    Room.currentRoomId  = doc.getDocument().getId();
                    Room room = doc.getDocument().toObject(Room.class);
                    room.setState(Room.WAITING);
                    Log.i(TAG,"room id -->"+Room.currentRoomId);
                    if(Room.currentRoomId != null) {
                        startListening();
                        room.addPlayer(Player.currentPlayer);
                        db.collection("rooms").document(Room.currentRoomId)
                                .set(room, SetOptions.merge());
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(GameDashboardActivity.this,"Failed to find the Room with give roomid",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startListening() {
        final DocumentReference docRef = db.collection("rooms").document(Room.currentRoomId);
        docRef.addSnapshotListener(GameDashboardActivity.this,roomListener);
    }

    private void showPlayersList(List<Player> players){

        for(Player player : players) {
            Log.i(TAG, "change -->" + player.toString());
        }
        if(mPlayerAdapter == null) {
            mPlayerAdapter = new PlayerAdapter(players);
            mListView.setAdapter(mPlayerAdapter);
            mListView.setVisibility(View.VISIBLE);
        }else{
            mPlayerAdapter.updatePlayers(players);
        }
        tictacIv.setVisibility(View.GONE);
        connect4Iv.setVisibility(View.GONE);
        puliMekaIv.setVisibility(View.GONE);
        roomIdEt.setVisibility(View.GONE);
        joinBtn.setVisibility(View.GONE);
        mCreateTextView.setVisibility(View.GONE);
        mJoinTextView.setVisibility(View.GONE);


        mRoomIdTv.setText(mRoom.getRoomId());
        mListView.setVisibility(View.VISIBLE);
        if(players.size() >= 2)
        mStartBtn.setVisibility(View.VISIBLE);
    }

    private String getRandomId() {
        SecureRandom rand = new SecureRandom();
        int random = rand.nextInt(100000);
        return String.format("5%s",random);
    }

}
