package com.example.exotically;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class cards {
    private String userId;
    private String name, petName, bio, species;
    private String profileImageUrl, petImageUrl;
    private boolean mating, socializing, gender;
    private int distance;

    public cards() {};

    public cards(String userId, String name, String petName, String bio, boolean gender, boolean mating, boolean socializing, String species){
        this.userId = userId;
        this.name = name;
        this.petName = petName;
        this.bio = bio;

        this.gender = gender;
        this.mating = mating;
        this.socializing = socializing;
        this.species = species;
    }

    //U I D
    public String getUserId(){
        return userId;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }

    //N A M E
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    //P R O F I L E   P I C
    public String getProfileImageUrl(){
        return profileImageUrl;
    }
    public void setProfileImageUrl(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }

    //B I O
    public void setBio(String bio){
        this.bio = bio;
    }
    public String getBio(){
        return bio;
    }

    //P E T   N A M E
    public void setPetName(String petName){
        this.petName = petName;
    }
    public String getPetName(){
        return petName;
    }

    //P E T   P I C
    public String getPetImageUrl(){
        return petImageUrl;
    }
    public void setPetImageUrl(String petImageUrl){
        this.petImageUrl = petImageUrl;
    }

    //S P E C I E S
    public String getSpecies(){
        return species;
    }
    public void setSpecies(String species){
        this.species = species;
    }

    //M A T I N G
    public boolean getMating(){
        return mating;
    }
    public void setMating(boolean mating){
        this.mating = mating;
    }

    //S O C I A L I Z I N G
    public boolean getSocializing(){
        return socializing;
    }
    public void setSocializing(boolean socializing){
        this.socializing = socializing;
    }

    //G E N D E R
    public boolean getGender(){
        return gender;
    }
    public void setGender(boolean gender){
        this.gender = gender;
    }

    //D I S T A N C E
    public String getDistance(){
        return String.valueOf(distance);
    }
    public void setDistance(int distance){
        this.distance = distance;
    }
}
