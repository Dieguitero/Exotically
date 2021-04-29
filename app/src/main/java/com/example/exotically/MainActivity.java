package com.example.exotically;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Test";
    private cards cards_data[];
    private arrayAdapter arrayAdapter;
    private int i;

    private FirebaseAuth mAuth;
    private String userId;
    private cards userCard;

    private String otherStatus;
    private boolean socializing, mating, gender;

    private DatabaseReference usersDb;

    ListView listView;
    List<cards> rowItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        rowItems = new ArrayList<cards>();
        SharedPreferences userPref = getSharedPreferences("UserProfile", MODE_PRIVATE);
        socializing = userPref.getBoolean("socializing", false);
        mating = userPref.getBoolean("mating", false);
        gender = userPref.getBoolean("gender", false);

        arrayAdapter = new arrayAdapter(this, R.layout.item,  rowItems);

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String otherId = obj.getUserId();

                usersDb.child(otherId).child("Links").child(userId).setValue("No");
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String otherId = obj.getUserId();

                usersDb.child(otherId).child("Links").child(userId).setValue("Yes");
                usersDb.child(userId).child("Links").child(otherId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            otherStatus = String.valueOf(task.getResult().getValue());
                        }
                    }
                });
                if(otherStatus == "Yes"){
                    Toast.makeText(MainActivity.this, "MATCH", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                getPotentialMatch();
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                //CLICK
            }
        });

    }

  /*  private void getUserCard(String uId) {
        usersDb.child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userCard = dataSnapshot.child("cardData").getValue(cards.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    } */
/*
    private void isConnectionMatch(cards userCard) {
        DatabaseReference currentUserConnectionsDb = usersDb.child("Users").child(currentId).child("Yes").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Toast.makeText(MainActivity.this, "new Connection", Toast.LENGTH_LONG).show();
                    usersDb.child(otherUserType).child(snapshot.getKey()).child("connections").child("matches").child(currentId).setValue(true);  //userType should be oppositeUserSex(other Type)
                    usersDb.child(userType).child(currentId).child("connections").child("matches").child(snapshot.getKey()).setValue(true);  //userType should be oppositeUserSex(other Type)


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }   */

    public void getPotentialMatch(){
        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if(snapshot.exists()){
                    cards item = snapshot.child("cardData").getValue(cards.class);
                    item.setUserId(snapshot.getKey());

                    if(snapshot.getKey() != userId
                            && !snapshot.child("Links").hasChild(userId)
                            && ((item.getSocializing() && socializing) || ((item.getMating() && mating) && (item.getGender() != gender) ))) {
                        rowItems.add(item);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void logoutUser(View view) {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, LandingActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    public void goToSettings(View view) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
        return;
    }
}