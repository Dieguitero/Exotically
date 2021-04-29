package com.example.exotically;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/*import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;*/

import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileSetupActivity extends AppCompatActivity {

    private EditText mOwnerName, mPetName, mBio;
    private Switch mMating, mSocializing;
    private RadioGroup mRadioGender;
    private RadioButton mSelectedGender;
    private Button mSave, mGetLocation;
    private ImageView mOwnerImage, mPetImage;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private Spinner mSpecies;
    private boolean settingOwnerImage, settingPetImage;
    private Bitmap bitmapOwner, bitmapPet;
    private Uri uriOwner, uriPet;

    //private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        //mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference();

        mOwnerName      = (EditText) findViewById(R.id.name);
        mPetName        = (EditText) findViewById(R.id.petName);
        mBio            = (EditText) findViewById(R.id.bio);

        mMating         = (Switch) findViewById(R.id.mating);
        mSocializing    = (Switch) findViewById(R.id.socializing);

        mRadioGender    = (RadioGroup) findViewById(R.id.radioGender);

        mSave           = (Button) findViewById(R.id.save);
        mGetLocation    = (Button) findViewById(R.id.getLocationButton);

        mOwnerImage     = (ImageView) findViewById(R.id.imageProfile);
        mPetImage       = (ImageView) findViewById(R.id.imagePet);

        /*mGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            location.getLatitude();
                            location.getLongitude();
                        }
                    }
                });
            }
        });*/

        mOwnerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                settingOwnerImage = true;
                getImageCameraOrGallery(22);
            }
        });

        mPetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                settingPetImage = true;
                getImageCameraOrGallery(23);
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                mSave.setEnabled(false);

                final String ownerName = mOwnerName.getText().toString();
                final String petName = mPetName.getText().toString();
                final String bio = mBio.getText().toString();
                boolean mating = mMating.isChecked();
                boolean socializing = mSocializing.isChecked();
                final String species = "Iguana";

                int selectedRadio = mRadioGender.getCheckedRadioButtonId();
                mSelectedGender = (RadioButton)findViewById(selectedRadio);
                boolean gender = false;
                if(mSelectedGender.getText() == "Female"){ gender = true; }



                //UPDATE USER INFO
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("cardData");

                Map userInfo = new HashMap<>();
                userInfo.put("name", ownerName);
                userInfo.put("petName", petName);
                userInfo.put("profileImageUrl", "default");
                userInfo.put("petImageUrl", "default");
                userInfo.put("bio", bio);

                userInfo.put("mating", mating);
                userInfo.put("socializing", socializing);
                userInfo.put("gender", gender);
                userInfo.put("species", species);

                currentUserDb.updateChildren(userInfo);

                SharedPreferences userPref = getSharedPreferences("UserProfile", MODE_PRIVATE);
                SharedPreferences.Editor prefEditor = userPref.edit();
                prefEditor.putString("name", ownerName);
                prefEditor.putString("petName", petName);
                prefEditor.putString("profileImageUrl", "default");
                prefEditor.putString("petImageUrl", "default");
                prefEditor.putString("bio", bio);

                prefEditor.putBoolean("mating", mating);
                prefEditor.putBoolean("socializing", socializing);
                prefEditor.putBoolean("gender", gender);
                prefEditor.putString("species", species);
                prefEditor.commit();

                if(uriOwner != null){
                    StorageReference oFilepath = mStorage.child(userId).child(uriOwner.getLastPathSegment());

                    oFilepath.putFile(uriOwner).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           /* Uri downloadUrl =taskSnapshot.getDownloadUrl();

                            DatabaseReference newPost = mDatabase.push(); */
                        }
                    });
                }
                if(uriPet != null){

                }

                Intent intent = new Intent(ProfileSetupActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private void getImageCameraOrGallery(int requestCode){
        FragmentManager fm = getSupportFragmentManager();
        ImageSelection chooseSource = new ImageSelection(requestCode);
        chooseSource.show(fm, "fragment_alert");

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(ProfileSetupActivity.this, String.valueOf(requestCode), Toast.LENGTH_LONG).show();
        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if (settingOwnerImage) {
                mOwnerImage.setImageBitmap(imageBitmap);
                bitmapOwner = imageBitmap;
                uriOwner = data.getData();
            }
            else if(settingPetImage){
                mPetImage.setImageBitmap(imageBitmap);
                bitmapPet = imageBitmap;
                uriPet = data.getData();
            }
            settingOwnerImage = false;
            settingPetImage = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSave.setEnabled(true);
    }
}