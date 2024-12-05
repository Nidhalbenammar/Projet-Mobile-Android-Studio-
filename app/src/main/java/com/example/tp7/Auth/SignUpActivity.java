package com.example.tp7.Auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tp7.DatabaseHelper;
import com.example.tp7.R;
import com.example.tp7.Auth.PasswordUtils;

public class SignUpActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText nomField, prenomField, etablissementField, usernameField, passwordField, ConfirmpasswordField;
    private Button signUpButton;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        dbHelper = new DatabaseHelper(this);

        nomField = findViewById(R.id.editTextNom);
        prenomField = findViewById(R.id.editTextPrenom);
        etablissementField = findViewById(R.id.editTextEtablissement);
        usernameField = findViewById(R.id.editTextUsername);
        passwordField = findViewById(R.id.editTextPassword);
        ConfirmpasswordField = findViewById(R.id.editTextConPassword);
        signUpButton = findViewById(R.id.buttonSignUp);

        signUpButton.setOnClickListener(v -> {
            String nom = nomField.getText().toString();
            String prenom = prenomField.getText().toString();
            String etablissement = etablissementField.getText().toString();
            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();
            String cpassword = ConfirmpasswordField.getText().toString();
            String role = "etudiant";

            if (!nom.isEmpty() && !prenom.isEmpty() && !etablissement.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
                if (!password.equals(cpassword)) {
                    Toast.makeText(SignUpActivity.this, "Mot de passe et sa confirmation doivent être conforme!", Toast.LENGTH_SHORT).show();
                    return;
                }


                String hashedPassword = PasswordUtils.hashPassword(password);
                if (hashedPassword != null) {
                    dbHelper.addUser(nom, prenom, etablissement, username, hashedPassword, role);
                    Toast.makeText(this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                    editor.clear();
                    editor.apply();
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, "erreur", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SignUpActivity.this, "Tous les champs sont obligatoires !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
