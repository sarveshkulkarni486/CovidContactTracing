package com.spam.maddy.covidcontacttracing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spam.maddy.covidcontacttracing.Common.Common;
import com.spam.maddy.covidcontacttracing.Model.User;

import java.util.ArrayList;
import java.util.Calendar;

public class RegisterationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static final String TAG = "GuestRegisterationActivity";

    private int mYear, mMonth, mDay;
    TextView dobEdt,already_user;
    EditText nameText,emailText,mobileText,passwordText,reEnterPasswordText;
    Button signupButton;
    TextView agreeGuestTerms;
    CheckBox terms_conditions;
    FirebaseDatabase database;
    DatabaseReference table_user,areaList;
    String affiliateVerified="NotVerified";
    Spinner spinner;
    ValueEventListener listener;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerDataList;

    String spinnerString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        database=FirebaseDatabase.getInstance();
        table_user=database.getReference("User").child("Guest");

        spinner=findViewById(R.id.spinner);
        areaList=database.getReference("Area").child("Location");
        spinnerDataList=new ArrayList<>();
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,spinnerDataList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(RegisterationActivity.this);
        retrieveData();
        nameText=findViewById(R.id.fullName);
        //    gendertextBtn=findViewById(R.id.genderRadioBtn);
        emailText=findViewById(R.id.userEmailId);
        mobileText=findViewById(R.id.mobileNumber);
        passwordText=findViewById(R.id.password);
        reEnterPasswordText=findViewById(R.id.confirmPassword);
        signupButton=(Button) findViewById(R.id.submitBtn1);
        agreeGuestTerms=findViewById(R.id.agreeGuestTerms);
        already_user=findViewById(R.id.already_user);
        terms_conditions=(CheckBox) findViewById(R.id.terms_conditions);
        agreeGuestTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(RegisterationActivity.this)
                        .setTitle("License agreement")
                        .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                terms_conditions.setChecked(true);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                terms_conditions.setChecked(false);
                            }
                        })
                        .setMessage(R.string.affagreement)
                        .show();


            }
        });
        already_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(RegisterationActivity.this,LoginActivity.class);
                finish();
                startActivity(in);
            }
        });
        dobEdt=findViewById(R.id.birthday);
        dobEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                dobEdt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

    }

    public void retrieveData(){
        listener=areaList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot item:dataSnapshot.getChildren()){
                    spinnerDataList.add(item.getValue().toString());
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void signup() {


        if (!validate()) {
            onSignupFailed();
            return;
        }
        signupButton.setEnabled(false);


        String name = nameText.getText().toString();
        // String gender = genderText.getText().toString();
        String email = emailText.getText().toString();
        String mobile = mobileText.getText().toString();
        String password = passwordText.getText().toString();
        String reEnterPassword = reEnterPasswordText.getText().toString();
        onSignupSuccess();
    }

    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        setResult(RESULT_OK, null);

        if (Common.isConnectedToInternet(getBaseContext())) {
            final ProgressDialog mDialog = new ProgressDialog(RegisterationActivity.this);
            mDialog.setMessage("Please Waiting.....");
            mDialog.show();

            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.child(mobileText.getText().toString()).exists()) {
                        mDialog.dismiss();
                        String name = nameText.getText().toString();
                        // String gender = genderText.getText().toString();
                        String email = emailText.getText().toString();
                        String dob = dobEdt.getText().toString();
                        String pass = passwordText.getText().toString();

                        User user = new User(name, pass,dob, email,spinnerString,"Negative","-");
                        table_user.child(mobileText.getText().toString()).setValue(user);
                        Toast.makeText(RegisterationActivity.this, "Sign Up Successfully", Toast.LENGTH_SHORT).show();

                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                Intent login_intent=new Intent(RegisterationActivity.this,LoginActivity.class);
                                startActivity(login_intent);
                                finish();
                            }
                        }, 700);

                        //Toast.makeText(signup.this, "Phone Number alredy register", Toast.LENGTH_SHORT).show();
                    } else {
                        if(dataSnapshot.child(mobileText.getText().toString()).exists()){
                            mDialog.dismiss();
                            Toast.makeText(RegisterationActivity.this, "You have Sign Up Successfully,Please Login for continue your shopping", Toast.LENGTH_SHORT).show();

                            //  Toast.makeText(signup.this, "Phone Number already register", Toast.LENGTH_SHORT).show();
                            /*Intent login_intent=new Intent(GuestRegisterationActivity.this,MainActivity.class);
                            startActivity(login_intent);*/
                        }
                                    /*Toast.makeText(signup.this, "" +
                                            "r", Toast.LENGTH_SHORT).show();
*/

                                   /* String name = edtName.getText().toString();
                                    String pass = password.getText().toString();
                                    String secureCode = edtSecureCode.getText().toString();
                                    User user = new User(name, pass, secureCode);
                                    table_user.child(edtPhone.getText().toString()).setValue(user);
                                    Toast.makeText(signup.this, "Sign Up Successfully", Toast.LENGTH_SHORT).show();*/
                        //finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(RegisterationActivity.this, "please check your connection !!", Toast.LENGTH_SHORT).show();
            return;
        }
        //finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "SingUp failed", Toast.LENGTH_LONG).show();


    }

    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String dob = dobEdt.getText().toString();
        String email = emailText.getText().toString();
        String mobile = mobileText.getText().toString();
        String password = passwordText.getText().toString();
        String reEnterPassword = reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameText.setError("at least 3 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }
        if (dob.isEmpty() ) {
            dobEdt.setError("Select your correct DOB");
            valid = false;
        } else {
            dobEdt.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);

        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            mobileText.setError(null);

        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            reEnterPasswordText.setError(null);

        }
        if (terms_conditions.isChecked()==false){
            agreeGuestTerms.setError("Please Accept Term's and Conditions");
            valid = false;
        }else {
            agreeGuestTerms.setError(null);
        }
        return valid;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        spinnerString=spinner.getSelectedItem().toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}