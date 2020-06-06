package com.pakodigames.pakodi.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pakodigames.pakodi.R;
import com.pakodigames.pakodi.model.Player;
import com.pakodigames.pakodi.model.Room;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity implements EventListener<QuerySnapshot> {

    private static final String TAG = MainActivity.class.getName();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference roomsRef = db.collection("rooms");
    private Button createBtn;
    private Button joinBtn;
    private String roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createBtn = findViewById(R.id.create);
        joinBtn = findViewById(R.id.join);


        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addData();
                roomId = getRandomId();
                createNewRoom(roomId);
            }
        });

        joinBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });

    }

    private void createNewRoom(String roomId) {
        Room room = new Room(roomId, null,0);
        roomsRef.add(room)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(MainActivity.this,"Failed",Toast.LENGTH_LONG).show();
                    }
                });
    }

    private String getRandomId() {
        SecureRandom rand = new SecureRandom();
        int random = rand.nextInt(100000);
        return String.format("5%s",random);
    }

    @Override
    protected void onStart() {
        super.onStart();
        roomsRef.addSnapshotListener(this,this);
    }

    public void addData(){
        /*Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);*/
        Room room = new Room("1234", null,0);
        roomsRef.add(room)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(MainActivity.this,"Failed",Toast.LENGTH_LONG).show();
                    }
                });

        Task<QuerySnapshot> sp = roomsRef.whereEqualTo("roomId", "1234").get();
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
          List<DocumentChange> changes =  queryDocumentSnapshots.getDocumentChanges();
          for( DocumentChange change : changes) {
              QueryDocumentSnapshot snapshot = change.getDocument();
              Map<String,Object> data = snapshot.getData();


          }
    }
}
