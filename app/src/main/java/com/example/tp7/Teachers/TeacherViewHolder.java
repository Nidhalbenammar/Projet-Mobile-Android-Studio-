package com.example.tp7.Teachers;

import android.app.Activity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tp7.DatabaseHelper;
import com.example.tp7.R;

public class TeacherViewHolder extends RecyclerView.ViewHolder {
    public TextView nom;
    public TextView email;
    public TeacherViewHolder(View itemView) {
        super(itemView);
        nom=itemView.findViewById(R.id.teacherName);
        email=itemView.findViewById(R.id.teacherEmail);
        itemView.setOnCreateContextMenuListener(this::onCreateContextMenu);

    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        ((Activity) v.getContext()).getMenuInflater().inflate(R.menu.context_menu, menu);

        MenuItem deleteItem = menu.findItem(R.id.action_delete);
        deleteItem.setOnMenuItemClickListener(item -> {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                TeacherAdapter adapter = (TeacherAdapter) ((RecyclerView) v.getParent()).getAdapter();
                Teacher teacher = adapter.getTeachers().get(position);
                DatabaseHelper dbHelper = adapter.getDbHelper();
                if (adapter != null) {
                    dbHelper.deleteTeacher(teacher.getId());

                    adapter.removeItem(position);
                }
                return true;
            }
            return false;
        });
    }
}
