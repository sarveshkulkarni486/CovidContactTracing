package com.spam.maddy.covidcontacttracing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.spam.maddy.covidcontacttracing.Common.Common;
import com.squareup.picasso.Picasso;

public class VaccineCenterDetails extends AppCompatActivity {

    TextView vaccineCenterName,vaccineCenterMNO,vaccineName,vaccineCenterAddress,vaccineQty,vaccinePrice;
    ImageView vaccineCenterImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine_center_details);

        Intent intent = getIntent();
        String str = intent.getStringExtra("message_key");

        vaccineCenterName=findViewById(R.id.vaccineDetailCenterName);
        vaccineCenterMNO=findViewById(R.id.vaccineDetailMno);
        vaccineName=findViewById(R.id.vaccineDetailName);
        vaccineCenterAddress=findViewById(R.id.vaccineDetailAddress);
        vaccineQty=findViewById(R.id.vaccineDetailQty);
        vaccinePrice=findViewById(R.id.vaccineDetailPrice);

        vaccineCenterImage=findViewById(R.id.vaccineCenterImage);

        Picasso.with(VaccineCenterDetails.this).load(Common.currentVaccine.getVaccineCenterImageUrl()).into(vaccineCenterImage);

        vaccineCenterName.setText(Common.currentVaccine.getVaccineCenterName());
        vaccineCenterMNO.setText(Common.currentVaccine.getContactNo());
        vaccineName.setText(Common.currentVaccine.getVaccineName());
        vaccineCenterAddress.setText(Common.currentVaccine.getAddress());
        vaccineQty.setText(Common.currentVaccine.getVaccineQty());
        vaccinePrice.setText(Common.currentVaccine.getVaccinePrice());


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //  startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }
}