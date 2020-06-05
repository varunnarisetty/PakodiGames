package com.pakodigames.pakodi.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.pakodigames.pakodi.model.ConnectFourRoom;
import com.pakodigames.pakodi.model.Player;
import com.pakodigames.pakodi.model.Room;

public class ConnectFourActivity extends Activity {
    private static String TAG = ConnectFourActivity.class.getName();
    private ImageView[][] cells;
    private View boardView;
    private ViewHolder viewHolder;
    public static int NUM_ROWS = 6;
    public static int NUM_COLS = 7;
    private ConnectFourRoom mConnectFourRoom;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private ListenerRegistration registration;

    private class ViewHolder {
        public TextView winnerText;
        public ImageView turnIndicatorImageView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        boardView = findViewById(R.id.game_board);
        buildCells();
        boardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_POINTER_UP:
                    case MotionEvent.ACTION_UP: {
                        int col = colAtX(motionEvent.getX());
                        if (col != -1 && mConnectFourRoom.getPlayerTurn().equals(Player.currentPlayer.getPlayerId()))
                            drop(col);
                    }
                }
                return true;
            }
        });
        Button resetButton = (Button) findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });
        viewHolder = new ViewHolder();
        viewHolder.turnIndicatorImageView = (ImageView) findViewById(R.id.turn_indicator_image_view);
//        viewHolder.turnIndicatorImageView.setImageResource(resourceForTurn());
        viewHolder.winnerText = (TextView) findViewById(R.id.winner_text);
        viewHolder.winnerText.setVisibility(View.GONE);
    }

    private void updateRoomData() {
        final DocumentReference docRef = mFirestore.collection("rooms").document(Room.currentRoomId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                mConnectFourRoom = documentSnapshot.toObject(ConnectFourRoom.class);
                viewHolder.turnIndicatorImageView.setImageResource(resourceForTurn());
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
                mConnectFourRoom = snapshot.toObject(ConnectFourRoom.class);
                updateUI();
            } else {
                Log.d(TAG, "Current data: null");
            }
        }
    };

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


    private void updateUI() {
        if(mConnectFourRoom.getPlayerTurn().equals(Player.currentPlayer.getPlayerId())){
            dropOpponent(mConnectFourRoom.getLastUpdateCol());
        }
    }

    private void buildCells() {
        cells = new ImageView[NUM_ROWS][NUM_COLS];
        for (int r=0; r<NUM_ROWS; r++) {
            ViewGroup row = (ViewGroup) ((ViewGroup) boardView).getChildAt(r);
            row.setClipChildren(false);
            for (int c=0; c<NUM_COLS; c++) {
                ImageView imageView = (ImageView) row.getChildAt(c);
                imageView.setImageResource(android.R.color.transparent);
                cells[r][c] = imageView;
            }
        }
    }

    private void dropOpponent(int col){
        if (mConnectFourRoom.isHasWinner())
            return;
        int row = mConnectFourRoom.lastAvailableRow(col)+1;
        if (row < 0 || col < 0)
            return;
        mConnectFourRoom.toggleTurn();
        final ImageView cell = cells[row][col];
        float move = -(cell.getHeight() * row + cell.getHeight() + 15);
        cell.setY(move);
        cell.setImageResource(resourceForTurn());
        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, Math.abs(move));
        anim.setDuration(500);
        anim.setFillAfter(true);
        cell.startAnimation(anim);
        if (mConnectFourRoom.checkForWin(col, row)) {
            win();
            mConnectFourRoom.setHasWinner(true);
        }
        mConnectFourRoom.toggleTurn();
    }

    private void drop(int col) {
        if (mConnectFourRoom.isHasWinner())
            return;
        int row = mConnectFourRoom.lastAvailableRow(col);
        if (row == -1)
            return;
        final ImageView cell = cells[row][col];
        float move = -(cell.getHeight() * row + cell.getHeight() + 15);
        cell.setY(move);
        cell.setImageResource(resourceForTurn());
        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, Math.abs(move));
        anim.setDuration(500);
        anim.setFillAfter(true);
        cell.startAnimation(anim);
        mConnectFourRoom.occupyCell(col, row);
        if (mConnectFourRoom.checkForWin(col, row)) {
            win();
        }
        changeTurn();
        mConnectFourRoom.nextTurn();
        mConnectFourRoom.setLastUpdateCol(col);
        updateRoomDb();

    }

    private void win() {
        int color = mConnectFourRoom.turn == 1 ? getResources().getColor(R.color.primary_player) : getResources().getColor(R.color.secondary_player);
        viewHolder.winnerText.setTextColor(color);
        viewHolder.winnerText.setVisibility(View.VISIBLE);
    }

    private void changeTurn() {
        viewHolder.turnIndicatorImageView.setImageResource(resourceForTurn());
        mConnectFourRoom.toggleTurn();
    }

    private int colAtX(float x) {
        float colWidth = cells[0][0].getWidth();
        int col = (int) x / (int) colWidth;
        if (col < 0 || col > 6)
            return -1;
        return col;
    }

    private int resourceForTurn() {
        switch (mConnectFourRoom.turn) {
            case 1:
                return R.drawable.red;
            case 2:
                return R.drawable.yellow;
        }
        return R.drawable.red;
    }

    private void reset() {
        mConnectFourRoom.reset();
        viewHolder.winnerText.setVisibility(View.GONE);
        viewHolder.turnIndicatorImageView.setImageResource(resourceForTurn());
        for (int r=0; r<NUM_ROWS; r++) {
            for (int c=0; c<NUM_COLS; c++) {
                cells[r][c].setImageResource(android.R.color.transparent);
            }
        }
    }

    private void updateRoomDb() {
        mFirestore.collection("rooms").document(Room.currentRoomId)
                .set(mConnectFourRoom, SetOptions.merge());
    }

    public void registerListener(){
        mFirestore.collection("rooms").document(Room.currentRoomId);
        final DocumentReference docRef = mFirestore.collection("rooms").document(Room.currentRoomId);
        registration = docRef.addSnapshotListener(roomListener);
    }

    public void unregisterListener(){
        registration.remove();           //<-- This is the key
    }

}
