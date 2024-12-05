package com.example.tp7;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.tp7.Cours.Cours;
import com.example.tp7.Taches.Tache;
import com.example.tp7.Teachers.Teacher;
import com.example.tp7.Users.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MyDataBase.db";
    private static final int DATABASE_VERSION = 15;

    private static final String TABLE_USER = "User";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_NAME = "nom";
    private static final String COLUMN_USER_LASTNAME = "prenom";
    private static final String COLUMN_USER_INSTITUTION = "etablissement";
    private static final String COLUMN_USER_USERNAME = "username";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String COLUMN_USER_ROLE = "role";


    private static final String TABLE_TEACHER = "Teacher";
    private static final String COLUMN_TEACHER_ID = "id";
    private static final String COLUMN_TEACHER_NAME = "name";
    private static final String COLUMN_TEACHER_EMAIL = "email";


    private static final String TABLE_COURS = "Cours";
    private static final String COLUMN_COURS_ID = "id";
    private static final String COLUMN_COURS_NAME = "nom";
    private static final String COLUMN_COURS_HOURS = "nbrh";
    private static final String COLUMN_COURS_TYPE = "type";
    private static final String COLUMN_COURS_TEACHER_ID = "idTeacher";

    private static final String TABLE_TACHE = "taches";
    private static final String COLUMN_TACHE_ID = "id";
    private static final String COLUMN_TACHE_NAME = "name";
    private static final String COLUMN_TACHE_HOUR = "hour";
    private static final String COLUMN_TACHE_MINUTE = "minute";
    private static final String COLUMN_TACHE_DAY = "day";
    private static final String COLUMN_TACHE_USER_ID = "user_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTeacherTable = "CREATE TABLE " + TABLE_TEACHER + " (" +
                COLUMN_TEACHER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TEACHER_NAME + " TEXT, " +
                COLUMN_TEACHER_EMAIL + " TEXT)";
        db.execSQL(createTeacherTable);

        String createCoursTable = "CREATE TABLE " + TABLE_COURS + " (" +
                COLUMN_COURS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_COURS_NAME + " TEXT, " +
                COLUMN_COURS_HOURS + " REAL, " +
                COLUMN_COURS_TYPE + " TEXT, " +
                COLUMN_COURS_TEACHER_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_COURS_TEACHER_ID + ") REFERENCES " + TABLE_TEACHER + "(" + COLUMN_TEACHER_ID + "))";
        db.execSQL(createCoursTable);

        String createUserTable = "CREATE TABLE " + TABLE_USER + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_NAME + " TEXT, " +
                COLUMN_USER_LASTNAME + " TEXT, " +
                COLUMN_USER_INSTITUTION + " TEXT, " +
                COLUMN_USER_USERNAME + " TEXT UNIQUE, " +
                COLUMN_USER_PASSWORD + " TEXT, "
                + COLUMN_USER_ROLE + " TEXT)";
        db.execSQL(createUserTable);

       String CREATE_TABLE_TACHE = "CREATE TABLE " + TABLE_TACHE + "("
                + COLUMN_TACHE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TACHE_NAME + " TEXT,"
                + COLUMN_TACHE_HOUR + " INTEGER,"
                + COLUMN_TACHE_MINUTE + " INTEGER,"
                + COLUMN_TACHE_DAY + " TEXT,"
                + COLUMN_TACHE_USER_ID + " INTEGER,"
                + "FOREIGN KEY (" + COLUMN_TACHE_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + "))";
        db.execSQL(CREATE_TABLE_TACHE);
        addAdminUser(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEACHER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TACHE);
        onCreate(db);
    }
    public void addTache(Tache tache, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TACHE_NAME, tache.getNom());
        values.put(COLUMN_TACHE_HOUR, tache.getH());
        values.put(COLUMN_TACHE_MINUTE, tache.getM());
        values.put(COLUMN_TACHE_DAY, tache.getJour());
        values.put(COLUMN_TACHE_USER_ID, userId);

        db.insert(TABLE_TACHE, null, values);
        db.close();
    }

    public List<Tache> getTachesByUserId(int userId) {
        List<Tache> taches = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TACHE,
                null,
                COLUMN_TACHE_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id=cursor.getInt(cursor.getColumnIndex(COLUMN_TACHE_ID));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_TACHE_NAME));
                int hour = cursor.getInt(cursor.getColumnIndex(COLUMN_TACHE_HOUR));
                int minute = cursor.getInt(cursor.getColumnIndex(COLUMN_TACHE_MINUTE));
                String day = cursor.getString(cursor.getColumnIndex(COLUMN_TACHE_DAY));
                taches.add(new Tache(id,name, hour, minute,day));
            }
            cursor.close();
        }
        db.close();
        return taches;
    }
    public void updateTache(int taskId, Tache tache) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TACHE_NAME, tache.getNom());
        values.put(COLUMN_TACHE_HOUR, tache.getH());
        values.put(COLUMN_TACHE_MINUTE, tache.getM());
        values.put(COLUMN_TACHE_DAY, tache.getJour());

        db.update(TABLE_TACHE, values, COLUMN_TACHE_ID + "=?", new String[]{String.valueOf(taskId)});
        db.close();
    }

    public void deleteTache(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TACHE, COLUMN_TACHE_ID + "=?", new String[]{String.valueOf(taskId)});
        db.close();
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                User user = new User(
                        cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_USER_LASTNAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_USER_INSTITUTION)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_USER_USERNAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD))
                );
                userList.add(user);
            }
            cursor.close();
        }
        return userList;
    }


    public void addUser(String nom, String prenom, String etablissement, String username, String password,String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, nom);
        values.put(COLUMN_USER_LASTNAME, prenom);
        values.put(COLUMN_USER_INSTITUTION, etablissement);
        values.put(COLUMN_USER_USERNAME, username);
        values.put(COLUMN_USER_PASSWORD, password);
        values.put(COLUMN_USER_ROLE, role);
        db.insert(TABLE_USER, null, values);
        db.close();
    }
    public boolean deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_USER, COLUMN_USER_ID + "=?", new String[]{String.valueOf(userId)});
        return rowsDeleted > 0;
    }

    private void addAdminUser(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, "Admin");
        values.put(COLUMN_USER_LASTNAME, "Admin");
        values.put(COLUMN_USER_INSTITUTION, "N/A");
        values.put(COLUMN_USER_USERNAME, "admin");
        values.put(COLUMN_USER_PASSWORD, "9af15b336e6a9619928537df30b2e6a2376569fcf9d7e773eccede65606529a0");
        values.put(COLUMN_USER_ROLE, "admin");

        long result = db.insert(TABLE_USER, null, values);
        if (result != -1) {
            Log.d("DatabaseHelper", "Admin user added successfully.");
        } else {
            Log.e("DatabaseHelper", "Failed to add admin user.");
        }
    }
    public boolean validateLogin(String username, String hashedPassword) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, null, COLUMN_USER_USERNAME + "=? AND " + COLUMN_USER_PASSWORD + "=?",
                new String[]{username, hashedPassword}, null, null, null);
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }

    public String getUserRole(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, new String[]{COLUMN_USER_ROLE},
                COLUMN_USER_USERNAME + "=?", new String[]{username}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String role = cursor.getString(0);
            cursor.close();
            db.close();
            return role;
        }
        return null;
    }
    public int getUserIdByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER,
                new String[]{COLUMN_USER_ID},
                COLUMN_USER_USERNAME + "=?",
                new String[]{username},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
            cursor.close();
            return userId;
        }
        return -1; // Return -1 if user not found
    }
    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, null, COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_USER_LASTNAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_USER_INSTITUTION)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_USER_USERNAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD))
            );
            cursor.close();
            return user;
        }
        return null;
    }
    public User getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("User", null, "username=?", new String[]{username}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("nom")),
                    cursor.getString(cursor.getColumnIndex("prenom")),
                    cursor.getString(cursor.getColumnIndex("etablissement")),
                    cursor.getString(cursor.getColumnIndex("username")),
                    cursor.getString(cursor.getColumnIndex("password"))
            );
            cursor.close();
            return user;
        }
        return null;
    }

    public boolean updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nom", user.getName());
        values.put("prenom", user.getLastname());
        values.put("etablissement", user.getInstitution());
        values.put("password",user.getPassword());

        int rowsUpdated = db.update("User", values, "id=?", new String[]{String.valueOf(user.getId())});
        return rowsUpdated > 0;
    }




    public void addTeacher(Teacher teacher) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEACHER_NAME, teacher.getName());
        values.put(COLUMN_TEACHER_EMAIL, teacher.getEmail());
        db.insert(TABLE_TEACHER, null, values);
        db.close();
    }

    public List<Teacher> getTeacherList() {
        List<Teacher> teacherList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TEACHER, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Teacher teacher = new Teacher(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TEACHER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEACHER_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEACHER_EMAIL))
                );
                teacherList.add(teacher);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return teacherList;
    }

    public void deleteTeacher(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TEACHER, COLUMN_TEACHER_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void addCours(Cours cours) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COURS_NAME, cours.getNom());
        values.put(COLUMN_COURS_HOURS, cours.getNbrh());
        values.put(COLUMN_COURS_TYPE, cours.getType());
        values.put(COLUMN_COURS_TEACHER_ID, cours.getIdTeacher());
        db.insert(TABLE_COURS, null, values);
        db.close();
    }


    public List<Cours> getCoursList() {
        List<Cours> coursList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COURS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Cours cours = new Cours(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COURS_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURS_NAME)),
                        cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_COURS_HOURS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURS_TYPE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COURS_TEACHER_ID))
                );
                coursList.add(cours);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return coursList;
    }
    public void updateCours(Cours cours) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COURS_NAME, cours.getNom());
        values.put(COLUMN_COURS_HOURS, cours.getNbrh());
        values.put(COLUMN_COURS_TYPE, cours.getType());
        values.put(COLUMN_COURS_TEACHER_ID, cours.getIdTeacher());

        // Update the database record
        db.update(TABLE_COURS, values, COLUMN_COURS_ID + " = ?", new String[]{String.valueOf(cours.getId())});
        db.close();
    }

    public void deleteCours(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COURS, COLUMN_COURS_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
    public String getTeacherNameById(int teacherId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM teacher WHERE id = ?", new String[]{String.valueOf(teacherId)});

        String teacherName = null;
        if (cursor.moveToFirst()) {
            teacherName = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return teacherName;
    }
    public int getTeacherIdByName(String teacherName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM Teacher WHERE name = ?", new String[]{teacherName});

        int teacherId = -1;
        if (cursor.moveToFirst()) {
            teacherId = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return teacherId;
    }
    public List<String> getAllTeacherNames() {
        List<String> teacherNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT name FROM Teacher", null);

        if (cursor.moveToFirst()) {
            do {
                teacherNames.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return teacherNames;
    }

    public List<Cours> searchCoursByName(String name) {
        List<Cours> coursList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_COURS,
                null,
                COLUMN_COURS_NAME + " LIKE ?",
                new String[]{"%" + name + "%"},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                Cours cours = new Cours(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COURS_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURS_NAME)),
                        cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_COURS_HOURS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURS_TYPE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COURS_TEACHER_ID))
                );
                coursList.add(cours);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return coursList;
    }
    public void deleteAllCours() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COURS, null, null);
        db.close();
    }



}
