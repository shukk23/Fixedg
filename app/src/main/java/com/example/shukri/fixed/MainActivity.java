package com.example.shukri.fixed;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Firebase myFirebaseRef;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView user_name;
    private LinearLayout user_info;
    private ImageButton logout;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private StorageReference mStorageRef;
    String inviter;
    String inviterName;


    @Override
    public void onStart() {
        Log.d("onStartM","true");
        super.onStart();
        if(mAuth.getCurrentUser()!=null) {
            myFirebaseRef.child(mAuth.getCurrentUser().getUid()).child("status").setValue("Online");
        }
//        mAuth.addAuthStateListener(mAuthListener);
    }



    //    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.d("onPauseM","true");
//        if(mAuth.getCurrentUser()!=null) {
//            myFirebaseRef.child(mAuth.getCurrentUser().getUid()).child("status").setValue("Offline");
//        }
//    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d("onStopM","true");
//        if(mAuth.getCurrentUser()!=null) {
//            myFirebaseRef.child(mAuth.getCurrentUser().getUid()).child("status").setValue("Offline");
//        }
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
    }

    @Override
    public void onBackPressed(){
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        myFirebaseRef = new Firebase("https://fixed-7dac1.firebaseio.com/users");
       mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                }
            }
        };
        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull final MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_play:
                        Play_Fragment p = new Play_Fragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.content,p).commit();
                        item.setChecked(true);
                        getFragmentManager().popBackStackImmediate();

                        return false;
                    case R.id.navigation_profile:

                        if(mAuth.getCurrentUser()!=null){
                            Profile_Fragment pf = new Profile_Fragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.content,pf).commit();
                            item.setChecked(true);
                            getFragmentManager().popBackStackImmediate();

                            return true;
                        }
                        else {
//                            Login_Fragment lf = new Login_Fragment();
//                            getSupportFragmentManager().beginTransaction().replace(R.id.content, lf).commit();
                            Intent i  = new Intent(getApplicationContext(),LoginActivity.class);
                            startActivity(i);
                            getFragmentManager().popBackStackImmediate();

                            item.setChecked(true);
                            return true;
                        }


                    case R.id.navigation_highscores:
                        Tab1 tab1 = new Tab1();
                        getSupportFragmentManager().beginTransaction().replace(R.id.content,tab1).commit();
                        item.setChecked(true);

                        return true;
                    case R.id.navigation_settings:

                        Settings_Fragment settings_fragment = new Settings_Fragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.content,settings_fragment).commit();
                        item.setChecked(true);


                        return true;


                }
                return true;
            }

        };BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
                navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

                Menu menu = navigation.getMenu();
                mOnNavigationItemSelectedListener.onNavigationItemSelected(menu.findItem(R.id.navigation_play));




            }
        }



