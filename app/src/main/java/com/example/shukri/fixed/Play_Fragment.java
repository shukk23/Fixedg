package com.example.shukri.fixed;

import android.animation.LayoutTransition;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by shukri on 22-Mar-17.
 */

public class Play_Fragment extends android.support.v4.app.Fragment {
    public FirebaseAuth mAuth;
    public Firebase myFirebaseRef;
    public FirebaseAuth.AuthStateListener mAuthListener;
    public TextView msg;
    public LinearLayout user_info;
    public ImageButton logout;
    public Button quickgame, versus,fournumbers,testb;
    public StorageReference mStorageRef;
    FirebaseDatabase mDatabase;
    public String senderName= null;
    public String senderId= null;
    public String senderPhoto= null;
    public String RoomId= null;
    String photourl = null;
    ListView listv;
    CustomListAdapter customListAdapter;
    ArrayList<Players> arrayList = new ArrayList<Players>();
    public ProgressDialog mProgressDialog;
    String progressMessage;
    String ReceiverId;

    public boolean isRequestConfirmation() {
        return requestConfirmation;
    }

    public void setRequestConfirmation(boolean requestConfirmation) {
        this.requestConfirmation = requestConfirmation;
    }

    boolean requestConfirmation;

    public void setRoomId(String roomId) {
        RoomId = roomId;
    }

    public String getRoomId() {
        return RoomId;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderPhoto(String senderPhoto) {
        this.senderPhoto = senderPhoto;
    }

    public String getSenderPhoto() {
        return senderPhoto;
    }

    public void setReceiverId(String receiverId) {
        ReceiverId = receiverId;
    }

    public String getReceiverId() {
        return ReceiverId;
    }

    public String getProgressMessage() {
        return progressMessage;
    }

    public void setProgressMessage(String progressMessage) {
        this.progressMessage = progressMessage;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderId() {
        return senderId;
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d("onStartP","true");

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

//    @Override
//    public void onPause() {
//        super.onPause();
//        Log.d("onPauseP","true");
//        if(mAuth.getCurrentUser()!=null) {
//            myFirebaseRef.child(mAuth.getCurrentUser().getUid()).child("status").setValue("Offline");
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.play_fragment,container,false);
        mStorageRef = FirebaseStorage.getInstance().getReference();
testb = (Button)v.findViewById(R.id.button15);
        quickgame = (Button)v.findViewById(R.id.quickgame);
        quickgame.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i = new Intent (getActivity(),Quickgame.class);
        startActivity(i);
    }
});

        versus = (Button)v.findViewById(R.id.versus);

        testb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (getActivity(),OnlinePlayers.class);
                startActivity(i);
            }
        });
        versus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent (getActivity(),Test.class);
//                startActivity(i);

                showDialog();
            }
        });

        fournumbers = (Button)v.findViewById(R.id.fournumbers);
        fournumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (getActivity(),fournumbers.class);
                startActivity(i);
            }
        });
        myFirebaseRef = new Firebase("https://fixed-7dac1.firebaseio.com/users");
        user_info = (LinearLayout)v.findViewById(R.id.user_info);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                    user_info.setVisibility(View.VISIBLE);
                    myFirebaseRef.child(mUser.getUid()).child("status").setValue("Online");


                    logout = (ImageButton)v.findViewById(R.id.logout);

                    msg=(TextView)v.findViewById(R.id.user_name);
                    msg.setText(firebaseAuth.getCurrentUser().getDisplayName());
                    setSenderName(msg.getText().toString());
                    final ImageView profilePicture = (ImageView)v.findViewById(R.id.profile_pic);
                    showRequests();


                    Profile profile = Profile.getCurrentProfile();
                    if(profile!=null) {
                        String fbid = Profile.getCurrentProfile().getId();
                        //Get the imageUrl  for the currently logged in User from intent data passed to this activity
                        String photoUrl = "https://graph.facebook.com/"+fbid+"/picture?type=large&width=720&height=720";

                        setPhotourl(photoUrl);
                        myFirebaseRef.child(mAuth.getCurrentUser().getUid()).child("photo").setValue(photoUrl);

                        Picasso.with(getActivity()).load(photoUrl).fit().into(profilePicture);

                    }
                    else {
                        StorageReference pathRef = mStorageRef.child("photo");

                        Log.d("pathref",pathRef.child(firebaseAuth.getCurrentUser().getUid()).toString());


                        pathRef.child(mAuth.getCurrentUser().getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                            @Override
                            public void onSuccess(Uri uri) {


                                setPhotourl(uri.toString());
//                                setSenderPhoto(uri.toString());

                                Picasso.with(getActivity()).load(uri).fit().into(profilePicture);
                            }
                        });

                    }

                        logout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.d("dlg", "lg clicked");

                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                                builder1.setTitle("Çkyqja");
                                builder1.setMessage("A jeni i sigurt per çkyqje?");
                                builder1.setPositiveButton("Çkyqu", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue
                                        FirebaseAuth.getInstance().signOut();
                                        myFirebaseRef.child(mUser.getUid()).child("status").setValue("Offline");
                                        LoginManager.getInstance().logOut();

                                        user_info.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
                                        user_info.setVisibility(View.INVISIBLE);
                                    }
                                });
                                builder1.setNegativeButton("Anulo", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                });
                                AlertDialog alertDialog = builder1.create();
                                alertDialog.show();

                            }
                        });

                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + mUser.getUid());
                } if(mUser==null) {

                    versus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Toast.makeText(getActivity(),"You must log in to play online",Toast.LENGTH_SHORT).show();

                        }
                    });
                    // User is signed out
