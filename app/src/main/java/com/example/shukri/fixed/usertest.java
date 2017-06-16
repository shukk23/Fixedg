package com.example.shukri.fixed;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class usertest extends AppCompatActivity {
    TextView emri, status;
    private FirebaseAuth mAuth;
    private TextView name;
    private Firebase dbRef;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String photoUrl = null;
    String user_id = null;
    private CircleImageView profilePicture;
    private StorageReference mStorageRef;
    Button gamerequest;

    Firebase myFirebaseRef=new Firebase("https://fixed-7dac1.firebaseio.com/users/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_usertest);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser mUser = mAuth.getCurrentUser();

                if (mUser != null) {
                    dbRef = new Firebase("https://fixed-7dac1.firebaseio.com/users/");

                    user_id = getIntent().getStringExtra("userid");

                    Bundle extras = getIntent().getExtras();
                    emri = (TextView) findViewById(R.id.text_view_name);
                    profilePicture = (CircleImageView) findViewById(R.id.profile_picture);
                    status = (TextView) findViewById(R.id.statustv);

                    gamerequest= (Button)findViewById(R.id.gamerequest);

                    gamerequest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(),mUser.getUid().toString()+"sents request to : "+user_id,Toast.LENGTH_LONG).show();
                        }
                    });

                    dbRef.child(user_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            String name_db = dataSnapshot.child("name").getValue().toString();
                            emri.setText(name_db);

                            if (dataSnapshot.child("status").exists()) {
                                status.setText(dataSnapshot.child("status").getValue().toString());
                                if (status.getText().equals("Online")) {
                                    status.setTextColor(Color.GREEN);
                                    gamerequest.setVisibility(View.VISIBLE);
//                                    gamerequest.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//
//                                            dataSnapshot.child("invite").child(mAuth.getCurrentUser().getUid());
//                                        }
//                                    });
                                }
                                else {
                                    status.setTextColor(Color.WHITE);
                                    gamerequest.setVisibility(View.INVISIBLE);
                                }

                            } else {
                                status.setText("null");
                            }
                            if (dataSnapshot.hasChild("photo")) {
                                photoUrl = dataSnapshot.child("photo").getValue().toString();
                                if (photoUrl != null) {

                                    Picasso.with(usertest.this).load(photoUrl).into(profilePicture);

                                }
                            }

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });


                    Log.d("url", photoUrl + "--" + user_id);
                    if (photoUrl == null) {
                        StorageReference pathRef = mStorageRef.child("photo");

                        Log.d("pathref", pathRef.child(user_id).toString());


                        pathRef.child(user_id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
//                    Log.d("purl",photoUrl);
//                    Log.d("puid",user_id.toString());
                                Picasso.with(getApplication()).load(uri).fit().centerCrop().into(profilePicture);
                            }
                        });


                    }

                    Log.d("currentuser",mUser.getUid().toString());

                }
            }
        };




    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        if(mAuth.getCurrentUser()!=null) {
            myFirebaseRef.child(mAuth.getCurrentUser().getUid()).child("status").setValue("Online");
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuth.getCurrentUser()!=null) {
            myFirebaseRef.child(mAuth.getCurrentUser().getUid()).child("status").setValue("Offline");
        }
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
