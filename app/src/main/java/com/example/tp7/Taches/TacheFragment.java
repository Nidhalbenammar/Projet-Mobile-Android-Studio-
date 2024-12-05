package com.example.tp7.Taches;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.tp7.DatabaseHelper;
import com.example.tp7.databinding.FragmentTacheBinding;

import java.util.ArrayList;
import java.util.List;

public class TacheFragment extends Fragment {

    private FragmentTacheBinding binding;
    private ArrayList<Tache> taches;
    private TacheAdapter adapter;
    private static final String CHANNEL_ID = "task_channel_id";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTacheBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);
        int userId = dbHelper.getUserIdByUsername(username);

        List<String> JoursDeLaSemaines = List.of("Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, JoursDeLaSemaines);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(dataAdapter);

        taches = new ArrayList<>(dbHelper.getTachesByUserId(userId));
        adapter = new TacheAdapter(taches);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(position -> {
            Tache selectedTache = taches.get(position);
            Intent intent = new Intent(requireContext(), DetailsTache.class);
            intent.putExtra("task_id", selectedTache.getId());
            intent.putExtra("task_name", selectedTache.getNom());
            intent.putExtra("task_hour", selectedTache.getH());
            intent.putExtra("task_minute", selectedTache.getM());
            intent.putExtra("task_day", selectedTache.getJour());
            startActivity(intent);
        });


        binding.button.setOnClickListener(v -> {
            int selectedPosition = binding.spinner.getSelectedItemPosition();
            String tacheText = binding.textInputEditText.getText().toString().trim();
            String selectedDay = binding.spinner.getItemAtPosition(selectedPosition).toString();
            int hour = binding.timePicker.getHour();
            int minute = binding.timePicker.getMinute();

            if (tacheText.isEmpty()) {
                Toast.makeText(requireContext(), "Veuillez entrer un nom pour la tâche", Toast.LENGTH_SHORT).show();
                return;
            }

            Tache newTask = new Tache(tacheText, hour, selectedDay, minute);
            taches.add(newTask);
            adapter.notifyDataSetChanged();
            dbHelper.addTache(newTask, userId);
            showNotification("Tâche ajoutée", "La tâche '" + tacheText + "' a été ajoutée avec succès !");
        });


        requestNotificationPermission();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager == null) {
            Toast.makeText(requireContext(), "Notification Manager non disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Task Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Notifications pour la gestion des tâches");
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_menu_add)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        notificationManager.notify(1, builder.build());
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (requireContext().checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }
    }

}
