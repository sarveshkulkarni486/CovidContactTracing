package com.spam.maddy.covidcontacttracing.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spam.maddy.covidcontacttracing.Interface.ItemClickListener;
import com.spam.maddy.covidcontacttracing.R;


public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    public TextView txtUserId,txtUserPhone,txtUserAddress,txtUserName,txtUserDOB,txtUserCovidTest,txtUserCovidTestDate;
    private ItemClickListener itemClickListener;
    public UserViewHolder(@NonNull View itemView) {
        super(itemView);

        txtUserAddress=(TextView)itemView.findViewById(R.id.user_address);
        txtUserId=(TextView)itemView.findViewById(R.id.user_id);
        txtUserPhone=(TextView)itemView.findViewById(R.id.user_phone);
        txtUserName=(TextView)itemView.findViewById(R.id.user_name);
        txtUserDOB=(TextView)itemView.findViewById(R.id.user_dob);
        txtUserCovidTest=(TextView)itemView.findViewById(R.id.user_covidTest);
        txtUserCovidTestDate=(TextView)itemView.findViewById(R.id.user_covidTestDate);

        itemView.setOnClickListener(this);
    }

   public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view,getAdapterPosition(),false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

    }
}
