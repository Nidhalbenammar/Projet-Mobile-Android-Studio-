package com.example.tp7.Taches;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tp7.DatabaseHelper;
import com.example.tp7.R;

public class DetailsTache extends AppCompatActivity {
    private EditText editTextName, editTextHour, editTextMinute, editTextDay;
    private Button btnSave, btnDelete;
    private DatabaseHelper dbHelper;
    private int taskId;
    private TextView textViewTaskId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_tache);

        editTextName = findViewById(R.id.editTextName);
        editTextHour = findViewById(R.id.editTextHour);
        editTextMinute = findViewById(R.id.editTextMinute);
        editTextDay = findViewById(R.id.editTextDay);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
       // textViewTaskId = findViewById(R.id.textViewTaskId);
        dbHelper = new DatabaseHelper(this);


        Intent intent = getIntent();
        String name = intent.getStringExtra("task_name");
        taskId = intent.getIntExtra("task_id", -1);
        int hour = intent.getIntExtra("task_hour", 0);
        int minute = intent.getIntExtra("task_minute", 0);
        String day = intent.getStringExtra("task_day");

        taskId = getIntent().getIntExtra("task_id", -1);  // D -1


        editTextName.setText(name);
        editTextHour.setText(String.valueOf(hour));
        editTextMinute.setText(String.valueOf(minute));
        editTextDay.setText(day);
        //textViewTaskId.setText("Task ID: " + taskId);

        btnSave.setOnClickListener(v -> {
            String newName = editTextName.getText().toString();
            int newHour = Integer.parseInt(editTextHour.getText().toString());
            int newMinute = Integer.parseInt(editTextMinute.getText().toString());
            String newDay = editTextDay.getText().toString();

            Tache updatedTache = new Tache(newName, newHour, newDay, newMinute);
            dbHelper.updateTache(taskId, updatedTache);
            finish();
        });


        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Supprimer Tâche")
                    .setMessage("Êtes vous sûr de supprimer cette tâche?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        dbHelper.deleteTache(taskId);
                        finish();
                    })
                    .setNegativeButton("Non", null)
                    .show();
        });
    }
}
