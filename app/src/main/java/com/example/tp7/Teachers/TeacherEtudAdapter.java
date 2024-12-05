package com.example.tp7.Teachers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tp7.R;

import java.util.List;

public class TeacherEtudAdapter extends RecyclerView.Adapter<TeacherEtudAdapter.TeacherEtudViewHolder> {

    private List<Teacher> teacherList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Teacher teacher);
    }

    public TeacherEtudAdapter(List<Teacher> teacherList, OnItemClickListener listener) {
        this.teacherList = teacherList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TeacherEtudViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teacher_etud, parent, false);
        return new TeacherEtudViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherEtudViewHolder holder, int position) {
        Teacher teacher = teacherList.get(position);
        holder.textViewTeacherName.setText(teacher.getName());
        holder.textViewTeacherEmail.setText(teacher.getEmail());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(teacher));
    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    public static class TeacherEtudViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTeacherName, textViewTeacherEmail;

        public TeacherEtudViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTeacherName = itemView.findViewById(R.id.textViewTeacherName);
            textViewTeacherEmail = itemView.findViewById(R.id.textViewTeacherEmail);
        }
    }
}
