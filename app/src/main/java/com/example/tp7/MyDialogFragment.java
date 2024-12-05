package com.example.tp7;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.tp7.Cours.Cours;
import com.example.tp7.Cours.CoursFragment;

import java.util.List;


public class MyDialogFragment extends DialogFragment {
    public static final int TYPE_TEACHER = 1;
    public static final int TYPE_COURS = 2;
    private DatabaseHelper dbHelper;
    private int dialogType;
    private OnTeacherAddedListener teacherListener;
    private OnCoursAddedListener coursListener;

    public void setDialogType(int type) {
        this.dialogType = type;
    }

    public void setTeacherListener(OnTeacherAddedListener listener) {
        this.teacherListener = listener;
    }

    public void setCoursListener(OnCoursAddedListener listener) {
        this.coursListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView;
        dbHelper = new DatabaseHelper(requireContext());
        if (dialogType == TYPE_TEACHER) {
            dialogView = getLayoutInflater().inflate(R.layout.activity_show_adding_dialog, null, false);

            final EditText nameField = dialogView.findViewById(R.id.edit_text);
            final EditText emailField = dialogView.findViewById(R.id.email);

            builder.setTitle("Ajouter Nouveau Enseignant")
                    .setMessage("Donner nom et email de l'enseignant")
                    .setPositiveButton("Valider", (dialog, which) -> {
                        String name = nameField.getText().toString();
                        String email = emailField.getText().toString();
                        if (!name.isEmpty() && !email.isEmpty() && teacherListener != null) {
                            teacherListener.onTeacherAdded(name, email);
                        }
                    });
        } else if (dialogType == TYPE_COURS) {
            dialogView = getLayoutInflater().inflate(R.layout.show_adding_cours, null, false);

            final EditText nameField = dialogView.findViewById(R.id.edit_textN);
            final EditText hoursField = dialogView.findViewById(R.id.nbrh);
            final Spinner teacherSpinner = dialogView.findViewById(R.id.teacher_spinner);
            List<String> teacherNames = dbHelper.getAllTeacherNames();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                    android.R.layout.simple_spinner_item, teacherNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            teacherSpinner.setAdapter(adapter);
            builder.setTitle("Ajouter Nouveau Cours")
                    .setMessage("Donner nom, nombre d'heures, type et enseignant")
                    .setPositiveButton("Valider", (dialog, which) -> {
                        String name = nameField.getText().toString();
                        String hoursText = hoursField.getText().toString();
                        String teacherName = teacherSpinner.getSelectedItem().toString();
                        int teacherId = dbHelper.getTeacherIdByName(teacherName);
                        RadioGroup radioGroup = dialogView.findViewById(R.id.radio_group_type);
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        RadioButton selectedRadioButton = dialogView.findViewById(selectedId);
                        String type = selectedRadioButton.getText().toString();

                        if (!name.isEmpty() && !hoursText.isEmpty() && teacherId != -1 && coursListener != null) {
                            float hours = Float.parseFloat(hoursText);
                            coursListener.onCoursAdded(name, hours, type, teacherId);
                            if (getParentFragment() instanceof CoursFragment) {
                                CoursFragment coursFragment = (CoursFragment) getParentFragment();
                                List<Cours> updatedCoursList = dbHelper.getCoursList();
                                coursFragment.updateCoursList(updatedCoursList);
                            }
                        }

                    });

        } else {
            throw new IllegalArgumentException("Invalid dialog type");
        }

        builder.setView(dialogView)
                .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());

        return builder.create();
    }

    public interface OnTeacherAddedListener {
        void onTeacherAdded(String name, String email);
    }

    public interface OnCoursAddedListener {
        void onCoursAdded(String name, Float nbrh, String type, int enseig);
    }
}
