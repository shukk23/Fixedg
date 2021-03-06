package com.example.shukri.fixed;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.plattysoft.leonids.ParticleSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class fournumbers extends AppCompatActivity {

    private Firebase myFirebaseRef;

    ListView lv;
    TextView t1,t2,t3,t4;
    Button check,newGame,reset,button1,button2,button3,button4,button5,button6,button7,button8,button9,button0,buttonClear, buttonCheck,quitgame;
    TextView rndm,textView4;
    int rnd1;
    int rnd2;
    int rnd3;
    int rnd4;

    final String[] numratVarg = new String[]{};
    final List<String> nr_list = new ArrayList<String>(Arrays.asList(numratVarg));
    Chronometer chronometer;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;
// ...


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(fournumbers.this, MainActivity.class));


    }

    @Override
    public void onStart() {
        super.onStart();
        Firebase.setAndroidContext(this);if(mAuth.getCurrentUser()!=null) {
            myFirebaseRef.child(mAuth.getCurrentUser().getUid()).child("status").setValue("Online");
        }
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(mAuth.getCurrentUser()!=null) {
            myFirebaseRef.child(mAuth.getCurrentUser().getUid()).child("status").setValue("Offline");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fournumbers);
        UiElements();
        newGame();
        mAuth=FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        startDialog();
        Log.d("clock",""+ SystemClock.elapsedRealtime());
        myFirebaseRef = new Firebase("https://fixed-7dac1.firebaseio.com/users");
        gridButtons();
//        myFirebaseRef.child(mAuth.getCurrentUser().getUid()).child("status").setValue("In-Game");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

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
        t3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (t3.getText().toString().trim().length() != 0) {

                    t4.requestFocus();
                    t4.setFocusable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        t4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    t4.setBackgroundResource(R.drawable.common_google_signin_btn_icon_dark_normal_background);

                }
                else{
                    t4.setBackgroundResource(R.drawable.common_google_signin_btn_icon_light_normal_background);

                }
            }
        });

        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//

                check();
                reset();

            }
        });

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(fournumbers.this);
                builder.setTitle("Rifillo Lojen");
                builder.setMessage("A jeni i sigrt qe te rifillohet loja?");
                builder.setPositiveButton("Po", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        lv.setAdapter(null);
                        reset();
                        newGame();
                        Toast.makeText(getApplication(), "Loja e re ka filluar",
                                Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Anulo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });

                builder.show();

