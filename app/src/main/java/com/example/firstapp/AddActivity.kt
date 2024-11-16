package com.example.firstapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale

class AddActivity : AppCompatActivity() {
    private lateinit var nameInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var dueDateInput: TextView // This will show the selected date
    private lateinit var priorityInput: EditText
    private lateinit var completionStatusSwitch: Switch
    private lateinit var backToHomeButton: Button
    private lateinit var addTask: Button
    private lateinit var dueDate: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        // Initialize the inputs
        nameInput = findViewById(R.id.nameInput)
        descriptionInput = findViewById(R.id.descriptionInput)
        dueDateInput = findViewById(R.id.dueDateInput)
        priorityInput = findViewById(R.id.priorityInput)
        completionStatusSwitch = findViewById(R.id.completionStatusSwitch)
        backToHomeButton = findViewById(R.id.backToHomeButton)
        addTask = findViewById(R.id.addTask)

        dueDateInput.setOnClickListener {
            showDatePicker()
        }

        // Set click listener for the Add Task button
        addTask.setOnClickListener {
            saveTask()
        }

        val backButton = findViewById<Button>(R.id.backToHomeButton)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    private fun showDatePicker() {
        // Get the current date
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create the DatePickerDialog
        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            // Create a Date object from the selected values
            dueDate = GregorianCalendar(selectedYear, selectedMonth, selectedDay).time

            // Format the selected date as a string
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dueDateInput.text = dateFormat.format(dueDate) // Display the date in the TextView
        }, year, month, day)

        datePickerDialog.show() // Show the dialog
    }

    private fun saveTask() {
        val name = nameInput.text.toString()
        val description = descriptionInput.text.toString()
        val priority = priorityInput.text.toString()
        val completionStatus = completionStatusSwitch.isChecked // Get the Switch state

        // Ensure all fields are filled out
        if (name.isEmpty() || description.isEmpty() || priority.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if due date has been set
        if (!::dueDate.isInitialized) {
            Toast.makeText(this, "Please select a due date", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a new Task object
        val task = Task(
            id = 0,
            title = name,
            description = description,
            dueDate = dueDate,
            priority_level = priority,
            completion_status = completionStatus
        )
        // Create an Intent to return the task data to MainActivity
        val resultIntent = Intent().apply {
            putExtra("TASK_TITLE", name)
            putExtra("TASK_DESCRIPTION", description)
            putExtra("TASK_DUE_DATE", dueDate.time)  // Send the date as a timestamp (long)
            putExtra("TASK_PRIORITY", priority)
            putExtra("TASK_COMPLETION_STATUS", completionStatus)
        }
        setResult(RESULT_OK, resultIntent)  // Send the result back
        Toast.makeText(this, "Task saved successfully!", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun clearInputs() {
        nameInput.text.clear()
        descriptionInput.text.clear()
        dueDateInput.text = "Select a date"
        priorityInput.text.clear()
        completionStatusSwitch.isChecked = false
    }
}