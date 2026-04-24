package com.example.to_do_app;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskClickListener {

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private DatabaseHelper db;
    private int userId;
    private List<Task> allTasks;
    private List<Task> filteredTasks;
    private LinearLayout layoutEmpty;
    private TabLayout tabLayout;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy", Locale.getDefault());
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("d/M/yyyy HH:mm", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = pref.getInt("userId", -1);

        if (userId == -1) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        tabLayout = findViewById(R.id.tabLayout);
        
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        allTasks = new ArrayList<>();
        filteredTasks = new ArrayList<>();
        adapter = new TaskAdapter(filteredTasks, this);
        recyclerView.setAdapter(adapter);

        ExtendedFloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> showTaskDialog(null));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                applyFilter(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        checkNotificationPermission();
        loadTasks();
    }

    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
    }

    private void applyFilter(int tabPosition) {
        filteredTasks.clear();
        Date today = getTodayDate();

        for (Task task : allTasks) {
            try {
                Date taskDate = dateFormat.parse(task.getTaskDate());
                if (tabPosition == 0) { // All
                    filteredTasks.add(task);
                } else if (tabPosition == 1) { // Active
                    if (taskDate != null && !taskDate.before(today)) {
                        filteredTasks.add(task);
                    }
                } else if (tabPosition == 2) { // Expired
                    if (taskDate != null && taskDate.before(today)) {
                        filteredTasks.add(task);
                    }
                }
            } catch (ParseException e) {
                if (tabPosition == 0 || tabPosition == 1) filteredTasks.add(task);
            }
        }
        updateUI();
    }

    private Date getTodayDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        
        MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    performSearch(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    performSearch(newText);
                    return true;
                }
            });
        }
        return true;
    }

    private void performSearch(String query) {
        allTasks.clear();
        Cursor cursor = db.searchTasks(userId, query);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                addTaskFromCursor(cursor);
            } while (cursor.moveToNext());
            cursor.close();
        }
        applyFilter(tabLayout.getSelectedTabPosition());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
    }

    private void loadTasks() {
        allTasks.clear();
        Cursor cursor = db.getTasks(userId);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                addTaskFromCursor(cursor);
            } while (cursor.moveToNext());
            cursor.close();
        }
        applyFilter(tabLayout.getSelectedTabPosition());
    }

    private void addTaskFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_ID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_TITLE));
        String desc = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_DESCRIPTION));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_DATE));
        String time = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_TIME));
        String priority = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_PRIORITY));
        String category = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_CATEGORY));
        String timestamp = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK_TIMESTAMP));
        allTasks.add(new Task(id, userId, title, desc, date, time, priority, category, timestamp));
    }

    private void updateUI() {
        adapter.setTaskList(filteredTasks);
        if (filteredTasks.isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
        }
    }

    private void showTaskDialog(Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_task, null);
        builder.setView(dialogView);

        TextInputEditText etTitle = dialogView.findViewById(R.id.etTaskTitle);
        TextInputEditText etDesc = dialogView.findViewById(R.id.etTaskDescription);
        TextInputEditText etDate = dialogView.findViewById(R.id.etTaskDate);
        TextInputEditText etTime = dialogView.findViewById(R.id.etTaskTime);
        ChipGroup cgPriority = dialogView.findViewById(R.id.cgPriority);
        AutoCompleteTextView actvCategory = dialogView.findViewById(R.id.actvCategory);

        String[] categories = {"Personal", "Work", "Urgent", "Other"};
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categories);
        actvCategory.setAdapter(catAdapter);

        if (task != null) {
            etTitle.setText(task.getTitle());
            etDesc.setText(task.getDescription());
            etDate.setText(task.getTaskDate());
            etTime.setText(task.getTaskTime());
            actvCategory.setText(task.getCategory(), false);
            
            if (task.getPriority().equals("High")) cgPriority.check(R.id.chipHigh);
            else if (task.getPriority().equals("Medium")) cgPriority.check(R.id.chipMedium);
            else cgPriority.check(R.id.chipLow);
            
            builder.setTitle("Edit Task");
        } else {
            builder.setTitle("New Task");
            cgPriority.check(R.id.chipLow);
            actvCategory.setText(categories[0], false);
        }

        etDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> {
                        etDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        etTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> {
                        etTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            timePickerDialog.show();
        });

        builder.setPositiveButton(task != null ? "Update" : "Add", (dialog, which) -> {
            String title = etTitle.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();
            String date = etDate.getText().toString().trim();
            String time = etTime.getText().toString().trim();
            String category = actvCategory.getText().toString();
            
            int checkedId = cgPriority.getCheckedChipId();
            String priority = "Low";
            if (checkedId == R.id.chipHigh) priority = "High";
            else if (checkedId == R.id.chipMedium) priority = "Medium";
            
            if (!title.isEmpty()) {
                long id;
                if (task != null) {
                    db.updateTask(task.getId(), title, desc, date, time, priority, category);
                    id = task.getId();
                    cancelReminder((int) id);
                } else {
                    id = db.addTask(userId, title, desc, date, time, priority, category);
                }
                
                if (!date.isEmpty() && !time.isEmpty()) {
                    scheduleReminder((int) id, title, desc, date, time);
                }
                
                loadTasks();
            } else {
                Toast.makeText(MainActivity.this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void scheduleReminder(int taskId, String title, String desc, String date, String time) {
        try {
            Date taskDateTime = dateTimeFormat.parse(date + " " + time);
            if (taskDateTime != null) {
                long reminderTime = taskDateTime.getTime() - (5 * 60 * 1000); // 5 minutes before
                
                if (reminderTime > System.currentTimeMillis()) {
                    Intent intent = new Intent(this, ReminderBroadcastReceiver.class);
                    intent.putExtra("taskTitle", title);
                    intent.putExtra("taskDesc", desc);
                    intent.putExtra("taskId", taskId);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, taskId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    
                    if (alarmManager != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent);
                        } else {
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent);
                        }
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void cancelReminder(int taskId) {
        Intent intent = new Intent(this, ReminderBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, taskId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    @Override
    public void onEditClick(Task task) {
        showTaskDialog(task);
    }

    @Override
    public void onDeleteClick(Task task) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.deleteTask(task.getId());
                    cancelReminder(task.getId());
                    loadTasks();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
