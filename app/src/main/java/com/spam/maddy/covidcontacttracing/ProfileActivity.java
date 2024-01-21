package com.spam.maddy.covidcontacttracing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.spam.maddy.covidcontacttracing.Common.Common;

public class ProfileActivity extends AppCompatActivity {

    TextView userName,userMno,userEmail,userAddress,userDOB,userPassword,userCovidTest,userCovidTestDate,userCovidResult;
    Switch covidSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.profilenv);
        bottomNavigationView.setSelectedItemId(R.id.action_profile);
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
                        startActivity(new Intent(getApplicationContext(),CovidHelpActivity.class));
                       // finish();
                        return true;

                    case R.id.action_vaccineCenter :
                        startActivity(new Intent(getApplicationContext(),AddVaccineCenterActivity.class));
                      //  finish();
                        return true;
                    case R.id.action_profile :
                       /* startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        finish();*/
                        return true;
                    case R.id.action_logout :
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        finish();
                        return true;
                }
                return true;
            }
        });

        userName=findViewById(R.id.userName);
        userMno=findViewById(R.id.userMno);
        userEmail=findViewById(R.id.userEmail);
        userAddress=findViewById(R.id.userAddress);
        userDOB=findViewById(R.id.userdob);
        userPassword=findViewById(R.id.userPassword);
        userCovidTest=findViewById(R.id.userCovidTest);
        userCovidTestDate=findViewById(R.id.userCovidTestDate);
        userCovidResult=findViewById(R.id.covidResult);

        covidSwitch=findViewById(R.id.covidSwitch);

        userName.setText(Common.currentUser.getName());
        userMno.setText(Common.currentUser.getPhone());
        userEmail.setText(Common.currentUser.getEmail());
        userAddress.setText(Common.currentUser.getHomeAddress());
        userDOB.setText(Common.currentUser.getDob());
        userPassword.setText(Common.currentUser.getPassword());
        userCovidTest.setText(Common.currentUser.getCovidTest());
        userCovidTestDate.setText(Common.currentUser.getCovidTestDate());
        userCovidResult.setText(Common.currentUser.getCovidTest());

        String testResult=userCovidResult.getText().toString();
        if(testResult.equals("Positive")){
            covidSwitch.setChecked(true);
            userCovidResult.setTextColor(Color.RED);
            userCovidTest.setTextColor(Color.RED);
            userCovidTestDate.setTextColor(Color.RED);
            userName.setTextColor(Color.RED);
            // CovidTestingDate= SimpleDateFormat.getDateInstance().format(new Date());
        }else {
            covidSwitch.setChecked(false);
            userCovidResult.setTextColor(Color.GREEN);
            userCovidTest.setTextColor(Color.GREEN);
            userCovidTestDate.setTextColor(Color.GREEN);
            userName.setTextColor(Color.GREEN);

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
      //  startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }
}