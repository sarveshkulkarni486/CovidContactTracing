package com.spam.maddy.covidcontacttracing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.spam.maddy.covidcontacttracing.Common.Common;
import com.spam.maddy.covidcontacttracing.Interface.ItemClickListener;
import com.spam.maddy.covidcontacttracing.Model.Vaccine;
import com.spam.maddy.covidcontacttracing.ViewHolder.VaccineViewHolder;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class AddVaccineCenterActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FloatingActionButton fab;

    FirebaseDatabase db;
    DatabaseReference foodList;
    FirebaseStorage storage;
    StorageReference storageReference;

    String categoryId="";

    FirebaseRecyclerAdapter<Vaccine, VaccineViewHolder> adapter;

    EditText edtVaccineCenterName,edtContactNo,edtAddress,edtVaccineName,edtVaccineQty,edtVaccinePrice,edtUpdateVaccineQty;
    Button btnSelect,btnUpload;

    Vaccine newFood;

    Uri saveUri;
    RelativeLayout rootLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vaccine_center);
//        foodList=db.getReference("Data").child("Vaccine").child("VaccineCenter");

        db=FirebaseDatabase.getInstance();
        foodList=db.getReference("Data").child("Vaccine").child("VaccineCenter");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        recyclerView=(RecyclerView)findViewById(R.id.recycler_vaccine);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        rootLayout=(RelativeLayout)findViewById(R.id.rootLayout);


        if(getIntent()!=null)
            categoryId= "mm";
        if(!categoryId.isEmpty())
            loadListFood();

        BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.vaccinenv);
        bottomNavigationView.setSelectedItemId(R.id.action_vaccineCenter);
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
                      //  finish();
                        return true;

                    case R.id.action_vaccineCenter :

                        return true;
                    case R.id.action_profile :
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                       // finish();
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
    protected void onResume() {
        super.onResume();
        adapter.startListening();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }

    private void loadListFood() {

        //Query listFoodByCategoryId = foodList;
        Query listFoodByCategoryId = foodList;//.orderByChild("menuId").equalTo(categoryId);

        FirebaseRecyclerOptions<Vaccine> options = new FirebaseRecyclerOptions.Builder<Vaccine>()
                .setQuery(listFoodByCategoryId, Vaccine.class)
                .build();

        adapter= new FirebaseRecyclerAdapter<Vaccine, VaccineViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull VaccineViewHolder viewHolder, int position, @NonNull Vaccine model) {
                viewHolder.vaccine_name.setText(model.getVaccineCenterName());
                Picasso.with(AddVaccineCenterActivity.this).load(model.getVaccineCenterImageUrl()).into(viewHolder.vaccine_center_image);
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent orderDetail=new Intent(AddVaccineCenterActivity.this,VaccineCenterDetails.class);
                        Common.currentVaccine=model;
                        orderDetail.putExtra("message_key",adapter.getRef(position).getKey());
                        startActivity(orderDetail);
                    }
                });
            }

            @NonNull
            @Override
            public VaccineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.vaccine_item,parent,false);


                return new VaccineViewHolder(itemView);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }













}