package com.example.shukri.fixed;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Test extends AppCompatActivity {
    ArrayList<String> listItems;

    String[] items;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ListView lv;
    EditText searchtext;
    ArrayList<String> arr = new ArrayList<>();
    ArrayList<Uri>arr2 = new ArrayList<>();
    SearchView searchView;
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String> picAdapter;
    private FirebaseDatabase mDatabase;
    ArrayList<String> picArray = new ArrayList<>();
    private StorageReference mStorageRef;

    LinearLayout linv;
    ImageView profilepic;
    Firebase myFirebaseRef=new Firebase("https://fixed-7dac1.firebaseio.com/users/");


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("onStart","true");
        if(mAuth.getCurrentUser()!=null) {
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
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                arrayAdapter.getFilter().filter(newText);
//                linv.setVisibility(View.INVISIBLE);

                return false;

            }

        });


        return super.onCreateOptionsMenu(menu);


    }

    private int i = 0;
    private int j = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_AppBarOverlay);
        setContentView(R.layout.activity_test);


        mStorageRef = FirebaseStorage.getInstance().getReference();

        linv= (LinearLayout)findViewById(R.id.linV);
//        profilepic = (ImageView)findViewById(R.id.profilepic);
        final ListView lv = (ListView) findViewById(R.id.lv);
        arrayAdapter = new ArrayAdapter<String>(Test.this, R.layout.customitem, R.id.itemtext, arr);

        mDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference dbRef = mDatabase.getReference("users");



        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                    myFirebaseRef.child(mUser.getUid()).child("status").setValue("Online");

                }
            }
        };

        final String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {

                         int dimensionInPixel=50;
                        int length = (int) dataSnapshot.getChildrenCount();
                        final ArrayList<String> idString = new ArrayList<String>(length);
                        int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimensionInPixel, getResources().getDisplayMetrics());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dimensionInDp, dimensionInDp);
                        lp.setMargins(0,16,0,0);


                        for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                            Log.d("ifkey", ds.getKey());
                            if (!ds.getKey().equals(user_id)) {
                                if (ds.hasChild("lastname")) {
                                    arr.add(ds.child("name").getValue().toString() + " " + ds.child("lastname").getValue().toString());
                                    idString.add(i, ds.child("id").getValue().toString());

                                    final ImageView iv = new ImageView(getApplicationContext());
                                    iv.setScaleType(ImageView.ScaleType.FIT_XY);
                                    iv.setVisibility(View.VISIBLE);
//                                    iv.setAdjustViewBounds(true);

                                    iv.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
                                    iv.setLayoutParams(lp);




                                    Log.d("mstorage",""+mStorageRef);
                                    mStorageRef.child("photo").child(idString.get(i)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Picasso.with(Test.this).load(uri).into(iv);


                                        }
                                    });
                                    ((LinearLayout)findViewById(R.id.linV)).addView(iv);




                                    i++;

                                }
                                else {
                                    if(ds.hasChild("name")){
                                    arr.add(ds.child("name").getValue().toString());}
                                    String photoUrl = ds.child("photo").getValue().toString();
                                    final ImageView iv = new ImageView(getApplicationContext());
                                    iv.setScaleType(ImageView.ScaleType.FIT_XY);
                                    iv.setVisibility(View.VISIBLE);
//                                    iv.setAdjustViewBounds(true);

                                    iv.setLayoutParams(lp);

                                   Picasso.with(Test.this).load(photoUrl).into(iv);
                                    ((LinearLayout)findViewById(R.id.linV)).addView(iv);

                                    idString.add(i, ds.child("id").getValue().toString());
                                    i++;

                                    Log.d("username", user_id);

                                    Log.d("idString", "");
                                    Log.d("getkey", ds.getKey());

                                }
                            }

                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    Toast.makeText(getApplication(), "id :" + idString.get(position), Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(Test.this, usertest.class);
                                    i.putExtra("userid", idString.get(position));
                                    startActivity(i);
                                }
                            });
                            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                    final AlertDialog.Builder dialog = new AlertDialog.Builder(Test.this)
                                            .setTitle("ok").setMessage(
                                                    "Send Game Request ...");
                                    final AlertDialog alert = dialog.create();

                                    alert.show();

                                    return false;
                                }
                            });

                        }
                        Log.d("arr.size", "" + arr.size());
                        lv.setAdapter(arrayAdapter);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }



    }

