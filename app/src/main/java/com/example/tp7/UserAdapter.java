package com.example.tp7;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tp7.Users.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private Context context;
    private List<User> userList;
    private DatabaseHelper databaseHelper;

    public UserAdapter(Context context, List<User> userList, DatabaseHelper databaseHelper) {
        this.context = context;
        this.userList = userList;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.textViewUserName.setText(user.getName() + " " + user.getLastname());
        holder.textViewInstitution.setText(user.getInstitution());

        holder.buttonDeleteUser.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Confirmation")
                    .setMessage("Vous êtes sûr de supprimer cet utilisateur?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        int userId = user.getId();
                        boolean isDeleted = databaseHelper.deleteUser(userId);
                        if (isDeleted) {
                            userList.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(context, "Utilisateur supprimé(e)", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Erreur", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Non", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
