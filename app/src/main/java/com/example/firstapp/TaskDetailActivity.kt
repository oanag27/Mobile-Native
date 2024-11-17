package com.example.firstapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale

class TaskDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        val taskId = intent.getIntExtra("TASK_ID", 0)
        val taskTitle = intent.getStringExtra("TASK_TITLE") ?: ""
        val taskDescription = intent.getStringExtra("TASK_DESCRIPTION") ?: ""
        val taskPriority = intent.getStringExtra("TASK_PRIORITY") ?: ""
        val taskDate = intent.getStringExtra("TASK_DATE") ?: ""
        val taskCompletionStatus = intent.getBooleanExtra("TASK_COMPLETION_STATUS", false)

        val titleEditText = findViewById<EditText>(R.id.taskTitleEditDetail).apply {
            setText(taskTitle)
        }
        val descriptionEditText = findViewById<EditText>(R.id.taskDescriptionEditDetail).apply {
            setText(taskDescription)
        }
        val priorityEditText = findViewById<EditText>(R.id.taskPriorityEditDetail).apply {
            setText(taskPriority)
        }
        val completionStatusSwitch = findViewById<Switch>(R.id.completionStatusSwitch).apply {
            isChecked = taskCompletionStatus
        }
        val taskDateTextView = findViewById<TextView>(R.id.taskDateEditDetail)
        taskDateTextView.text = taskDate

        // Set up the click listener for the due date
        taskDateTextView.setOnClickListener {
            showDatePicker()
        }
        findViewById<Button>(R.id.saveButtonTaskDetail).setOnClickListener {
            val updatedTitle = titleEditText.text.toString()
            val updatedDescription = descriptionEditText.text.toString()
            val updatedPriority = priorityEditText.text.toString()
            val updatedCompletionStatus = completionStatusSwitch.isChecked

            // Send the updated values back to MainActivity
            val resultIntent = Intent().apply {
                putExtra("UPDATED_TASK_ID", taskId)
                putExtra("UPDATED_TASK_TITLE", updatedTitle)
                putExtra("UPDATED_TASK_DESCRIPTION", updatedDescription)
                putExtra("UPDATED_TASK_PRIORITY", updatedPriority)
                putExtra("UPDATED_TASK_DATE", taskDateTextView.text.toString())
                putExtra("UPDATED_TASK_COMPLETION_STATUS", updatedCompletionStatus)
            }
            setResult(RESULT_OK, resultIntent)
            Toast.makeText(this, "Task updated successfully!", Toast.LENGTH_SHORT).show()
            finish()
        }

        findViewById<Button>(R.id.backToHomeButton).setOnClickListener {
            onBackPressed()
        }

    }
    private fun showDatePicker() {
        // Get the current date from the TextView
        val calendar = Calendar.getInstance()
        val currentDate = findViewById<TextView>(R.id.taskDateEditDetail).text.toString()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        try {
            val parsedDate = dateFormat.parse(currentDate)
            calendar.time = parsedDate ?: Date()
        } catch (e: Exception) {
            calendar.time = Date()
        }

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            // Set the new due date
            val newDate = GregorianCalendar(selectedYear, selectedMonth, selectedDay).time
            // Format and update the TextView with the new due date
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            findViewById<TextView>(R.id.taskDateEditDetail).text = dateFormat.format(newDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun showDeleteConfirmationDialog(taskId: Int) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Are you sure you want to delete this task?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                deleteTask(taskId)
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.dismiss() // Dismiss the dialog if "No" is clicked
            }

        val alert = dialogBuilder.create()
        alert.show()
    }

    private fun deleteTask(taskId: Int) {
        val resultIntent = Intent().apply {
            putExtra("DELETED_TASK_ID", taskId)
        }
        setResult(RESULT_OK,resultIntent)  // Send back the task ID to MainActivity
        finish()
    }
}

