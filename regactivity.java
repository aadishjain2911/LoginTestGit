package com.example.logintest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class regactivity extends AppCompatActivity {

    EditText name , email , crpass , copass , age ;
    TextView textview ;
    Button reg ;

    FirebaseAuth mfirebaseauth ;

    DatabaseReference databaseUsers ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regactivity);

        databaseUsers = FirebaseDatabase.getInstance().getReference("Users") ;

        mfirebaseauth = FirebaseAuth.getInstance() ;
        Intent temp = getIntent() ;

        name = (EditText) findViewById(R.id.name) ;
        email = (EditText) findViewById(R.id.email) ;
        age = (EditText) findViewById(R.id.age) ;
        crpass = (EditText) findViewById(R.id.crpass) ;
        copass = (EditText) findViewById(R.id.copass) ;
        textview = (TextView) findViewById(R.id.textview) ;
        reg = (Button) findViewById(R.id.register) ;
    }

    public void register(View v) {
        final String nm = name.getText().toString() ;
        final String em = email.getText().toString();
        final int ag = (int) Double.parseDouble(age.getText().toString());
        String crpassword = crpass.getText().toString();
        String copassword = copass.getText().toString();

        if (nm.isEmpty()) {
            name.setError("Please provide name.");
            name.requestFocus() ;
        }

        if (em.isEmpty()) {
            email.setError("Please provide email.");
            email.requestFocus() ;
        }

        if (age.getText().toString().isEmpty()) {
            age.setError("Please provide age.");
            age.requestFocus() ;
        }

        if (crpassword.isEmpty() || copassword.isEmpty()) {
            crpass.setError("Please set a password.");
            crpass.requestFocus() ;
        }

        if (!crpassword.equals(copassword)) {
            textview.setText("Create Password and Confirm Password should be same.");
        }

        else {
            mfirebaseauth.createUserWithEmailAndPassword(em,crpassword).addOnCompleteListener(regactivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    String id = databaseUsers.push().getKey() ;

                    Userclass usr = new Userclass(id,nm,em,ag) ;

                    databaseUsers.child(id).setValue(usr) ;

                    textview.setText("Registered Successfully.");

                }

                else textview.setText("User could not be registered.");

                }
            });
        }
    }
}
