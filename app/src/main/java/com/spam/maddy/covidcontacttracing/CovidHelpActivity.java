package com.spam.maddy.covidcontacttracing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CovidHelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_help);


        BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.helpnv);
        bottomNavigationView.setSelectedItemId(R.id.action_precautions);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.action_home :
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        finish();
                        return true;

                    case R.id.action_precautions :
                        //startActivity(new Intent(getApplicationContext(),CovidHelpActivity.class));
                        // finish();
                        return true;

                    case R.id.action_vaccineCenter :
                      //  startActivity(new Intent(getApplicationContext(),AddVaccineCenterActivity.class));

                        return true;
                    case R.id.action_profile :
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        //finish();
                        return true;
                    case R.id.action_logout :
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        finish();
                        return true;
                }
                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }
}