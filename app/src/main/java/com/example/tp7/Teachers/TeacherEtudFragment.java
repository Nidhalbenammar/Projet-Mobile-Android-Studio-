package com.example.tp7.Teachers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tp7.DatabaseHelper;
import com.example.tp7.R;

import java.util.List;

public class TeacherEtudFragment extends Fragment {

    private RecyclerView recyclerViewTeachers;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_etud, container, false);
        recyclerViewTeachers = view.findViewById(R.id.recyclerViewTeachers);
        dbHelper = new DatabaseHelper(requireContext());


        List<Teacher> teacherList = dbHelper.getTeacherList();
        TeacherEtudAdapter adapter = new TeacherEtudAdapter(teacherList, teacher -> {
            //click
            TeacherDetailsFragment detailsFragment = TeacherDetailsFragment.newInstance(teacher);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, detailsFragment)
                    .addToBackStack(null)
                    .commit();
        });

        recyclerViewTeachers.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewTeachers.setAdapter(adapter);

        return view;
    }
}
