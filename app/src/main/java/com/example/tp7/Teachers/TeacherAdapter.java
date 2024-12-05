package com.example.tp7.Teachers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tp7.DatabaseHelper;
import com.example.tp7.R;

import java.util.Comparator;
import java.util.List;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherViewHolder> {
    public List<Teacher> teachers;
    private DatabaseHelper dbHelper;

    public TeacherAdapter(List<Teacher> teachers, DatabaseHelper dbHelper) {
        this.teachers = teachers;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teacher,parent,false);
        return new TeacherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewHolder holder, int position) {
            Teacher teacher=teachers.get(position);
            holder.nom.setText(teacher.getName());
            holder.email.setText(teacher.getEmail());
    }

    @Override
    public int getItemCount() {
        return teachers.size();
    }
    public void sortByName() {
        teachers.sort(Comparator.comparing(Teacher::getName));
        notifyDataSetChanged();
    }

    public void reverseByName() {
        teachers.sort((t1, t2) -> t2.getName().compareTo(t1.getName()));
        notifyDataSetChanged();
    }
    public void addTeacher(Teacher teacher) {
        dbHelper.addTeacher(teacher);
        teachers.add(teacher);
        notifyDataSetChanged();
    }
    public void removeItem(int position) {
        teachers.remove(position);
        notifyItemRemoved(position);
    }
    public List<Teacher> getTeachers() {
        return teachers;
    }
    public DatabaseHelper getDbHelper() {
        return dbHelper;
    }
}