//                    user_info.setVisibility(View.INVISIBLE);
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }

            }
        };

        return v;

    }


    public void showDialog(){
        final Dialog dialog = new Dialog(getActivity(),R.style.Dialog);
        dialog.setContentView(R.layout.dialoglayout);
        dialog.setTitle("Tipi i lojes");
        Button dialogClose = (Button)dialog.findViewById(R.id.dialogClose);
        final Button dialogOk = (Button)dialog.findViewById(R.id.dialogOk);

        final RadioGroup rg = (RadioGroup)dialog.findViewById(R.id.radioGroup);
        int selectedId = rg.getCheckedRadioButtonId();

        int a = 0;
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes final int checkedId) {


            }
        });

        dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               final RadioButton radioButton = (RadioButton)dialog.findViewById(rg.getCheckedRadioButtonId());
               if (radioButton.getText().toString().contains("3")){
                    dialog.dismiss();


                   showOnlinePlayersDialog();
                   Toast.makeText(getActivity(),"Loja u krijua",Toast.LENGTH_SHORT).show();

               }
                if (radioButton.getText().toString().contains("4")){
                    dialog.dismiss();
                    Intent i = new Intent(getActivity(),fournumbers.class);
                    startActivity(i);

                }



            }
        });

        dialog.show();

    }

    public void showRequests(){
        Firebase requestsRef = new Firebase("https://fixed-7dac1.firebaseio.com/requests");

        requestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                    if (dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                        setSenderId(dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("From").getValue().toString());
                        requestDialog(dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("From").getValue().toString());
                    }
                    else {
                        return;
                    }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void createRoom() {
        mDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference dbRef = mDatabase.getReference("rooms/");


        dbRef.child(mAuth.getCurrentUser().getUid()+" vs "+getReceiverId()).child("Players").child(mAuth.getCurrentUser().getUid()).child("name").setValue(mAuth.getCurrentUser().getDisplayName());
        dbRef.child(mAuth.getCurrentUser().getUid()+" vs "+getReceiverId()).child("Players").child(mAuth.getCurrentUser().getUid()).child("photo").setValue(getPhotourl());


    }

    public void requestDialog(String senderId){

        Firebase usersRef = new Firebase("https://fixed-7dac1.firebaseio.com/users");

        Log.d("senderId",senderId);
        if(getActivity()!=null) {
            final Dialog dialog = new Dialog(getActivity(), R.style.Dialog);
            dialog.setContentView(R.layout.request_dialog);


            dialog.setTitle("Ftese per loje");
            usersRef.child(senderId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("lastname") == true) {

                        senderName = dataSnapshot.child("name").getValue().toString() + " " + dataSnapshot.child("lastname").getValue().toString();

                        Log.d("sendeName", "" + senderName);
                    }
                    if (dataSnapshot.hasChild("lastname") == false) {
                        senderName = dataSnapshot.child("name").getValue().toString();


                    }
                    TextView dialogText = (TextView) dialog.findViewById(R.id.dialogText);

                    dialogText.setText("Ftese per loje nga : " + senderName);

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }

            });
            Log.d("senderName", "" + senderName);

            Button requestCancel = (Button) dialog.findViewById(R.id.requestCancel);
            final Button requestAccept = (Button) dialog.findViewById(R.id.requestAccept);
            final Intent i = new Intent(getActivity(), MultiThree.class);

            requestCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    deleteRequest();
                    Log.d("refuzuar","true");
                    hideProgresDialog();
                    setRequestConfirmation(false);


                }
            });
            requestAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptRequest();
                    dialog.dismiss();
//                    deleteRequest();
                    showProgressDialog();
                    i.putExtra("playerone", "" + getSenderId().toString());
                    setRequestConfirmation(true);
