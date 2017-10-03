package com.example.shukri.fixed;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by shukri on 22-Mar-17.
 */

public class Profile_Fragment extends android.support.v4.app.Fragment {
    private Firebase myFirebaseRef;
    private FirebaseAuth mAuth;
    private TextView name;
    private TextView email;
    private Button changeButton;
    private Button revertButton;
    private ImageButton logoutb,updateb;
    // To hold Facebook profile picture
    private CircleImageView profilePicture;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private StorageReference mStorageRef;
    String profilepic;
    @Nullable
    Firebase mRef=new Firebase("https://fixed-7dac1.firebaseio.com/users/");

    @Override
    public void onStart() {
        super.onStart();
        Log.d("onStartP","true");
        myFirebaseRef = new Firebase("https://fixed-7dac1.firebaseio.com/users");

        if(mAuth.getCurrentUser()!=null) {
            myFirebaseRef.child(mAuth.getCurrentUser().getUid()).child("status").setValue("Online");
        }
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("onStopM","true");
        if(mAuth.getCurrentUser()!=null) {
            myFirebaseRef.child(mAuth.getCurrentUser().getUid()).child("status").setValue("Offline");
        }
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    String photoUrl;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.profile_fragment,container,false);
        myFirebaseRef = new Firebase("https://fixed-7dac1.firebaseio.com/users/");
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                final FirebaseUser mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                    name = (TextView) getView().findViewById(R.id.text_view_name);
                    email = (TextView)getView().findViewById(R.id.text_view_email);

                    profilePicture = (CircleImageView)getView().findViewById(R.id.profile_picture);
                    logoutb = (ImageButton)getView().findViewById(R.id.logoutbutton);
                    updateb = (ImageButton)getView().findViewById(R.id.updatebutton);



                    updateb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LinearLayout update = (LinearLayout)getView().findViewById(R.id.updateinfo);
                            update.setVisibility(View.VISIBLE);
                        }
                    });
                    logoutb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d("dlg", "lg clicked");

                            AlertDialog.Builder builder1=new AlertDialog.Builder(getActivity());
                            builder1 .setTitle("Çkyqja");
                            builder1.setMessage("A deshironi te çkyqeni");
                            builder1  .setPositiveButton("Çkyqu", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue
                                    FirebaseAuth.getInstance().signOut();
                                    myFirebaseRef.child(mUser.getUid()).child("status").setValue("Offline");

                                    LoginManager.getInstance().logOut();

                                    Intent i  = new Intent(getActivity(),LoginActivity.class);
                                    startActivity(i);




                                }
                            });
                            builder1    .setNegativeButton("Anulo", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            });
                            AlertDialog alertDialog = builder1.create();
                            alertDialog.show();

                        }
                    });
                    final Profile profile = Profile.getCurrentProfile();
                    if(profile!=null)// User is signed in with Facebook
                    {

                        //Get the uid for the currently logged in User from intent data passed to this activity
                        String uid = firebaseAuth.getCurrentUser().getUid();
                        //Get the imageUrl  for the currently logged in User from intent data passed to this activity
//                        String imageUrl = pref.getString("profile_picture", "");

                        //Get the uid for the currently logged in User from intent data passed to this activity
                        String fbid = Profile.getCurrentProfile().getId();
                        //Get the imageUrl  for the currently logged in User from intent data passed to this activity
                        String imageUrl = "https://graph.facebook.com/"+fbid+"/picture?type=large&width=720&height=720";
                        Log.d("imageUrl",imageUrl);


                        Log.d("fb",Profile.getCurrentProfile().getId());

                        myFirebaseRef.child(uid).child("photo").addValueEventListener(new ValueEventListener() {
                            //onDataChange is called every time the name of the User changes in your Firebase Database
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //Inside onDataChange we can get the data as an Object from the dataSnapshot
                                //getValue returns an Object. We can specify the type by passing the type expected as a parameter
                                photoUrl = dataSnapshot.getValue(String.class);
                                Log.d("photo",""+photoUrl);
                                Picasso.with(getActivity()).load(photoUrl).into(profilePicture);
                                Log.d("imt",""+photoUrl);
                            }
                            //onCancelled is called in case of any error
                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                Toast.makeText(getActivity(), "" + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                        mRef.child(uid).child("photo").setValue(imageUrl);

                        name.setText(firebaseAuth.getCurrentUser().getDisplayName().toString());
                        email.setText(firebaseAuth.getCurrentUser().getEmail().toString());



                        myFirebaseRef.child(uid).child("email").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String data = dataSnapshot.getValue(String.class);
                                email.setText(data);

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }

                        });
                    }
                    else {
                        // User is signed in
                         getProfilePicture();
                        name.setText(mAuth.getCurrentUser().getDisplayName());
                        profilePicture.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent g = new Intent(Intent.ACTION_PICK);
                                g.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                g.setType("image/*");
                                startActivityForResult(g,2);
                            }
                        });
                        SharedPreferences uname = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor editor = uname.edit();
                        editor.putString("user_id",mUser.getUid());
                        editor.apply();
                        Log.d("ffoto",mStorageRef.toString());
                        Log.d("displayname",mUser.getDisplayName());
                        final StorageReference pathRef = mStorageRef.child("photo");

                        Log.d("pathref",pathRef.child(firebaseAuth.getCurrentUser().getUid()).toString());


                    }

                    Log.d("TAG", "onAuthStateChanged:signed_in:" + mUser.getUid());
                } else {// User is signed out
                   getActivity().finish();
                    mAuth.signOut();
                    LoginManager.getInstance().logOut();

                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }

            }
        };

        return v;


    }
    public void getProfilePicture(){

        mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild("photo")) {
                    mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("photo").setValue("https://firebasestorage.googleapis.com/v0/b/fixed-7dac1.appspot.com/o/photo%2Fdefault.jpg?alt=media&token=c87e962f-a07b-440d-8c58-17ff58b8ac59");

//                    setDefaultProfilePicture();

                }
                else {
                    Picasso.with(getActivity()).load(dataSnapshot.child("photo").getValue().toString()).resize(150,150).into(profilePicture);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

//    public void setDefaultProfilePicture(){
//        profilePicture.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
//
//
//    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2 && resultCode==RESULT_OK ){
            Log.d("requestCode","true");
            final Uri uri = data.getData();
            final ProgressDialog p ;
            p = new ProgressDialog(getActivity());
            p.setIndeterminate(true);
            p.setCancelable(false);
            p.setMessage("Uploading Profile Picture");
            p.show();

                final Intent a = new Intent();
                a.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                final StorageReference filepath = mStorageRef.child("photo").child(mAuth.getCurrentUser().getUid());
                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   p.setIndeterminate(false);

                        p.dismiss();
                    Toast.makeText(getActivity(),"Done",Toast.LENGTH_SHORT).show();
                        profilepic = filepath.getDownloadUrl().toString();
                        Log.d("profilepicss", profilepic);



                    }
                });

            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    mRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("photo").setValue(uri.toString());

                    Log.d("inserted",uri.toString());
                    Picasso.with(getActivity()).load(uri).fit().into(profilePicture);
                }
            });

        }
    }


}
