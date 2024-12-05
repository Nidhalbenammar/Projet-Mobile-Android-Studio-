package com.example.tp7.Teachers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tp7.R;


public class TeacherDetailsFragment extends Fragment {

    private static final String ARG_TEACHER = "teacher";
    private Teacher teacher;

    public static TeacherDetailsFragment newInstance(Teacher teacher) {
        TeacherDetailsFragment fragment = new TeacherDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TEACHER, teacher);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_details, container, false);
        EditText editTextName = view.findViewById(R.id.editTextName);
        EditText editTextEmail = view.findViewById(R.id.editTextEmail);
        EditText editTextSubject = view.findViewById(R.id.editTextSubject);
        EditText editTextMessage = view.findViewById(R.id.editTextMessage);
        Button buttonSendEmail = view.findViewById(R.id.buttonSendEmail);

        if (getArguments() != null) {
            teacher = (Teacher) getArguments().getSerializable(ARG_TEACHER);
            editTextEmail.setText(teacher.getEmail());
            editTextName.setText(teacher.getName());
        }

        buttonSendEmail.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString();
            String subject = editTextSubject.getText().toString();
            String message = editTextMessage.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(subject) || TextUtils.isEmpty(message)) {
                Toast.makeText(requireContext(), "Tous les champs sont obligatoires!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);

            try {
                startActivity(Intent.createChooser(emailIntent, "Send Email"));
            } catch (Exception e) {
                Toast.makeText(requireContext(), "No email app found!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