//                    startActivity(i);
////               createRoom();
////                    final int secs = 3;
////                    new CountDownTimer((secs + 1) * 1000, 1000) {
////
////                        @Override
////                        public void onTick(long millisUntilFinished) {
////
////
////                        }
////
////                        @Override
////                        public void onFinish() {
////
////
////
////                        }
////                    }.start();
//
//
////                i.putExtra("playertwo",""+mAuth.getCurrentUser().getUid().toString());

                    Log.d("playerone", "" + getSenderId().toString());
                    Log.d("playertwo", "" + mAuth.getCurrentUser().getUid().toString());

                }
            });
            final int secs = 5;
            new CountDownTimer((secs + 1) * 1000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {

                    dialog.setTitle("Ftesa refuzohet ne : " + (int) (millisUntilFinished * .001f));

                }

                @Override
                public void onFinish() {
                    i.putExtra("playerone", "" + getSenderId().toString());
                    hideProgresDialog();
                    dialog.dismiss();
                    if (isRequestConfirmation()) {
                        startActivity(i);
                    }
//                    deleteRequest();

                }
            }.start();

            dialog.show();

            Log.d("getsenderId", "" + getSenderId());

            Firebase dbref;
            dbref = new Firebase("https://fixed-7dac1.firebaseio.com/users");

            dbref.child(getSenderId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    setSenderPhoto(dataSnapshot.child("photo").getValue().toString());
//                Toast.makeText(getActivity(),""+dataSnapshot.child("photo").getValue().toString(),Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }

            });

        }
    }
    public void deleteRequest(){
        Firebase requestsRef = new Firebase("https://fixed-7dac1.firebaseio.com/");

        requestsRef.child("requests").child(mAuth.getCurrentUser().getUid()).removeValue();

    }

    public void acceptRequest(){
//        Toast.makeText(getActivity(),"getSenderPhoto : "+getSenderPhoto(),Toast.LENGTH_SHORT).show();

        Firebase requestsRef = new Firebase("https://fixed-7dac1.firebaseio.com/requests");
        mDatabase = FirebaseDatabase.getInstance();
         DatabaseReference dbRef = mDatabase.getReference("rooms");
        RandomNumbers randomNumbers = new RandomNumbers();

        //room setup names and photos
        dbRef.child(getSenderId()+" vs "+mAuth.getCurrentUser().getUid()).child("Players").child(mAuth.getCurrentUser().getUid()).child("name").setValue(mAuth.getCurrentUser().getDisplayName());
        dbRef.child(getSenderId()+" vs "+mAuth.getCurrentUser().getUid()).child("Players").child(mAuth.getCurrentUser().getUid()).child("photo").setValue(getPhotourl());
        dbRef.child(getSenderId()+" vs "+mAuth.getCurrentUser().getUid()).child("Players").child(getSenderId()).child("name").setValue(getSenderName());
        dbRef.child(getSenderId()+" vs "+mAuth.getCurrentUser().getUid()).child("Players").child(getSenderId()).child("photo").setValue(getSenderPhoto());


        // room setup tries
        dbRef.child(getSenderId()+" vs "+mAuth.getCurrentUser().getUid()).child("tries").child(getSenderId()).setValue(" ");
        dbRef.child(getSenderId()+" vs "+mAuth.getCurrentUser().getUid()).child("tries").child(mAuth.getCurrentUser().getUid()).setValue(" ");


        //first turn
        dbRef.child(getSenderId()+" vs "+mAuth.getCurrentUser().getUid()).child("turn").setValue(getSenderId());


        //room generate randoms
        randomNumbers.generateNumbers();
        dbRef.child(getSenderId()+" vs "+mAuth.getCurrentUser().getUid()).child("Randoms").child("rnd1").setValue(randomNumbers.getRndOne());
        dbRef.child(getSenderId()+" vs "+mAuth.getCurrentUser().getUid()).child("Randoms").child("rnd2").setValue(randomNumbers.getRndTwo());
        dbRef.child(getSenderId()+" vs "+mAuth.getCurrentUser().getUid()).child("Randoms").child("rnd3").setValue(randomNumbers.getRndThree());

        setRoomId(getSenderId()+" vs "+mAuth.getCurrentUser().getUid());

//        requestsRef.child(mAuth.getCurrentUser().getUid()).removeValue();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("pref", getSenderId().toString());
        editor.putString("roomID",""+getSenderId()+" vs "+mAuth.getCurrentUser().getUid().toString());
        editor.putString("accepted","1");
        editor.apply();



    }

    public void showOnlinePlayersDialog(){


        final Dialog dialog = new Dialog(getActivity(),R.style.Dialog);
        dialog.setContentView(R.layout.online_players_list);
        final SwipeRefreshLayout swipeContainer;

        dialog.setTitle("Lojtaret Online");
        swipeContainer = (SwipeRefreshLayout) dialog.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getUsers();
                swipeContainer.setRefreshing(false);

            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



        dialog.setTitle("Dergo Ftese");
        listv = (ListView) dialog.findViewById(R.id.listv);
        arrayList = new ArrayList<>();
        customListAdapter = new CustomListAdapter(getActivity(), R.layout.listview_item, arrayList);

        Button closeDialog = (Button)dialog.findViewById(R.id.closeDialog);
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        getUsers();
        dialog.show();


    }
    public void getUsers(){


        customListAdapter.clear();
        arrayList.clear();
        Firebase dbRef;

        dbRef = new Firebase("https://fixed-7dac1.firebaseio.com/users/");

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String photoUrl = "";
                int length = (int) dataSnapshot.getChildrenCount();
                int i = 0;
                int j = 0;
                final ArrayList<String> idString = new ArrayList<String>(length);

                if (dataSnapshot.getChildrenCount()<2){
                    Toast.makeText(getActivity(), "Nuk ka lojtar online", Toast.LENGTH_SHORT).show();

                }
                for (DataSnapshot ds : dataSnapshot.getChildren()) {


                    if (!ds.getKey().equals(mAuth.getCurrentUser().getUid())) {
                        if (ds.child("status").getValue().equals("Online")) {
                            if (ds.hasChild("name")) {
                                String name = ds.child("name").getValue().toString();
                                if (ds.hasChild("lastname") == true) {

                                    String lastname = ds.child("lastname").getValue().toString();
                                    idString.add(i, ds.child("id").getValue().toString());
                                    i++;
                                    arrayList.add(new Players(ds.child("photo").getValue().toString(), name + " " + lastname));

                                }
                                if (ds.hasChild("lastname") == false) {
                                    idString.add(i, ds.child("id").getValue().toString());
                                    i++;
                                    arrayList.add(new Players(ds.child("photo").getValue().toString(), name));

                                }
                            }

                        }

                    }

                }
                listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

//                        Toast.makeText(getApplication(), "id :" + idString.get(position), Toast.LENGTH_SHORT).show();
                        sendRequestTo(idString.get(position));
                        setReceiverId(idString.get(position));

                        showOnlinePlayersDialog();
                        showProgressDialog();

                        final int secs = 5;
                        new CountDownTimer((secs+1)*1000,1000){

                            @Override
                            public void onTick(long millisUntilFinished) {


//                                Toast.makeText(getActivity(), "request sent to  :" + idString.get(position), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFinish() {
                                isAccepted();
                            }
                        }.start();

//                        Intent i = new Intent(getApplication(), usertest.class);
//                        i.putExtra("userid", idString.get(position));
//                        startActivity(i);
                    }
                });
