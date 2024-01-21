package com.spam.maddy.covidcontacttracing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.spam.maddy.covidcontacttracing.Common.Common;
import com.spam.maddy.covidcontacttracing.Interface.ItemClickListener;
import com.spam.maddy.covidcontacttracing.Model.User;
import com.spam.maddy.covidcontacttracing.ViewHolder.UserViewHolder;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FloatingActionButton fab;

    FirebaseDatabase db;
    DatabaseReference foodList,areaList;
    FirebaseStorage storage;
    StorageReference storageReference;

    String categoryId="";

    FirebaseRecyclerAdapter<User, UserViewHolder> adapter;


    User newFood;

    Uri saveUri;
    LinearLayout rootLayout;
    Spinner homeSpinner;
    ImageView searchView;
    ValueEventListener listener;
    ArrayAdapter<String> spinnerAdapter;
    ArrayList<String> spinnerDataList;

    String spinnerString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.homenv);
        bottomNavigationView.setSelectedItemId(R.id.action_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.action_home :
                        return true;

                    case R.id.action_precautions :
                        startActivity(new Intent(getApplicationContext(),CovidHelpActivity.class));

                        return true;

                    case R.id.action_vaccineCenter :
                      //  startActivity(new Intent(getApplicationContext(),AddVaccineCenterActivity.class));

                        return true;
                    case R.id.action_profile :
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));

                        return true;
                    case R.id.action_logout :
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        finish();
                        return true;
                }
                return true;
            }
        });

        db=FirebaseDatabase.getInstance();
        foodList=db.getReference("User").child("Guest");

        homeSpinner=findViewById(R.id.homeSpinner);
        searchView=findViewById(R.id.search);
        areaList=db.getReference("Area").child("Location");
        spinnerDataList=new ArrayList<>();
        spinnerAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,spinnerDataList);
        homeSpinner.setAdapter(spinnerAdapter);
        homeSpinner.setOnItemSelectedListener(HomeActivity.this);
        retrieveData();
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // loadListFood(spinnerString);
                Toast.makeText(HomeActivity.this, ""+spinnerString,Toast.LENGTH_LONG).show();
            }
        });

        recyclerView=(RecyclerView)findViewById(R.id.recycler_user);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        rootLayout=(LinearLayout)findViewById(R.id.rootLayout);

     /*   if(getIntent()!=null)
            categoryId= "mm";
        if(!categoryId.isEmpty())
            loadListFood(Common.currentUser.getHomeAddress());*/

    }
    public void retrieveData(){
        listener=areaList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot item:dataSnapshot.getChildren()){
                    spinnerDataList.add(item.getValue().toString());
                }

                spinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        spinnerString=homeSpinner.getSelectedItem().toString();

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onResume() {
        super.onResume();
//        adapter.startListening();
    }

    private void loadListFood(String key) {

        //Query listFoodByCategoryId = foodList;
        Query listFoodByCategoryId = foodList.orderByChild("homeAddress").equalTo(key);

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(listFoodByCategoryId,User.class)
                .build();

        adapter= new FirebaseRecyclerAdapter<User, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder viewHolder, int position, @NonNull User model) {
                //    viewHolder.txtUserId.setText(adapter.getRef(position).getKey());
                viewHolder.txtUserName.setText(model.getName());
                viewHolder.txtUserAddress.setText(model.getHomeAddress());
                viewHolder.txtUserDOB.setText(model.getDob());
                viewHolder.txtUserPhone.setText(adapter.getRef(position).getKey());
                viewHolder.txtUserCovidTest.setText(model.getCovidTest());
                viewHolder.txtUserCovidTestDate.setText(model.getCovidTestDate());
                String covidResult=model.getCovidTest();

                if (covidResult.equals("Negative")){
                    viewHolder.txtUserName.setTextColor(Color.GREEN);
                    viewHolder.txtUserCovidTest.setTextColor(Color.GREEN);
                    viewHolder.txtUserCovidTestDate.setTextColor(Color.GREEN);
                }else{
                    viewHolder.txtUserName.setTextColor(Color.RED);
                    viewHolder.txtUserCovidTest.setTextColor(Color.RED);
                    viewHolder.txtUserCovidTestDate.setTextColor(Color.RED);

                }
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                     Toast.makeText(HomeActivity.this,"Covid Result"+ covidResult,Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_item,parent,false);


                return new UserViewHolder(itemView);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        //adapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }
}