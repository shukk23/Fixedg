package com.example.shukri.fixed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "Tag";
    //Add YOUR Firebase Reference URL instead of the following URL
    private Firebase mRef = new Firebase("https://fixed-7dac1.firebaseio.com/");
    private User user;
    private EditText username;
    private EditText firstname;
    private EditText lastname;
    private EditText phoneNumber;
    private EditText email;
    private EditText password;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        username = (EditText) findViewById(R.id.edit_text_username);
        firstname= (EditText) findViewById(R.id.firstname);
        lastname= (EditText) findViewById(R.id.lastname);
        email = (EditText) findViewById(R.id.edit_text_new_email);
        password = (EditText) findViewById(R.id.edit_text_new_password);

    }

    @Override
    public void onStop() {
        super.onStop();
    }
    public void onSignUpClicked(View view) {
        createNewAccount(email.getText().toString(), password.getText().toString());
        showProgressDialog();
    }
    protected void setUpUser() {

        user = new User();
        user.setUsername(username.getText().toString());
        user.setName(firstname.getText().toString());
        user.setLastname(lastname.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
    }

    private void createNewAccount(String email, String password) {
        Log.d(TAG, "createNewAccount:" + email);
        if (!validateForm()) {
            return;
        }
        //This method sets up a new User by fetching the user entered details.
        setUpUser();
        //This method  method  takes in an email address and password, validates them and then creates a new user
        // with the createUserWithEmailAndPassword method.
        // If the new account was created, the user is also signed in, and the AuthStateListener runs the onAuthStateChanged callback.
        // In the callback, you can use the getCurrentUser method to get the user's account data.

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Autentifikimi deshtoi,provoni perseri.",
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            onAuthenticationSucess(task.getResult().getUser());
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(firstname.getText().toString()+" "+lastname.getText().toString()).build();
                            task.getResult().getUser().updateProfile(profileUpdates);
                            String uid = task.getResult().getUser().getUid();
                            Log.d("uid",""+task.getResult().getUser().getUid());
                            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("user_id",uid);
//                            editor.putString("profile_picture",image);
                            editor.apply();
                            setDefaultPicture(uid);

                        }


                    }
                });

    }


    private void onAuthenticationSucess(FirebaseUser mUser) {
        // Write new user
        saveNewUser(mUser.getUid(), user.getName(),user.getLastname(),user.getUsername(), user.getEmail());
//        signOut();
        // Go to LoginActivity
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        finish();
    }

    private void saveNewUser(String userId, String name,String lastname,String username, String email) {
        User user = new User(userId,name,lastname,username,email,null);

        mRef.child("users").child(userId).setValue(user);
    }

    public void setDefaultPicture(String id){
        Firebase dbRef;


        dbRef = new Firebase("https://fixed-7dac1.firebaseio.com/users/");
        dbRef.child(id).child("photo").setValue("https://firebasestorage.googleapis.com/v0/b/fixed-7dac1.appspot.com/o/photo%2Fdefault.jpg?alt=media&token=82294c2a-61c8-499b-82c7-39f9774f015c");
    }

    private void signOut() {
        mAuth.signOut();
    }
    private boolean validateForm() {
        boolean valid = true;

        String userEmail = email.getText().toString();
        if (TextUtils.isEmpty(userEmail)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }

        String userPassword = password.getText().toString();
        if (TextUtils.isEmpty(userPassword)) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }


    public void showProgressDialog() {
        if (validateForm() == true) {
        if (mProgressDialog == null) {

                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setIndeterminate(true);
            }
            mProgressDialog.show();
        }

    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}