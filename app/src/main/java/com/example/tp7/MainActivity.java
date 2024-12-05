package com.example.tp7;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.example.tp7.Auth.LoginActivity;
import com.example.tp7.Auth.PasswordUtils;
import com.example.tp7.Cours.Cours;
import com.example.tp7.Cours.CoursEtudFragment;
import com.example.tp7.Cours.CoursFragment;
import com.example.tp7.Taches.TacheFragment;
import com.example.tp7.Teachers.Teacher;
import com.example.tp7.Teachers.TeacherAdapter;
import com.example.tp7.Teachers.TeacherEtudFragment;
import com.example.tp7.Teachers.homeFragment;
import com.example.tp7.Users.ProfileFragment;
import com.example.tp7.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityMainBinding bind;
    boolean cours=false;
    boolean enseig = false;
    boolean about = false;
    boolean us=false;
    Menu menu;
    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private Toolbar toolbar;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        bind = ActivityMainBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        setContentView(view);
        setTitle("Mon Application");
        setSupportActionBar(bind.toolbar);
        bind.navView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, bind.drawerLayout, bind.toolbar, R.string.open_drawer, R.string.close_drawer);
        bind.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);

        String username = sharedPreferences.getString("username", null);

        View headerView = bind.navView.getHeaderView(0);
        String role = dbHelper.getUserRole(username);
        configureNavigation(role);
        TextView textViewUserEmail = headerView.findViewById(R.id.textView);
        if (username != null) {
            textViewUserEmail.setText("Welcome ! "+ username);
        }
        if (!sharedPreferences.getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AccueilFragment()).commit();
            bind.navView.setCheckedItem(R.id.nav_home);
        }
        setToolbarTitle("Accueil");
        /*DatabaseHelper dbHelper = new DatabaseHelper(this);


       /*dbHelper.addTeacher(new Teacher(1, "Ben ammar nidhal", "nidhal@gmail.com"));
        dbHelper.addTeacher(new Teacher(2, "Firas bahroun", "firas@gmail.com"));


        dbHelper.addCours(new Cours("Math", 3.5f, "cours", 1));
        dbHelper.addCours(new Cours("Mobile", 2.0f, "atelier", 2));
        dbHelper.addCours(new Cours("java", 2.0f, "atelier", 2));


        List<Teacher> teachers = dbHelper.getTeacherList();
        for (Teacher teacher : teachers) {
            Log.d("Teacher", "ID: " + teacher.getId() + ", Name: " + teacher.getName() + ", Email: " + teacher.getEmail());
        }

        List<Cours> courses = dbHelper.getCoursList();
        for (Cours cours : courses) {
            Log.d("Cours", "Name: " + cours.getNom() + ", Hours: " + cours.getNbrh() + ", Type: " + cours.getType()+"teacher id : "+cours.getIdTeacher());
        }*/

    }
    private void configureNavigation(String role) {
        Menu menu = bind.navView.getMenu();

        if ("etudiant".equals(role)) {
            menu.findItem(R.id.nav_home).setVisible(false);
            menu.findItem(R.id.nav_Users).setVisible(false);
            menu.findItem(R.id.nav_cours).setVisible(false);
        } else if ("admin".equals(role)) {
            menu.findItem(R.id.nav_coursE).setVisible(false);
            menu.findItem(R.id.nav_TeachersEtud).setVisible(false);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_home) {
            bind.toolbar.setTitle("Enseignants");
            enseig = true;
            about = false;
            cours = false;
            us=false;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new homeFragment()).commit();
        } else if (item.getItemId() == R.id.nav_accueil) {
            bind.toolbar.setTitle("Accueil");
            about = true;
            enseig = false;
            cours = false;
            us=false;
            //bind.navView.getMenu().findItem(R.id.nav_about).setVisible(false);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AccueilFragment()).commit();
        } else if (item.getItemId() == R.id.nav_tache) {
            bind.toolbar.setTitle("Taches");
            about = false;
            enseig = false;
            cours = false;
            us=false;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TacheFragment()).commit();
        } else if (item.getItemId() == R.id.nav_logout) {
            logout();

            Toast.makeText(this, "Bye Bye", Toast.LENGTH_LONG).show();
            finish();
        } else if (item.getItemId() == R.id.nav_cours) {
            about = false;
            enseig = false;
            cours = true;
            us=false;
            bind.toolbar.setTitle("Cours");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CoursFragment()).commit();
        }  else if (item.getItemId() == R.id.nav_profile) {
        about = false;
        enseig = false;
        cours = false;
        us=false;
        bind.toolbar.setTitle("Profile");
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
    }
        else if (item.getItemId() == R.id.nav_Users) {
        about = false;
        enseig = false;
        cours = false;
        us=true;
        bind.toolbar.setTitle("Liste des utilisateurs");
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ManageUsersFragment()).commit();
    }
        else if (item.getItemId() == R.id.nav_coursE) {
            about = false;
            enseig = false;
            cours = false;
            us=false;
            bind.toolbar.setTitle("Liste des cours");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CoursEtudFragment()).commit();
        }
        else if (item.getItemId() == R.id.nav_TeachersEtud) {
            about = false;
            enseig = false;
            cours = false;
            us=false;
            bind.toolbar.setTitle("Liste des enseignants");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TeacherEtudFragment()).commit();
        }

        bind.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    public void setToolbarTitle(String title) {
        if (toolbar != null) {
            toolbar.setTitle(title); // Set the title of the toolbar
        }
    }

    @Override
    public void onBackPressed() {
        if (bind.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            bind.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menus) {
        getMenuInflater().inflate(R.menu.menu_main_about_xml, menus);
        this.menu = menus;
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();

        if (enseig) {
            getMenuInflater().inflate(R.menu.menu_main_enseig_xml, menu);
        } else if (about) {
            getMenuInflater().inflate(R.menu.menu_main_about_xml, menu);
        } else if (cours) {
            getMenuInflater().inflate(R.menu.cours_menu, menu);
        } else if (us) {
            getMenuInflater().inflate(R.menu.add_user_m, menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.a_z) {
            homeFragment fragmentA = (homeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragmentA != null) {
                TeacherAdapter adapterA = fragmentA.getAdapter();
                if (adapterA != null) {
                    adapterA.sortByName();
                    return true;
                }
            }
        } else if (itemId == R.id.z_a) {
            homeFragment fragmentZ = (homeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragmentZ != null) {
                TeacherAdapter adapterZ = fragmentZ.getAdapter();
                if (adapterZ != null) {
                    adapterZ.reverseByName();
                    return true;
                }
            }
        } else if (itemId == R.id.add) {
            showAddingDialog();
            return true;
        } else if (itemId == R.id.action_add) {
            showAddingDialog2();
            return true;
        } else if (itemId == R.id.action_search) {
            showSearchDialog();
            return true;
        }  else if (itemId == R.id.addU) {
            showAddUserDialog();
            return true;
        }
        else if (itemId == R.id.action_list_all) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new CoursFragment())
                    .addToBackStack(null)
                    .commit();
            return true;
        } else if (itemId == R.id.action_clear_list) {
            showClearAllCoursesDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void showAddingDialog() {
        MyDialogFragment dialog = new MyDialogFragment();
        dialog.setDialogType(MyDialogFragment.TYPE_TEACHER);
        dialog.setTeacherListener(new MyDialogFragment.OnTeacherAddedListener() {
            @Override
            public void onTeacherAdded(String name, String email) {
                Teacher newTeacher = new Teacher(name, email);
                homeFragment fragment = (homeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                TeacherAdapter adapter = fragment != null ? fragment.getAdapter() : null;
                if (adapter != null) {
                    adapter.addTeacher(newTeacher);
                }
            }
        });
        dialog.show(getSupportFragmentManager(), "add_teacher_dialog");
    }

    private void showAddingDialog2() {
        MyDialogFragment dialog = new MyDialogFragment();
        dialog.setDialogType(MyDialogFragment.TYPE_COURS);
        dialog.setCoursListener(new MyDialogFragment.OnCoursAddedListener() {
            @Override
            public void onCoursAdded(String name, Float nbrh, String type, int enseig) {
                Cours newCours = new Cours(name, nbrh, type, enseig);
                dbHelper.addCours(newCours);
                Toast.makeText(MainActivity.this, "Cours ajouté avec succès", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show(getSupportFragmentManager(), "add_cours_dialog");
    }
    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_search_course, null);
        builder.setView(dialogView);

        EditText searchField = dialogView.findViewById(R.id.edit_search_name);
        Button searchButton = dialogView.findViewById(R.id.button_search);

        AlertDialog dialog = builder.create();

        searchButton.setOnClickListener(v -> {
            String courseName = searchField.getText().toString().trim();
            if (!courseName.isEmpty()) {
                List<Cours> result = dbHelper.searchCoursByName(courseName);
                if (!result.isEmpty()) {
                    Toast.makeText(this, "Cours trouvés : " + result.size(), Toast.LENGTH_SHORT).show();
                    CoursFragment fragment = (CoursFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                    if (fragment != null) {
                        fragment.updateCoursList(result);
                    }
                } else {
                    Toast.makeText(this, "Aucun cours trouvé", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Entrez un nom", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

        dialog.show();
    }
    public void showClearAllCoursesDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Voulez-vous vraiment supprimer tous les cours ?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    DatabaseHelper dbHelper = new DatabaseHelper(this);
                    dbHelper.deleteAllCours();
                    CoursFragment coursFragment = (CoursFragment) getSupportFragmentManager().findFragmentByTag(CoursFragment.class.getSimpleName());
                    if (coursFragment != null) {
                        coursFragment.clearAllCoursList();
                    }
                    Toast.makeText(this, "Tous les cours ont été supprimés.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Non", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }
    private void showAddUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_user, null);
        builder.setView(dialogView);

        EditText nameField = dialogView.findViewById(R.id.edit_name);
        EditText lastnameField = dialogView.findViewById(R.id.edit_lastname);
        EditText institutionField = dialogView.findViewById(R.id.edit_institution);
        EditText usernameField = dialogView.findViewById(R.id.edit_username);
        EditText passwordField = dialogView.findViewById(R.id.edit_password);
        Button addButton = dialogView.findViewById(R.id.button_add_user);

        AlertDialog dialog = builder.create();


        addButton.setOnClickListener(v -> {
            String name = nameField.getText().toString().trim();
            String lastname = lastnameField.getText().toString().trim();
            String institution = institutionField.getText().toString().trim();
            String username = usernameField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String hashedPassword = PasswordUtils.hashPassword(password);
            if (!name.isEmpty() && !lastname.isEmpty() && !institution.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
                if (hashedPassword != null) {
                String role = "etudiant";


                dbHelper.addUser(name, lastname, institution, username, hashedPassword, role);
                Toast.makeText(this, "Utilisateur ajouté avec succès!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
            }}
        });

        dialog.show();
    }

    public void logout() {
        Intent musicIntent = new Intent(MainActivity.this, MusicService.class);
        stopService(musicIntent);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }







}





