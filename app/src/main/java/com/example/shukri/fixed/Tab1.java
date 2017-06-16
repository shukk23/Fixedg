package com.example.shukri.fixed;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Tab1 extends android.support.v4.app.Fragment {
    ListView lv;
    final String[] score=new String[]{};
    int scoreList;
    final List<String>score_list = new ArrayList<>(Arrays.asList(score));
    private FirebaseDatabase mDatabase;

    ArrayList<String>arr = new ArrayList<>();
    private SectionsPagerAdapter mSectionsPagerAdapter;
   private ViewPager mViewPager;
   TabLayout tabLayout;
    RelativeLayout relative;
    ImageButton imageButton5;
    ImageButton imageButton6;
    Button threenum, fournum;
    FrameLayout three, four;
    TextView rank,emri,pikt;
    LinearLayout names;
    View view;
    public FirebaseAuth mAuth;
    public Firebase myFirebaseRef;
    public FirebaseAuth.AuthStateListener mAuthListener;

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




    private int i = 0;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.tab1, container, false);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                }
            }
        };
        names = (LinearLayout) rootView.findViewById(R.id.names);

        three = (FrameLayout)rootView.findViewById(R.id.three);
        four = (FrameLayout)rootView.findViewById(R.id.four);
        imageButton5 = (ImageButton)rootView.findViewById(R.id.imageButton5);
        imageButton6 = (ImageButton)rootView.findViewById(R.id.imageButton6);
        fournum = (Button)rootView.findViewById(R.id.fournum);
        threenum = (Button)rootView.findViewById(R.id.threenum);
//        relative = (RelativeLayout)rootView.findViewById(R.id.relative);
//        int scoreList=getArguments().getInt("score");
        mViewPager = (ViewPager)rootView. findViewById(R.id.container);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        rank =(TextView)rootView.findViewById(R.id.rank);
        emri =(TextView)rootView.findViewById(R.id.emri);
        pikt =(TextView)rootView.findViewById(R.id.pikt);

        view = rootView.findViewById(R.id.view8);
        mAuth=FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference dbRef = mDatabase.getReference();
        // Set up the ViewPager with the sections adapter.
        lv = (ListView)rootView.findViewById(R.id.lv);
        final ArrayAdapter<String> arrayAdapter =new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,arr);


        dbRef.child("highscores").orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int r =1;
                int length = (int) dataSnapshot.getChildrenCount();
                final String[] idString = new String[length];
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    arr.add(r+"."+ds.getKey()+"--"+ds.getValue());

//
//                    while(i < length) {
//                        idString[i] = iterator.next().getChildren().iterator().next().getKey().toString();
//                        Log.d(Integer.toString(i), idString[i]);
//                        i++;
//                        dbRef.child("users").child(idString[i]).child("rank").setValue(r);
//
//                    }

                    if(r==1){

                        rank.setText(""+r+".");
                        emri.setText(""+ds.getKey());
                        pikt.setText(""+ds.getValue());
                    }
                    else {
                        rank.setPaddingRelative(15,15,15,15);

                        emri.setPadding(5,5,5,5);
                        pikt.setPadding(5,5,5,5);
                        rank.append("\n" + r + ".");

                        emri.append("\n" + ds.getKey());
                        pikt.append("\n" + ds.getValue());
                    }
                    r++;
                    Log.d("datax",dataSnapshot.getValue().toString());
                }
                lv.setAdapter(arrayAdapter);





            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sc = pref.getString("score","nothing");


        fournum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("btn","clk");
                fouractive();
                three.setVisibility(View.INVISIBLE);
                four.setVisibility(View.VISIBLE);

            }
        });
        threenum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("btn","clk");
               threeactive();
                four.setVisibility(View.INVISIBLE);
                three.setVisibility(View.VISIBLE);


            }
        });

        return rootView;
    }

public void threeactive(){
    fournum.setTextColor(Color.BLACK);
    imageButton5.setImageResource(R.drawable.ic_looks_4_black_24dp);
    imageButton6.setImageResource(R.drawable.ic_looks_3_hover_24dp);
    threenum.setTextColor(getResources().getColor(R.color.colorPrimary));

}

    public void fouractive(){
        fournum.setTextColor(getResources().getColor(R.color.colorPrimary));
        imageButton5.setImageResource(R.drawable.ic_looks_4_hover_24dp);
        imageButton6.setImageResource(R.drawable.ic_looks_3_black_24dp);
        threenum.setTextColor(Color.BLACK);

    }

}
