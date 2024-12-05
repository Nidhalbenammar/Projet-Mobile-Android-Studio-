package com.example.tp7.Cours;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tp7.DatabaseHelper;
import com.example.tp7.R;

import java.util.List;

public class CoursAdapter extends RecyclerView.Adapter<CoursAdapter.CoursViewHolder> {

    private List<Cours> coursList;
    private Context context;
    private DatabaseHelper dbHelper;

    public CoursAdapter(List<Cours> coursList, Context context, DatabaseHelper dbHelper) {
        this.coursList = coursList;
        this.context = context;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public CoursViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cours, parent, false);
        return new CoursViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoursViewHolder holder, int position) {
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




        holder.btnEdit.setTag(false);


        holder.btnEdit.setOnClickListener(v -> {
            boolean isEditing = (boolean) holder.btnEdit.getTag();

            if (!isEditing) {

                holder.btnEdit.setTag(true);
                holder.btnEdit.setText("SAVE");
            } else {

                String updatedNbHeures = holder.nbHeures.getText().toString().trim();
                String updatedNomCours = holder.nomCours.getText().toString().trim();
                String updatedTeacherName = (String) holder.nomEnseignant.getSelectedItem();

                if (!updatedNbHeures.isEmpty() && !updatedNomCours.isEmpty() && updatedTeacherName != null) {

                    cours.setNbrh(Float.parseFloat(updatedNbHeures));
                    cours.setNom(updatedNomCours);
                    cours.setIdTeacher(dbHelper.getTeacherIdByName(updatedTeacherName));


                    dbHelper.updateCours(cours);


                    holder.btnEdit.setTag(false);
                    holder.btnEdit.setText("EDIT");


                    Toast.makeText(context, "Cours updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Erreur", Toast.LENGTH_SHORT).show();
                }
            }
        });



        holder.btnDelete.setOnClickListener(v -> {

            dbHelper.deleteCours(cours.getId());
            Log.d("Cours ID", String.valueOf(cours.getId()));

            coursList.remove(position);

            notifyItemRemoved(position);
            notifyItemRangeChanged(position, coursList.size());
            Toast.makeText(context, "Cours supprimé avec succès", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return coursList.size();
    }

    static class CoursViewHolder extends RecyclerView.ViewHolder {
        TextView typeCours, nomCours;
        EditText nbHeures;
        Spinner nomEnseignant;
        Button btnEdit, btnDelete;

        public CoursViewHolder(@NonNull View itemView) {
            super(itemView);
            typeCours = itemView.findViewById(R.id.typeCours);
            nomCours = itemView.findViewById(R.id.nomCours);
            nbHeures = itemView.findViewById(R.id.nbHeures);
            nomEnseignant = itemView.findViewById(R.id.nomEnseignant);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
    public void Cours(Cours cour) {
        dbHelper.addCours(cour);
     
}
    public void updateList(List<Cours> newList) {
        this.coursList.clear();
        this.coursList.addAll(newList);
        notifyDataSetChanged();
    }

}
