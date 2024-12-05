package com.example.tp7.Users;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.Manifest;

import com.example.tp7.Auth.PasswordUtils;
import com.example.tp7.DatabaseHelper;
import com.example.tp7.R;

public class ProfileFragment extends Fragment {

    private EditText editTextName, editTextLastname, editTextInstitution, editTextUsername, editTextPassword, editTextCpassword,editTextPhoneNumber,editTextSMS;
    private Button buttonUpdateProfile,buttonCallPhone,buttonSendSMS;
    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;

    private boolean isEditing = false;
    private User currentUser;
    private static final int REQUEST_GALLERY_PERMISSION = 101;
    private static final int REQUEST_GALLERY_IMAGE = 102;
    private static final int REQUEST_CALL_PERMISSION = 103;
    private static final int REQUEST_SMS_PERMISSION = 102;
    private ImageView profileImage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        editTextName = view.findViewById(R.id.editTextName);
        editTextLastname = view.findViewById(R.id.editTextLastname);
        editTextInstitution = view.findViewById(R.id.editTextInstitution);
        editTextUsername = view.findViewById(R.id.editTextUsername);
        editTextPassword=view.findViewById(R.id.editTextpassword);
        editTextCpassword = view.findViewById(R.id.editTextCpassword);
        editTextPhoneNumber = view.findViewById(R.id.editTextPhoneNumber);  //
        buttonUpdateProfile = view.findViewById(R.id.buttonUpdateProfile);
        buttonCallPhone = view.findViewById(R.id.buttonCallPhone);
        buttonSendSMS = view.findViewById(R.id.buttonSendSMS);
        editTextSMS=view.findViewById(R.id.editTextSMS);
        dbHelper = new DatabaseHelper(requireContext());
        sharedPreferences = requireContext().getSharedPreferences("UserSession", requireContext().MODE_PRIVATE);


        loadUserData();


        buttonUpdateProfile.setOnClickListener(v -> {
            if (!isEditing) {
                enableEditing(true);
            } else {
                updateProfile();
            }
        });
        buttonCallPhone.setOnClickListener(v -> {
            String phoneNumber = editTextPhoneNumber.getText().toString().trim();
            if (phoneNumber.isEmpty()) {
                Toast.makeText(requireContext(), "Entrez un numéro svp!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                startActivity(callIntent);
            }
        });
        buttonSendSMS.setOnClickListener(v -> {
            String sms = editTextSMS.getText().toString().trim();
            String number = editTextPhoneNumber.getText().toString().trim();

            if (number.isEmpty()) {
                Toast.makeText(requireContext(), "Entrez un numéro svp !", Toast.LENGTH_SHORT).show();
                return;
            }

            if (sms.isEmpty()) {
                Toast.makeText(requireContext(), "Entrez un message svp!", Toast.LENGTH_SHORT).show();
                return;
            }


            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS_PERMISSION);
            } else {
                sendSMS(number, sms);
            }
        });

        profileImage = view.findViewById(R.id.ProfileImage);

        profileImage.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_GALLERY_PERMISSION);
            } else {
                openGallery();
            }
        });
        return view;
    }
    private void sendSMS(String number, String message) {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:" + number));
            intent.putExtra("sms_body", message);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Failed to send SMS!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUserData() {
        String username = sharedPreferences.getString("username", null);
        if (username != null) {
            currentUser = dbHelper.getUserByUsername(username);
            if (currentUser != null) {
                editTextName.setText(currentUser.getName());
                editTextLastname.setText(currentUser.getLastname());
                editTextInstitution.setText(currentUser.getInstitution());
                editTextUsername.setText(currentUser.getUsername());
                editTextPassword.setText(currentUser.getPassword());
                editTextCpassword.setText(currentUser.getPassword());
            }
        }
    }

    private void enableEditing(boolean enable) {
        isEditing = enable;
        editTextName.setEnabled(enable);
        editTextLastname.setEnabled(enable);
        editTextInstitution.setEnabled(enable);
        editTextPassword.setEnabled(enable);
        editTextCpassword.setEnabled(enable);
        editTextUsername.setEnabled(false);
        buttonUpdateProfile.setText(enable ? "Enregistrer" : "Update Profile");
    }

    private void updateProfile() {
        String name = editTextName.getText().toString();
        String lastname = editTextLastname.getText().toString();
        String institution = editTextInstitution.getText().toString();
        String password=editTextPassword.getText().toString();
        String confirmPassword = editTextCpassword.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(lastname) || TextUtils.isEmpty(institution)) {
            Toast.makeText(requireContext(), "Tous les champs sont obligatoire !", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(requireContext(), "Mot de passe et sa confiration doivent être conforme !", Toast.LENGTH_SHORT).show();
            return;
        }
        String hashedPassword = PasswordUtils.hashPassword(password);
        if (currentUser != null) {
            currentUser.setName(name);
            currentUser.setLastname(lastname);
            currentUser.setInstitution(institution);
            currentUser.setPassword(hashedPassword);

            boolean success = dbHelper.updateUser(currentUser);
            if (success) {
                Toast.makeText(requireContext(), "Profile modifié avec succés!", Toast.LENGTH_SHORT).show();
                enableEditing(false);
            } else {
                Toast.makeText(requireContext(), "erreur!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_GALLERY_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_GALLERY_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(requireContext(), "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY_IMAGE && resultCode == requireActivity().RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), selectedImageUri);

                profileImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Failed to load image!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
