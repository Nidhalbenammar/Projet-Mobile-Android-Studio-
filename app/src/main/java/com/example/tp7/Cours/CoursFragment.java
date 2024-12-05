package com.example.tp7.Cours;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tp7.DatabaseHelper;
import com.example.tp7.R;

import java.util.List;


public class CoursFragment extends Fragment {

    private RecyclerView recyclerView;
    private CoursAdapter coursAdapter;
    private List<Cours> coursList;
    private DatabaseHelper dbHelper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cours, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCours);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        /*
        coursList = new ArrayList<>();
        coursList.add(new Cours("Cours", 1.5F, "Développement Mobile",1));
        coursList.add(new Cours("Atelier",  3.0F, "Programmation Web",2));
        */
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        List<Cours> coursList = dbHelper.getCoursList();
        coursAdapter = new CoursAdapter(coursList, getContext(),dbHelper);
        recyclerView.setAdapter(coursAdapter);

        return view;

    }
    public CoursAdapter getAdapter() {
        return coursAdapter;
    }
    public void updateCoursList(List<Cours> newList) {
        if (coursAdapter != null) {
            coursAdapter.updateList(newList);
            coursAdapter.notifyDataSetChanged();
        }
    }
    public void clearAllCoursList() {
        coursList.clear();
        coursAdapter.notifyDataSetChanged();
    }





}
