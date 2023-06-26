package com.codebykt.todo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private EditText editTextTaskName, editTextTaskDescription;
    private Button buttonAddDate, buttonAdd;
    private TextView textViewDate;
    private ListView listViewTasks;
    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> tasksAdapter;
    private ArrayList<String> tasksList;

    private Calendar selectedDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dbHelper = new DatabaseHelper(this);

        editTextTaskName = findViewById(R.id.editTextTaskName);
        editTextTaskDescription = findViewById(R.id.editTextTaskDescription);
        buttonAddDate = findViewById(R.id.buttonAddDate);
        buttonAdd = findViewById(R.id.buttonAdd);
        textViewDate = findViewById(R.id.textViewDate);
        listViewTasks = findViewById(R.id.listViewTasks);

        tasksList = new ArrayList<>();
        tasksAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tasksList);
        listViewTasks.setAdapter(tasksAdapter);

        buttonAddDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });

        listViewTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                markTaskAsCompleted(position);
            }
        });

        displayTasks();
    }

    private void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedDateTime = Calendar.getInstance();
                selectedDateTime.set(Calendar.YEAR, year);
                selectedDateTime.set(Calendar.MONTH, monthOfYear);
                selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                int hour = currentDate.get(Calendar.HOUR_OF_DAY);
                int minute = currentDate.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(HomeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedDateTime.set(Calendar.MINUTE, minute);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                        String dateTime = dateFormat.format(selectedDateTime.getTime());
                        textViewDate.setText("Selected Date and Time: " + dateTime);
                    }
                }, hour, minute, false);

                timePickerDialog.show();
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void addTask() {
        String taskName = editTextTaskName.getText().toString().trim();
        String taskDescription = editTextTaskDescription.getText().toString().trim();
        String taskDateTime = selectedDateTime != null ? selectedDateTime.getTime().toString() : "";

        if (taskName.isEmpty()) {
            Toast.makeText(this, "Please enter a task name", Toast.LENGTH_SHORT).show();
            return;
        }

        long result = dbHelper.insertTask(taskName, taskDescription, taskDateTime);

        if (result != -1) {
            Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show();
            editTextTaskName.setText("");
            editTextTaskDescription.setText("");
            textViewDate.setText("");
            selectedDateTime = null;
            displayTasks();
        } else {
            Toast.makeText(this, "Failed to add task", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayTasks() {
        tasksList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int taskId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID));
            String taskName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TASK_NAME));
            String taskDescription = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TASK_DESCRIPTION));
            String taskDateTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TASK_DATE_TIME));
            int isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_IS_COMPLETED));

            String taskDisplay = "";

            if (isCompleted == 1) {
                taskDisplay += "[Completed] ";
            }

            if (taskName != "") {
                taskDisplay += "Task Name: " + taskName + "\n";
            }

            if (taskDescription != "") {
                taskDisplay += "Description: " + taskDescription + "\n";
            }
            if (taskDateTime != "") {
                taskDisplay += "Date and Time: " + taskDateTime;
            }

            tasksList.add(taskDisplay);
        }

        cursor.close();
        db.close();

        tasksAdapter.notifyDataSetChanged();
    }

    private void markTaskAsCompleted(int position) {
        String taskName = tasksList.get(position);
        String completedTaskName = taskName.replace("[Completed] ", "");

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COL_TASK_NAME + " = ?", new String[]{completedTaskName});
        db.close();

        displayTasks();
    }






}
