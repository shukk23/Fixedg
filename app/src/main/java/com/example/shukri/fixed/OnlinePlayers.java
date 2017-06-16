package com.example.shukri.fixed;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class OnlinePlayers extends AppCompatActivity {
    private SwipeRefreshLayout swipeContainer;


    SearchView searchView;
    ImageView imageView;
    private Firebase dbRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ArrayList<String> arr = new ArrayList<>();
    ArrayList<String> picArr = new ArrayList<>();
    ArrayAdapter<String> picarrayAdapter;
    ArrayAdapter<String> arrayAdapter;
    ListView listv;
    CustomListAdapter customListAdapter;
    public static ArrayList<Players> arrayList = new ArrayList<Players>();

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        if (mAuth.getCurrentUser() != null) {
            dbRef.child(mAuth.getCurrentUser().getUid()).child("status").setValue("Online");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search by name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                customListAdapter.getFilter().filter(newText);
                customListAdapter.notifyDataSetChanged();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuth.getCurrentUser() != null) {
            dbRef.child(mAuth.getCurrentUser().getUid()).child("status").setValue("Offline");
        }
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_AppBarOverlay);

        setContentView(R.layout.activity_online_players);
        dbRef = new Firebase("https://fixed-7dac1.firebaseio.com/users/");
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        arrayAdapter = new ArrayAdapter<String>(OnlinePlayers.this, R.layout.listview_item, R.id.textView2, arr);
//        picarrayAdapter= new ArrayAdapter<String>(OnlinePlayers.this, R.layout.listview_item, R.id.imageView2, picArr);
        listv = (ListView) findViewById(R.id.listv);
        arrayList = new ArrayList<>();
        customListAdapter = new CustomListAdapter(getApplicationContext(), R.layout.listview_item, arrayList);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.

                customListAdapter.clear();
                arrayList.clear();
                refresh();            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser mUser = mAuth.getCurrentUser();

                if (mUser != null) {

                    getUsers();
                } else {
                    Toast.makeText(getApplicationContext(), "No user", Toast.LENGTH_SHORT).show();
                }

            }
        };



    }
    private void getUsers(){
        customListAdapter.clear();
        arrayList.clear();

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String photoUrl = "";
                int length = (int) dataSnapshot.getChildrenCount();
                int i = 0;
                int j = 0;
                final ArrayList<String> idString = new ArrayList<String>(length);

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (!ds.getKey().equals(mAuth.getCurrentUser().getUid())) {
                        if (ds.child("status").getValue().equals("Online")) {
                            String name = ds.child("name").getValue().toString();
                            if (ds.hasChild("lastname") == true) {
                                photoUrl = ds.child("photo").getValue().toString();

                                String lastname = ds.child("lastname").getValue().toString();
                                idString.add(i, ds.child("id").getValue().toString());
                                i++;
                                arrayList.add(new Players(ds.child("photo").getValue().toString(), name + " " + lastname));

                            }
                            if (ds.hasChild("lastname") == false) {
                                arr.add(name);
                                idString.add(i, ds.child("id").getValue().toString());
                                i++;
                                arrayList.add(new Players(ds.child("photo").getValue().toString(), name));

                            }

                        }

                    }

                }
                listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Toast.makeText(getApplication(), "id :" + idString.get(position), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(OnlinePlayers.this, usertest.class);
                        i.putExtra("userid", idString.get(position));
                        startActivity(i);
                    }
                });
//                           Picasso.with(getApplicationContext()).load(photoUrl).fit().into(img);
                customListAdapter.notifyDataSetChanged();
                listv.setTextFilterEnabled(true);
                listv.setAdapter(customListAdapter);
                listv.setTextFilterEnabled(true);

                if (arrayList.size() < 1) {
                    Toast.makeText(getApplicationContext(), "No Online Players", Toast.LENGTH_SHORT).show();


                }
//                            listv.setAdapter(picarrayAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    public void refresh() {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.



                // Remember to CLEAR OUT old items before appending in the new ones
              getUsers();
                swipeContainer.setRefreshing(false);

    }
}


