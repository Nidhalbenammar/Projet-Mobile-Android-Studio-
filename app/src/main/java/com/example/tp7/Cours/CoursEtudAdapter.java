package com.example.tp7.Cours;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tp7.DatabaseHelper;
import com.example.tp7.R;

import java.util.List;

public class CoursEtudAdapter extends RecyclerView.Adapter<CoursEtudAdapter.CoursEtudViewHolder> {

    private List<Cours> coursList;
    private Context context;
    private DatabaseHelper dbHelper;

    public CoursEtudAdapter(List<Cours> coursList, Context context, DatabaseHelper dbHelper) {
        this.coursList = coursList;
        this.context = context;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public CoursEtudViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_coursetud, parent, false);
        return new CoursEtudViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoursEtudViewHolder holder, int position) {
        Cours cours = coursList.get(position);


        holder.typeCours.setText(cours.getType());
        holder.nomCours.setText(cours.getNom());
        holder.nbHeures.setText(String.valueOf(cours.getNbrh()));


        List<String> teacherNames = dbHelper.getAllTeacherNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, teacherNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.nomEnseignant.setAdapter(adapter);


        int teacherPosition = teacherNames.indexOf(dbHelper.getTeacherNameById(cours.getIdTeacher()));

        if (teacherPosition != -1) {
            holder.nomEnseignant.setSelection(teacherPosition);
        } else {
            holder.nomEnseignant.setSelection(0);
        }


    }

    @Override
    public int getItemCount() {
        return coursList.size();
    }

    static class CoursEtudViewHolder extends RecyclerView.ViewHolder {
        TextView typeCours, nomCours;
        EditText nbHeures;
        Spinner nomEnseignant;

        public CoursEtudViewHolder(@NonNull View itemView) {
            super(itemView);
            typeCours = itemView.findViewById(R.id.typeCours);
            nomCours = itemView.findViewById(R.id.nomCours);
            nbHeures = itemView.findViewById(R.id.nbHeures);
            nomEnseignant = itemView.findViewById(R.id.nomEnseignant);
            nomEnseignant.setEnabled(false);

        }
    }

    public void updateList(List<Cours> newList) {
        this.coursList.clear();
        this.coursList.addAll(newList);
        notifyDataSetChanged();
    }

}
