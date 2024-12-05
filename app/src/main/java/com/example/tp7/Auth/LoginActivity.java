package com.example.tp7.Auth;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.tp7.DatabaseHelper;
import com.example.tp7.MainActivity;
import com.example.tp7.MusicService;
import com.example.tp7.R;
import com.example.tp7.Auth.PasswordUtils;

public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText usernameField, passwordField;
    private Button loginButton;
    private TextView registerText;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        dbHelper = new DatabaseHelper(this);
        editor = sharedPreferences.edit();

        usernameField = findViewById(R.id.editTextUsername);
        passwordField = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        registerText = findViewById(R.id.textViewRegister);

        loginButton.setOnClickListener(v -> {
            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();


            String hashedPassword = PasswordUtils.hashPassword(password);
            if (hashedPassword != null && dbHelper.validateLogin(username, hashedPassword)) {
                editor.putString("username", username);
                editor.putBoolean("isLoggedIn", true);
                editor.apply();
                Intent musicIntent = new Intent(LoginActivity.this, MusicService.class);
                startService(musicIntent);

                showLoginNotification();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Utilisateur inexistant ou informations incomplètes !", Toast.LENGTH_SHORT).show();
            }
        });

        registerText.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });
    }

    private void showLoginNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String channelId = "login_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId, "Login Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(android.R.drawable.ic_menu_info_details)
                .setContentTitle("Connexion réussie")
                .setContentText("Bienvenue, la musique est en train de jouer !")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        notificationManager.notify(3, builder.build());
    }
}
