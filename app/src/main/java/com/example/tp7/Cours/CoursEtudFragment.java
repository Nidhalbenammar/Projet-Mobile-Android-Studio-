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


public class CoursEtudFragment extends Fragment {

    private RecyclerView recyclerView;
    private CoursEtudAdapter coursEtudAdapter;
    private List<Cours> coursList;
    private DatabaseHelper dbHelper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cours_etud, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCours);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        List<Cours> coursList = dbHelper.getCoursList();
        coursEtudAdapter = new CoursEtudAdapter(coursList, getContext(),dbHelper);
        recyclerView.setAdapter(coursEtudAdapter);

        return view;

    }
    public CoursEtudAdapter getAdapter() {
        return coursEtudAdapter;
    }

    public void clearAllCoursList() {
        coursList.clear();
        coursEtudAdapter.notifyDataSetChanged();
    }





}
