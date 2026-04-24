package com.example.to_do_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TodoAppImproved.db";
    private static final int DATABASE_VERSION = 4;

    // Users table
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    // Tasks table
    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_TASK_ID = "id";
    public static final String COLUMN_TASK_USER_ID = "user_id";
    public static final String COLUMN_TASK_TITLE = "title";
    public static final String COLUMN_TASK_DESCRIPTION = "description";
    public static final String COLUMN_TASK_DATE = "task_date";
    public static final String COLUMN_TASK_TIME = "task_time";
    public static final String COLUMN_TASK_PRIORITY = "priority";
    public static final String COLUMN_TASK_CATEGORY = "category";
    public static final String COLUMN_TASK_TIMESTAMP = "timestamp";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TASK_USER_ID + " INTEGER,"
                + COLUMN_TASK_TITLE + " TEXT,"
                + COLUMN_TASK_DESCRIPTION + " TEXT,"
                + COLUMN_TASK_DATE + " TEXT,"
                + COLUMN_TASK_TIME + " TEXT,"
                + COLUMN_TASK_PRIORITY + " TEXT,"
                + COLUMN_TASK_CATEGORY + " TEXT,"
                + COLUMN_TASK_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY(" + COLUMN_TASK_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")" + ")";
        db.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE " + TABLE_TASKS + " ADD COLUMN " + COLUMN_TASK_PRIORITY + " TEXT DEFAULT 'Low'");
            db.execSQL("ALTER TABLE " + TABLE_TASKS + " ADD COLUMN " + COLUMN_TASK_CATEGORY + " TEXT DEFAULT 'Personal'");
        }
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE " + TABLE_TASKS + " ADD COLUMN " + COLUMN_TASK_TIME + " TEXT");
        }
    }

    // User operations
    public long addUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        return db.insert(TABLE_USERS, null, values);
    }

    public int checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID};
        String selection = COLUMN_EMAIL + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
        }
        cursor.close();
        return userId;
    }

    // Task operations
    public long addTask(int userId, String title, String description, String date, String time, String priority, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_USER_ID, userId);
        values.put(COLUMN_TASK_TITLE, title);
        values.put(COLUMN_TASK_DESCRIPTION, description);
        values.put(COLUMN_TASK_DATE, date);
        values.put(COLUMN_TASK_TIME, time);
        values.put(COLUMN_TASK_PRIORITY, priority);
        values.put(COLUMN_TASK_CATEGORY, category);
        return db.insert(TABLE_TASKS, null, values);
    }

    public Cursor getTasks(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_TASKS, null, COLUMN_TASK_USER_ID + " = ?", new String[]{String.valueOf(userId)}, null, null, COLUMN_TASK_DATE + " ASC");
    }

    public Cursor searchTasks(int userId, String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_TASK_USER_ID + " = ? AND (" + COLUMN_TASK_TITLE + " LIKE ? OR " + COLUMN_TASK_DESCRIPTION + " LIKE ?)";
        String[] selectionArgs = {String.valueOf(userId), "%" + query + "%", "%" + query + "%"};
        return db.query(TABLE_TASKS, null, selection, selectionArgs, null, null, COLUMN_TASK_DATE + " ASC");
    }

    public int updateTask(int taskId, String title, String description, String date, String time, String priority, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_TITLE, title);
        values.put(COLUMN_TASK_DESCRIPTION, description);
        values.put(COLUMN_TASK_DATE, date);
        values.put(COLUMN_TASK_TIME, time);
        values.put(COLUMN_TASK_PRIORITY, priority);
        values.put(COLUMN_TASK_CATEGORY, category);
        return db.update(TABLE_TASKS, values, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
    }

    public void deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
    }
}
