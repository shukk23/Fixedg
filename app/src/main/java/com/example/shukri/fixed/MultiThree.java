package com.example.shukri.fixed;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.plattysoft.leonids.ParticleSystem;
import com.serhatsurguvec.continuablecirclecountdownview.ContinuableCircleCountDownView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MultiThree extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Firebase myFirebaseRef,roomsRef;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ImageView playerOneImg, playerTwoImg;
    TextView playerOneText,playerTwoText;
    String photourl;
    String pOneId,pTwoId;
    ListView listv;
    CustomListAdapter customListAdapter;
    public static ArrayList<Players> arrayList = new ArrayList<Players>();
    String secondPlayer;
    ListView mLv,lv;
    TextView t1,t2,t3;
    Button check,newGame,reset,button1,button2,button3,button4,button5,button6,button7,button8,button9,button0,buttonClear, buttonCheck,quitgame;
    TextView rndm,textView4;
    String  rnd1;
    String rnd2;
    final String[] numratVarg = new String[]{};
    final List<String> nr_list = new ArrayList<String>(Arrays.asList(numratVarg));
    String rnd3;
    int playerNumber ;
    String roomid;
    public ProgressDialog mProgressDialog;
    GridLayout gridLayout;
    ContinuableCircleCountDownView mCountDownView;

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    final ArrayList<String> opponentArrayList= new ArrayList<String>();
    public String getRnd1() {
        return rnd1;
    }

    public void setRnd1(String rnd1) {
        this.rnd1 = rnd1;
    }

    public String getRnd2() {
        return rnd2;
    }

    public void setRnd2(String rnd2) {
        this.rnd2 = rnd2;
    }

    public String getRnd3() {
        return rnd3;
    }

    public void setRnd3(String rnd3) {
        this.rnd3 = rnd3;
    }

    public void setpTwoId(String pTwoId) {
        this.pTwoId = pTwoId;
    }

    public String getpTwoId() {
        return pTwoId;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setSecondPlayer(String secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public String getSecondPlayer() {
        return secondPlayer;
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d("onStartP", "true");

        if (mAuth.getCurrentUser() != null) {
            myFirebaseRef.child(mAuth.getCurrentUser().getUid()).child("status").setValue("In Game");
        }
        mAuth.addAuthStateListener(mAuthListener);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_three);
        UiElements();

        mAuth = FirebaseAuth.getInstance();
        myFirebaseRef = new Firebase("https://fixed-7dac1.firebaseio.com/users");
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String sc = pref.getString("senderID", "nothing");
        final String roomid = pref.getString("roomID","nothing");
        setRoomid(roomid);
        getRandoms();
        gridButtons();
        insertInGame();

//        checkInGame();
        gridLayout.setVisibility(View.VISIBLE);


//        final Handler h = new Handler();
//        final int delay = 10000; //milliseconds
//
//        h.postDelayed(new Runnable(){
//            public void run(){
//                //do something
//                checkTurn();
//
//                h.postDelayed(this, delay);
//            }
//        }, delay);
        quitgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                quitDialog();



            }
        });


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        buttonCheck.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int secs = 5;
                        if(!check()) {
                            setTurn(getpTwoId());
                            reset();
//                            startCountDown();
////                            new CountDownTimer((secs + 1) * 1000, 1000) {
////
////                                @Override
////                                public void onTick(long millisUntilFinished) {
////
////
//////                                Toast.makeText(getActivity(), "request sent to  :" + idString.get(position), Toast.LENGTH_SHORT).show();
////                                }
////
////                                @Override
////                                public void onFinish() {
////
////                                }
////                            }.start();
                        }
                        else{
                            youWin();
                        }
                    }
                });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {


//                    Toast.makeText(getApplication(),""+getRoomid(),Toast.LENGTH_SHORT).show();
                    checkTurn();
                    checkPlayers();


//                    checkRoom();
//                    getOpponentsTries();



                } else {
                    Intent i = new Intent(MultiThree.this,MainActivity.class);
                    startActivity(i);
                }
            }
        };

        t1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                t1.setBackgroundResource(R.drawable.common_google_signin_btn_icon_dark_normal_background);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (t1.getText().toString().trim().length() != 0) {

                    t2.requestFocus();
                    t2.setFocusable(true);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        });
        t2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (t2.getText().toString().trim().length() != 0) {

                    t3.requestFocus();
                    t3.setFocusable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        t1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    t1.setBackgroundResource(R.drawable.common_google_signin_btn_icon_dark_normal_background);

                }
                else{
                    t1.setBackgroundResource(R.drawable.common_google_signin_btn_icon_light_normal_background);

                }
            }
        });
        t2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    t2.setBackgroundResource(R.drawable.common_google_signin_btn_icon_dark_normal_background);

                }
                else{
                    t2.setBackgroundResource(R.drawable.common_google_signin_btn_icon_light_normal_background);

                }
            }
        });
        t3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    t3.setBackgroundResource(R.drawable.common_google_signin_btn_icon_dark_normal_background);

                }
                else{
                    t3.setBackgroundResource(R.drawable.common_google_signin_btn_icon_light_normal_background);

                }
            }
        });


    }

    public void checkInGame() {
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("rooms");

        for (int i = 0;i<2;i++) {
            dbRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child("ingame").getChildrenCount() == 2) {
                        hideProgresDialog();
                        startCountDown();
                    } else {
                        Toast.makeText(getApplication(), "in game !2", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(MultiThree.this);

            mProgressDialog.setMessage("Waiting for Opponent");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgresDialog(){
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void checkPlayers(){
        roomsRef = new Firebase("https://fixed-7dac1.firebaseio.com/rooms");
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String opponent = pref.getString("senderID","");
        setpTwoId(opponent);

        roomsRef.child(getRoomid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Players").getChildrenCount()==2){
                    if (dataSnapshot.child("Players").child(getpTwoId()).getValue().equals("quit")) {
                        Toast.makeText(MultiThree.this, "Opponent quitted", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(MultiThree.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        deleteRoom();

//                    deleteRoom();

                    }

//                    else {
//                    Intent i = new Intent(MultiThree.this, MainActivity.class);
//
//                    startActivity(i);
//                    finish();
//                    }
                    Toast.makeText(getApplicationContext(),"2 Players",Toast.LENGTH_SHORT).show();
                    for (DataSnapshot ds : dataSnapshot.child("Players").getChildren()){
                        if (ds.getKey().equals(mAuth.getCurrentUser().getUid())){
                            playerOneText.setText(ds.child("name").getValue().toString());
                            Picasso.with(getApplication()).load(ds.child("photo").getValue().toString()).fit().into(playerOneImg);
                        }
                        if (ds.getKey().equals(getpTwoId())) {

                                playerTwoText.setText(ds.child("name").getValue().toString());
                                Picasso.with(getApplication()).load(ds.child("photo").getValue().toString()).fit().into(playerTwoImg);

                        }

                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"not 2 Players",Toast.LENGTH_SHORT).show();

                }

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        Log.d("getptwoid",""+getpTwoId());
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("rooms");

        dbRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(getRoomid())) {
                    if (dataSnapshot.child(getRoomid()).child("Players").child(getpTwoId()).getValue().equals("quit")) {
                        Toast.makeText(MultiThree.this, "Opponent quitted", Toast.LENGTH_SHORT).show();


                        Intent i = new Intent(MultiThree.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        deleteRoom();

//                    deleteRoom();

                    }
                }
                else {
                    Intent i = new Intent(MultiThree.this, MainActivity.class);

                    startActivity(i);
                    finish();
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public boolean check() {

        if (!mCountDownView.isStopped()){

            mCountDownView.cancel();
            mCountDownView.setVisibility(View.INVISIBLE);
            gridLayout.setVisibility(View.GONE);
            }
        enableButtons();
        t3.clearFocus();
        lv.setFocusable(true);

        int qelluar = 0;
        int fiksuar = 0;
        String msg = "";
        if (t1.getText().toString().equalsIgnoreCase("" + getRnd1())) {
            fiksuar++;

        }
        if (t2.getText().toString().equalsIgnoreCase("" + getRnd2())) {
            fiksuar++;

        }
        if (t3.getText().toString().equalsIgnoreCase("" + getRnd3())) {
            fiksuar++;

        }
        if (t1.getText().toString().equalsIgnoreCase("" + getRnd2())) {
            qelluar++;
            //msg+=fiksuar+" fiksuar," + qelluar + " qelluar";

        }
        if (t1.getText().toString().equalsIgnoreCase("" + getRnd3())) {
            qelluar++;
        }
        if (t2.getText().toString().equalsIgnoreCase("" + getRnd1())) {
            qelluar++;
            //msg+=fiksuar+" fiksuar," + qelluar + " qelluar";

        }
        if (t2.getText().toString().equalsIgnoreCase("" + getRnd3())) {
            qelluar++;

        }
        if (t3.getText().toString().equalsIgnoreCase("" + getRnd2())) {
            qelluar++;
            //msg+=fiksuar+" fiksuar," + qelluar + " qelluar";

        }
        if (t3.getText().toString().equalsIgnoreCase("" + getRnd1())) {
            qelluar++;
        }
        if (fiksuar == 3) {
            qelluar = 0;
            rndm.setVisibility(View.VISIBLE);
            rndm.setText("Sakte : " + rnd1 + rnd2 + rnd3);
//            showAnimation();
//            showDialog();
            final int score = nr_list.size() + 1;
            final String pik = String.valueOf(score);
//            youWin();
            return true;

        }


        if (t1.length() > 0 || t2.length() > 0 || t3.length() > 0) {
            msg += fiksuar + " fiksuar," + qelluar + " qelluar";
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.customitem, nr_list);


            nr_list.add(t1.getText().toString() + t2.getText().toString() + t3.getText().toString() + "--- " + msg);

            arrayAdapter.notifyDataSetChanged();
            lv.setAdapter(arrayAdapter);
            lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

            insertDatabase(t1.getText().toString(),t2.getText().toString(),t3.getText().toString());

        }


        return false;
    }

    public void checkRoom(){
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("rooms/");

        dbRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(getRoomid())){
//                    checkPlayers();
                }
                else{

                    Toast.makeText(getApplication(),"Room closed",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MultiThree.this,MainActivity.class);
                    startActivity(i);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    
    public void checkOpponentsTries(String t1,String t2,String t3){



            enableButtons();

            lv.setFocusable(true);

            int qelluar = 0;
            int fiksuar = 0;
            String msg = "";
            if (t1.equalsIgnoreCase("" + getRnd1())) {
                fiksuar++;

            }
            if (t2.equalsIgnoreCase("" + getRnd2())) {
                fiksuar++;

            }
            if (t3.equalsIgnoreCase("" + getRnd3())) {
                fiksuar++;

            }
            if (t1.equalsIgnoreCase("" + getRnd2())) {
                qelluar++;
                //msg+=fiksuar+" fiksuar," + qelluar + " qelluar";

            }
            if (t1.equalsIgnoreCase("" + getRnd3())) {
                qelluar++;
            }
            if (t2.equalsIgnoreCase("" + getRnd1())) {
                qelluar++;
                //msg+=fiksuar+" fiksuar," + qelluar + " qelluar";

            }
            if (t2.equalsIgnoreCase("" + getRnd3())) {
                qelluar++;

            }
            if (t3.equalsIgnoreCase("" + getRnd2())) {
                qelluar++;
                //msg+=fiksuar+" fiksuar," + qelluar + " qelluar";

            }
            if (t3.equalsIgnoreCase("" + getRnd1())) {
                qelluar++;
            }
            if (fiksuar == 3) {
                qelluar = 0;
                rndm.setVisibility(View.VISIBLE);
                rndm.setText("Sakte : " + rnd1 + rnd2 + rnd3);
//            showAnimation();
//            showDialog();
                final int score = nr_list.size() + 1;


                final String pik = String.valueOf(score);

            }
            if (t1.length() > 0 && t2.length() > 0 && t3.length() > 0) {

                msg += fiksuar + " fiksuar," + qelluar + " qelluar";
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.customitem, opponentArrayList);



                if (opponentArrayList.contains(t1+t2+t3+  " --- " + msg)) {
//                        opponentArrayList.add("Not played this round");
                    }


                else {
                    opponentArrayList.add(t1 + t2+ t3+  " --- " + msg);

                }


                arrayAdapter.notifyDataSetChanged();
                mLv.setAdapter(arrayAdapter);
                mLv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

            }

        }

    private void getOpponentsTries(){
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("rooms/");

        dbRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(getRoomid())){
                    if (dataSnapshot.child(getRoomid()).child("tries").child(getpTwoId()).getValue().toString().length() > 2) {
                        String oTry1 = String.valueOf(dataSnapshot.child(getRoomid()).child("tries").child(getpTwoId()).getValue().toString().toString().charAt(0));
                        String oTry2 = String.valueOf(dataSnapshot.child(getRoomid()).child("tries").child(getpTwoId()).getValue().toString().toString().charAt(1));
                        String oTry3 = String.valueOf(dataSnapshot.child(getRoomid()).child("tries").child(getpTwoId()).getValue().toString().charAt(2));

                        checkOpponentsTries(oTry1, oTry2, oTry3);

                        startCountDown();
                        checkWinner();

                    }
                }
                else {
                    Intent i = new Intent(MultiThree.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void reset(){
        t1.setText("");
        t2.setText("");
        t3.setText("");
        t1.requestFocus();
        enableButtons();
    }//end of reset()

    public void setTurn(String turn){
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("rooms/"+getRoomid());

        dbRef.child("turn").setValue(turn);
    }

    public void getRandoms(){
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("rooms");

        dbRef.child(getRoomid()).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child("Randoms").exists()) {
                setRnd1(dataSnapshot.child("Randoms").child("rnd1").getValue().toString());
                setRnd2(dataSnapshot.child("Randoms").child("rnd2").getValue().toString());
                setRnd3(dataSnapshot.child("Randoms").child("rnd3").getValue().toString());
           

//                Toast.makeText(getApplicationContext(), "Random 1 : " + getRnd1(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), "Random 2 : " + getRnd2(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), "Random 3 : " + getRnd3(), Toast.LENGTH_SHORT).show();
            }

//                if (dataSnapshot.child(mAuth.getCurrentUser().getUid()).exists()) {
//                    setRnd1(dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("Randoms").child("rnd1").getValue().toString());
//                    setRnd2(dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("Randoms").child("rnd2").getValue().toString());
//                    setRnd3(dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("Randoms").child("rnd3").getValue().toString());
//
//
//                    Toast.makeText(getApplicationContext(), "Random 1 : " + getRnd1(), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getApplicationContext(), "Random 2 : " + getRnd2(), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getApplicationContext(), "Random 3 : " + getRnd3(), Toast.LENGTH_SHORT).show();
//                }
//                    
//                    Toast.makeText(getApplicationContext(), "ChildCount : " + dataSnapshot.child(getpTwoId()).getChildrenCount(), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getApplicationContext(), "id 2 : " + getpTwoId(), Toast.LENGTH_SHORT).show();
                }
            

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();

            }
        });




//        roomsRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                    if (dataSnapshot.child(mAuth.getCurrentUser().getUid()).exists()) {
//                        setRnd1(dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("Randoms").child("rnd1").getValue().toString());
//                        setRnd2(dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("Randoms").child("rnd2").getValue().toString());
//                        setRnd3(dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("Randoms").child("rnd3").getValue().toString());
//                    }
//
//                    if (dataSnapshot.child(getpTwoId().toString()).exists()) {
//                        setRnd1(dataSnapshot.child(getpTwoId()).child("Randoms").child("rnd1").getValue().toString());
//                        setRnd2(dataSnapshot.child(getpTwoId()).child("Randoms").child("rnd2").getValue().toString());
//                        setRnd3(dataSnapshot.child(getpTwoId()).child("Randoms").child("rnd3").getValue().toString());
//                    }
//
//                    Toast.makeText(getApplicationContext(), "Random 1 : " + getRnd1(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), "Random 2 : " + getRnd2(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), "Random 3 : " + getRnd3(), Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });


    }

    public void enableButtons(){
        button0.setEnabled(true);
        button1.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);
        button4.setEnabled(true);
        button5.setEnabled(true);
        button6.setEnabled(true);
        button7.setEnabled(true);
        button8.setEnabled(true);
        button9.setEnabled(true);


    }

    public void youWin(){
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("rooms/"+getRoomid());
        dbRef.child("winner").setValue(mAuth.getCurrentUser().getUid());

        showDialog();
    }

    public void checkWinner(){
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("rooms/"+getRoomid());

        dbRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("winner").exists()) {
                    if (!dataSnapshot.child("winner").getValue().equals(mAuth.getCurrentUser().getUid())) {
                        youLose();
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void showDialog() {

        String t="";
        if((nr_list.size()+1)>1){
            t+= "tries";

        }
        else{
            t+="try";

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(MultiThree.this);
        builder.setTitle("Game Finished");
        builder.setMessage("You finished in : "+(nr_list.size()+1)+" "+t+"\n"+"Start new game or go to main menu");
//        builder.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                lv.setAdapter(null);
//                reset();
//                Toast.makeText(getApplication(),"Soon",Toast.LENGTH_SHORT).show();
//
//            }
//        });
        builder.setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                quitGame();
            }
        });
        Profile p = Profile.getCurrentProfile();
        if (p != null) {

            final String finalT = t;
            builder.setNeutralButton("Share your score", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, "Finished in : "+(nr_list.size()+1)+" "+ finalT);
                    startActivity(Intent.createChooser(intent, "Share with"));
                }
            });}
        builder.setCancelable(false);

        builder.show();

    }//end of showDialog()

    public void youLose(){
        if (mCountDownView.isStarted()){
            mCountDownView.cancel();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(MultiThree.this);
        builder.setTitle("Game Finished");
        builder.setMessage("You lose");
        builder.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                lv.setAdapter(null);
                reset();

            }
        });
        builder.setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });

        builder.setCancelable(false);

        builder.show();
        rndm.setVisibility(View.VISIBLE);
        rndm.setText("Sakte : " + rnd1 + rnd2 + rnd3);

    }

    public void insertInGame(){
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("rooms/"+getRoomid());

        dbRef.child("ingame").child(mAuth.getCurrentUser().getUid()).setValue(mAuth.getCurrentUser().getUid());
//        showProgressDialog();
    }

    public void startCountDown(){
//        gridLayout.setVisibility(View.INVISIBLE);
        if (mCountDownView.isFinished()){
        mCountDownView.cancel();}
        mCountDownView.setVisibility(View.VISIBLE);
        mCountDownView.setTimer(10000);
        mCountDownView.start();
        mCountDownView.setListener(new ContinuableCircleCountDownView.OnCountDownCompletedListener() {
            @Override
            public void onTick(long passedMillis) {

            }

            @Override
            public void onCompleted() {
                insertDatabase("c","c","c");
                setTurn(getpTwoId());
                checkTurn();

//                mCountDownView.stop();



                mCountDownView.setVisibility(View.INVISIBLE);
                gridLayout.setVisibility(View.GONE);


            }
        });
    }

    public void stopCountdown(){
        if (mCountDownView.isStarted()){

            mCountDownView.cancel();}
    }


    public void checkTurn(){
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("rooms/");

        dbRef.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(getRoomid())) {
                    if (dataSnapshot.child(getRoomid()).child("turn").getValue().equals(mAuth.getCurrentUser().getUid())) {
                        getOpponentsTries();
                        gridLayout.setVisibility(View.VISIBLE);
                        stopCountdown();
                        startCountDown();
                    }
                    if (!dataSnapshot.child(getRoomid()).child("turn").getValue().equals(mAuth.getCurrentUser().getUid())) {
                        gridLayout.setVisibility(View.GONE);
//                    startCountDown();
                    }
                }
                else {
                    stopCountdown();

                    Intent i = new Intent(MultiThree.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //end of check()
    public void showAnimation(){
        new ParticleSystem(this, 80, R.drawable.confeti2, 2000)
                .setSpeedRange(0.2f,0.5f)
                .oneShot(findViewById(R.id.emiter_top_right),80);
    }

    public void gridButtons(){
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                while (true) {
                    if (t1.hasFocus()) {
                        t1.setText("1");
                        break;
                    }

                    if (t2.hasFocus()) {
                        t2.setText("1");
                        break;
                    }
                    if (t3.hasFocus()) {
                        t3.setText("1");
                        break;
                    }


                }
                button1.setEnabled(false);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                while (true) {
                    if (t1.hasFocus()) {
                        t1.setText("2");
                        break;
                    }

                    if (t2.hasFocus()) {
                        t2.setText("2");
                        break;
                    }
                    if (t3.hasFocus()) {
                        t3.setText("2");
                        break;
                    }

                }button2.setEnabled(false);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                while (true) {
                    if (t1.hasFocus()) {
                        t1.setText("3");
                        break;
                    }

                    if (t2.hasFocus()) {
                        t2.setText("3");
                        break;
                    }
                    if (t3.hasFocus()) {
                        t3.setText("3");
                        break;
                    }

                }button3.setEnabled(false);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                while (true) {
                    if (t1.hasFocus()) {
                        t1.setText("4");
                        break;
                    }

                    if (t2.hasFocus()) {
                        t2.setText("4");
                        break;
                    }
                    if (t3.hasFocus()) {
                        t3.setText("4");
                        break;
                    }

                }button4.setEnabled(false);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                while (true) {
                    if (t1.hasFocus()) {
                        t1.setText("5");
                        break;
                    }

                    if (t2.hasFocus()) {
                        t2.setText("5");
                        break;
                    }
                    if (t3.hasFocus()) {
                        t3.setText("5");
                        break;
                    }

                }button5.setEnabled(false);
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                while (true) {
                    if (t1.hasFocus()) {
                        t1.setText("6");
                        break;
                    }

                    if (t2.hasFocus()) {
                        t2.setText("6");
                        break;
                    }
                    if (t3.hasFocus()) {
                        t3.setText("6");
                        break;
                    }

                }button6.setEnabled(false);
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                while (true) {
                    if (t1.hasFocus()) {
                        t1.setText("7");
                        break;
                    }

                    if (t2.hasFocus()) {
                        t2.setText("7");
                        break;
                    }
                    if (t3.hasFocus()) {
                        t3.setText("7");
                        break;
                    }

                }button7.setEnabled(false);
            }
        });
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                while (true) {
                    if (t1.hasFocus()) {
                        t1.setText("8");
                        break;
                    }

                    if (t2.hasFocus()) {
                        t2.setText("8");
                        break;
                    }
                    if (t3.hasFocus()) {
                        t3.setText("8");
                        break;
                    }

                }button8.setEnabled(false);
            }
        });
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                while (true) {
                    if (t1.hasFocus()) {
                        t1.setText("9");
                        break;
                    }

                    if (t2.hasFocus()) {
                        t2.setText("9");
                        break;
                    }
                    if (t3.hasFocus()) {
                        t3.setText("9");
                        break;
                    }

                }button9.setEnabled(false);
            }
        });
        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                while (true) {
                    if (t1.hasFocus()) {
                        t1.setText("0");
                        break;
                    }

                    if (t2.hasFocus()) {
                        t2.setText("0");
                        break;
                    }
                    if (t3.hasFocus()) {
                        t3.setText("0");
                        break;
                    }

                }button0.setEnabled(false);
            }
        });

    }

    public void UiElements() {
        playerOneImg = (ImageView)findViewById(R.id.playerOneImg);
        playerTwoImg =(ImageView)findViewById(R.id.playerTwoImg);

        playerOneText = (TextView)findViewById(R.id.playerOneText);
        playerTwoText = (TextView)findViewById(R.id.playerTwoText);
        lv = (ListView) findViewById(R.id.lv);
        mLv = (ListView) findViewById(R.id.mLv);
        mCountDownView = (ContinuableCircleCountDownView) findViewById(R.id.circleCountDownView);
        gridLayout = (GridLayout)findViewById(R.id.gridLayout);
        t1 = (TextView) findViewById(R.id.t1);
        t2 = (TextView) findViewById(R.id.t2);
        t3 = (TextView) findViewById(R.id.t3);
        check = (Button) findViewById(R.id.buttonCheck);
        newGame = (Button) findViewById(R.id.newGame);
        reset = (Button) findViewById(R.id.buttonClear);
        rndm = (TextView) findViewById(R.id.rndm);
        quitgame = (Button) findViewById(R.id.quitgame);
        button1=(Button) findViewById(R.id.button1);
        button2=(Button) findViewById(R.id.button2);
        button3=(Button) findViewById(R.id.button3);
        button4=(Button) findViewById(R.id.button4);
        button5=(Button) findViewById(R.id.button5);
        button6=(Button) findViewById(R.id.button6);
        button7=(Button) findViewById(R.id.button7);
        button8=(Button) findViewById(R.id.button8);
        button9=(Button) findViewById(R.id.button9);
        button0=(Button) findViewById(R.id.button0);
        buttonClear=(Button) findViewById(R.id.buttonClear);
        buttonCheck =(Button) findViewById(R.id.buttonCheck);

    }//end of UiElements()

    private void insertDatabase(final String r1, final String r2, final String r3){
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("rooms/"+getRoomid());
        dbRef.child("tries").child(mAuth.getCurrentUser().getUid()).setValue(r1+r2+r3);
//        dbRef.child("tries").child(mAuth.getCurrentUser().getUid()).child("t2").setValue(r2);
//        dbRef.child("tries").child(mAuth.getCurrentUser().getUid()).child("t3").setValue(r3);

//        dbRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
//            @Override
//            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
//
//
//                    Toast.makeText(getApplication(),"phone getKey :"+dbRef.child(mAuth.getCurrentUser().getUid()).child("tries").push().getKey().toString(),Toast.LENGTH_LONG).show();
//
//
//
//                }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    @Override
    protected void onStop() {

//        deleteRoom();
//        MultiThree.this.finish();
        super.onStop();
    }

    @Override
    protected void onDestroy() {

//        MultiThree.this.finish();
        super.onDestroy();
    }

    public void deleteRoom(){
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("rooms");

        dbRef.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(getRoomid())){
                    dbRef.child(getRoomid()).removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void quitDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MultiThree.this);
        builder.setTitle("Quit Game");
        builder.setMessage("Are you sure to quit game ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                quitGame();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        builder.show();

    }

    @Override
    public void onBackPressed() {

        quitDialog();
    }

    public void quitGame(){

        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("rooms");

        dbRef.child(getRoomid()).child("Players").child(mAuth.getCurrentUser().getUid()).setValue("quit");

        Intent i = new Intent(MultiThree.this,MainActivity.class);

        startActivity(i);
        finish();
    }


    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    public void saveBitmap(Bitmap bitmap) {
        File imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshot.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }
}