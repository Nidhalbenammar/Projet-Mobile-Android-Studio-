package com.example.tp7;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class UserViewHolder extends RecyclerView.ViewHolder {
    TextView textViewUserName, textViewInstitution;
    Button buttonDeleteUser;

    public UserViewHolder(View itemView) {
        super(itemView);
        textViewUserName = itemView.findViewById(R.id.textViewUserName);
        textViewInstitution = itemView.findViewById(R.id.textViewInstitution);
        buttonDeleteUser = itemView.findViewById(R.id.buttonDeleteUser);
    }
}