//                           Picasso.with(getApplicationContext()).load(photoUrl).fit().into(img);
                customListAdapter.notifyDataSetChanged();
                listv.setTextFilterEnabled(true);
                listv.setAdapter(customListAdapter);
                listv.setTextFilterEnabled(true);

//                if (arrayList.size() < 1) {
//                    Toast.makeText(getActivity(), "Nuk ka lojtar online", Toast.LENGTH_SHORT).show();
//
//
//                }
//                            listv.setAdapter(picarrayAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity(),R.style.Dialog);

            mProgressDialog.setMessage("Ju lutem prisni");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }
   public void hideProgresDialog(){
       if (mProgressDialog != null && mProgressDialog.isShowing()) {
           mProgressDialog.dismiss();
       }
    }
    public void isAccepted(){

        final boolean isAccepted;
        Firebase roomsRef,requestRef,dbref;
        dbref = new Firebase("https://fixed-7dac1.firebaseio.com/");
        requestRef = new Firebase("https://fixed-7dac1.firebaseio.com/requests");

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.child("requests").hasChild(mAuth.getCurrentUser().getUid())==false) {
                    if (dataSnapshot.child("rooms").child(mAuth.getCurrentUser().getUid()+" vs "+getReceiverId()).child("Players").getChildrenCount() == 2) {
                        hideProgresDialog();

                        setRoomId(mAuth.getCurrentUser().getUid()+" vs "+getReceiverId());
                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("roomID",""+mAuth.getCurrentUser().getUid().toString()+" vs "+getReceiverId());
                        editor.apply();

                            Toast.makeText(getActivity(), "Pranuar", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getActivity(), MultiThree.class);

                        //                        Toast.makeText(getActivity(), "Room id : "+getRoomId(), Toast.LENGTH_SHORT).show();
                        startActivity(i);
                        getActivity().finish();
                    }
                    else {
                        hideProgresDialog();
                        Toast.makeText(getActivity(), "Refuzuar", Toast.LENGTH_SHORT).show();
                        }


                    }
//                }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }


    public void sendRequestTo(String id){
        Firebase requestsRef = new Firebase("https://fixed-7dac1.firebaseio.com/requests");

        requestsRef.child(id).child("From").setValue(mAuth.getCurrentUser().getUid());


    }
}