//                reset();
//
//
//                lv.setAdapter(null);
//                newGame();



            }
        });

        quitgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(fournumbers.this);
                builder.setTitle("Dil nga loja");
                builder.setMessage("A jeni i sigurt qe te dilet nga loja ?");
                builder.setPositiveButton("Po", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(fournumbers.this,MainActivity.class);
                        startActivity(i);
                    }
                });
                builder.setNegativeButton("Anulo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });

                builder.show();

//                reset();
//
//
//                lv.setAdapter(null);
//                newGame();



            }
        });

    }//end of onCreate
    public void UiElements() {
        lv = (ListView) findViewById(R.id.lv);
        t1 = (TextView) findViewById(R.id.t1);
        t2 = (TextView) findViewById(R.id.t2);
        t3 = (TextView) findViewById(R.id.t3);
        t4 = (TextView) findViewById(R.id.t4);

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
    public void newGame(){
//    int[] numrat = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        rndm.setVisibility(View.INVISIBLE);
        enableButtons();
        nr_list.clear();
        ArrayList<Integer> nr=new ArrayList<>();
        nr.add(0);
        nr.add(1);
        nr.add(2);
        nr.add(3);
        nr.add(4);
        nr.add(5);
        nr.add(6);
        nr.add(7);
        nr.add(8);
        nr.add(9);

        Random r = new Random();
        int rndPos1 = r.nextInt(nr.size());
        rnd1 = nr.remove(rndPos1);

        int rndPos2 = r.nextInt(nr.size());
        rnd2 = nr.remove(rndPos2);

        int rndPos3 = r.nextInt(nr.size());
        rnd3 = nr.remove(rndPos3);

        int rndPos4 = r.nextInt(nr.size());
        rnd4= nr.remove(rndPos4);

        rndm.setText(""+rnd1+rnd2+rnd3+rnd4);
    }//end of newGame()
    public void check(){

        enableButtons();
        t3.clearFocus();
        lv.setFocusable(true);

        int qelluar = 0;
        int fiksuar = 0;
        String msg = "";
        if(t1.getText().toString().equalsIgnoreCase(""+rnd1)){
            fiksuar++;

        }
        if (t2.getText().toString().equalsIgnoreCase(""+rnd2)){
            fiksuar++;

        }
        if(t3.getText().toString().equalsIgnoreCase(""+rnd3)){
            fiksuar++;

        }
        if(t4.getText().toString().equalsIgnoreCase(""+rnd4)){
            fiksuar++;

        }
        if (t1.getText().toString().equalsIgnoreCase(""+rnd2) ){
            qelluar++;
            //msg+=fiksuar+" fiksuar," + qelluar + " qelluar";

        }
        if (t1.getText().toString().equalsIgnoreCase(""+rnd3)){
            qelluar++;
        }
        if(t1.getText().toString().equalsIgnoreCase(""+rnd4)){
            fiksuar++;

        }
        if (t2.getText().toString().equalsIgnoreCase(""+rnd1) ){
            qelluar++;
            //msg+=fiksuar+" fiksuar," + qelluar + " qelluar";

        }
        if (t2.getText().toString().equalsIgnoreCase(""+rnd3)){
            qelluar++;

        }
        if (t2.getText().toString().equalsIgnoreCase(""+rnd4)){
            qelluar++;

        }
        if (t3.getText().toString().equalsIgnoreCase(""+rnd2) ){
            qelluar++;
            //msg+=fiksuar+" fiksuar," + qelluar + " qelluar";

        }
        if (t3.getText().toString().equalsIgnoreCase(""+rnd1)){
            qelluar++;
        }
        if (t3.getText().toString().equalsIgnoreCase(""+rnd4)){
            qelluar++;
        }

        if (t4.getText().toString().equalsIgnoreCase(""+rnd1)){
            qelluar++;
        }
        if (t4.getText().toString().equalsIgnoreCase(""+rnd2)){
            qelluar++;
        }
        if (t4.getText().toString().equalsIgnoreCase(""+rnd3)){
            qelluar++;
        }

        if(fiksuar==4){
            qelluar=0;
            rndm.setVisibility(View.VISIBLE);
            rndm.setText("Sakte : "+rnd1+rnd2+rnd3+rnd4);
            showAnimation();
            showDialog();
            final int score = nr_list.size()+1;


            final String pik = String.valueOf(score);

            if(mAuth.getCurrentUser()!=null){
                String emri = null;
                String email=null;
                if (mAuth.getCurrentUser().getDisplayName() != null) {


                    emri = mAuth.getCurrentUser().getDisplayName();
                }

                final DatabaseReference dbRef = mDatabase.getReference("highscores/");
                final String key = dbRef.push().getKey();
                final DatabaseReference keyli = mDatabase.getReference("highscores/");


                final String finalEmri = emri;
                final String finalEmail = email;
                Log.d("finalem",""+finalEmail);
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        if(dataSnapshot.hasChild(finalEmri)==false){

                            dbRef.child(finalEmri).setValue(pik);
                            Toast.makeText(getApplication(),"Added",Toast.LENGTH_SHORT).show();
                        }

                        if(dataSnapshot.hasChild(finalEmri)==true) {
                            String piktdb = dataSnapshot.child(finalEmri).getValue().toString();
                            Log.d("integ", "" + Integer.parseInt(piktdb));
                            if(score<Integer.parseInt(piktdb)){
                                dbRef.child(finalEmri).setValue(pik);
                                Toast.makeText(getApplication(),"Your new highscore is : "+score,Toast.LENGTH_SHORT).show();

                            }
//                            if(dataSnapshot.hasChild(finalEmail)==false){
//
//                                dbRef.child(finalEmail).setValue(pik);
//                                Toast.makeText(getApplication(),"Added",Toast.LENGTH_SHORT).show();
//
//                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("score", emri+" "+pik);
                editor.apply();
            }
        }
        if(t1.length()>0 || t2.length()>0 || t3.length()>0 || t4.length()>0) {
            msg += fiksuar + " fiksuar," + qelluar + " qelluar";
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.customitem , nr_list);



            nr_list.add(t1.getText().toString() + t2.getText().toString() + t3.getText().toString() + t4.getText().toString()+ "--- " + msg);

            arrayAdapter.notifyDataSetChanged();
            lv.setAdapter(arrayAdapter);
            lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);


        }

    }//end of check()
    public void showAnimation(){
        new ParticleSystem(this, 80, R.drawable.confeti2, 2000)
                .setSpeedRange(0.2f,0.5f)
                .oneShot(findViewById(R.id.emiter_top_right),80);
    }
    public void showDialog() {

        String t="";
        if((nr_list.size()+1)>1){
            t+= "tries";

        }
        else{
            t+="try";

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(fournumbers.this);
        builder.setTitle("Game Finished");
        builder.setMessage("You finished in : "+(nr_list.size()+1)+" "+t+"\n"+"Start new game or go to main menu");
        builder.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                lv.setAdapter(null);
                reset();
                newGame();
            }
        });
        builder.setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void startDialog() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle("").setMessage(
                        "Loja po fillon ...");

        final AlertDialog alert = dialog.create();
        alert.show();
        new CountDownTimer(2000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub

                alert.dismiss();
            }
        }.start();





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
                    if (t4.hasFocus()) {
                        t4.setText("1");
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
                    }if (t4.hasFocus()) {
                        t4.setText("2");
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
                    }if (t4.hasFocus()) {
                        t4.setText("3");
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
                    }if (t4.hasFocus()) {
                        t4.setText("4");
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
                    }if (t4.hasFocus()) {
                        t4.setText("5");
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
                    }if (t4.hasFocus()) {
                        t4.setText("6");
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
                    }if (t4.hasFocus()) {
                        t4.setText("7");
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
                    }if (t4.hasFocus()) {
                        t4.setText("8");
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
                    }if (t4.hasFocus()) {
                        t4.setText("9");
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
                    }if (t4.hasFocus()) {
                        t4.setText("0");
                        break;
                    }

                }button0.setEnabled(false);
            }
        });

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



    public void reset(){
        t1.setText("");
        t2.setText("");
        t3.setText("");
        t4.setText("");

        t1.requestFocus();
        enableButtons();
    }//end of reset()
    public void getScore(){
        int score= nr_list.size();
    }


//    @Override
//    public void onStop() {
//        super.onStop();
//
//        myFirebaseRef.child(mAuth.getCurrentUser().getUid()).child("status").setValue("Offline");
//
//    }
}
