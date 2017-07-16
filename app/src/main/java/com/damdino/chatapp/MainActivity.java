package com.damdino.chatapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int DEFAULT_MSJ_LENGHT_LIMIT = 500;
    private static int SIGN_IN_REQUET_CODE = 1;
    ArrayList<ChatModel> list;
    private ListView listView;
    private EditText editText;
    private FloatingActionButton button;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private CustomAdapter customAdapter;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ChildEventListener childEventListener;
    private ProgressDialog progressDialog;

    @Override
    protected void onResume() {
        super.onResume();
        auth.addAuthStateListener(authStateListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
        clearAdapterAndListener();
        customAdapter.clear();

    }

    private void clearAdapterAndListener() {

        // adapter temizliyoruz ve child event dinleyicisini kaldır zaten adam logout
        customAdapter.clear();
        if (childEventListener != null) {
            databaseReference.removeEventListener(childEventListener);
            childEventListener = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView = (ListView) findViewById(R.id.listview);
        editText = (EditText) findViewById(R.id.editText);
        button = (FloatingActionButton) findViewById(R.id.button);

        auth = FirebaseAuth.getInstance();    //auth için
        database = FirebaseDatabase.getInstance();   // VT

        databaseReference = database.getReference().child("mesaj");

        //listView Adapter ilişkisi
        list = new ArrayList<>();
        customAdapter = new CustomAdapter(getApplicationContext(), list);
        listView.setAdapter(customAdapter);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().length() > 0) {

                    sendChatMessage();

                } else {

                    Toast.makeText(getApplicationContext(), "Lütfen mesaj giriniz!", Toast.LENGTH_LONG).show();
                }

            }

            private void sendChatMessage() {


                FirebaseUser user = auth.getCurrentUser();
                String userEmailAddress = user.getEmail();
                long msTime = System.currentTimeMillis();
                Date curDateTime = new Date(msTime);
                SimpleDateFormat formatter = new SimpleDateFormat("dd'/'MM'/'y hh:mm");
                String dateTime = formatter.format(curDateTime);
                ChatModel chatMessages = new ChatModel(editText.getText().toString(), userEmailAddress, dateTime);
                databaseReference.push().setValue(chatMessages);
                editText.setText("");


            }
        });


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    getSupportActionBar().setTitle("" + user.getEmail());
                    readChatMessages();
                    Toast.makeText(getApplicationContext(), "Merhaba Chat Uygulamamıza Hoşgeldiniz!", Toast.LENGTH_SHORT).show();
                } else {
                    // User is signed out

                    clearAdapterAndListener();
                    startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                            .setProviders(AuthUI.EMAIL_PROVIDER, AuthUI.GOOGLE_PROVIDER)
                            .build(), SIGN_IN_REQUET_CODE);
                }
            }


            private void readChatMessages() {

                // child ları dinlemek icin kullaniyoruz listview in her rowu bir cocuk :)
                if (childEventListener == null) {
                    childEventListener = new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            ChatModel chatMessages = dataSnapshot.getValue(ChatModel.class);
                            customAdapter.add(chatMessages);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {


                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    };

                    databaseReference.addChildEventListener(childEventListener);
                }


            }
        };

    }
}