package com.example.firstapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.util.Date

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
        val dateEditText = findViewById<EditText>(R.id.taskDateEditDetail).apply {
            setText(taskDate)
        }
        val completionStatusSwitch = findViewById<Switch>(R.id.completionStatusSwitch).apply {
            isChecked = taskCompletionStatus
        }

        findViewById<Button>(R.id.saveButtonTaskDetail).setOnClickListener {
            val updatedTitle = titleEditText.text.toString()
            val updatedDescription = descriptionEditText.text.toString()
            val updatedPriority = priorityEditText.text.toString()
            val updatedDate = dateEditText.text.toString().trim()
            val updatedCompletionStatus = completionStatusSwitch.isChecked

            // Send the updated values back to MainActivity
            val resultIntent = Intent().apply {
                putExtra("UPDATED_TASK_ID", taskId)
                putExtra("UPDATED_TASK_TITLE", updatedTitle)
                putExtra("UPDATED_TASK_DESCRIPTION", updatedDescription)
                putExtra("UPDATED_TASK_PRIORITY", updatedPriority)
                putExtra("UPDATED_TASK_DATE", updatedDate)
                putExtra("UPDATED_TASK_COMPLETION_STATUS", updatedCompletionStatus)
            }
            setResult(RESULT_OK, resultIntent)
            Toast.makeText(this, "Task updated successfully!", Toast.LENGTH_SHORT).show()
            finish()
        }

        findViewById<Button>(R.id.backToHomeButton).setOnClickListener {
            onBackPressed()
        }
        findViewById<Button>(R.id.deleteButtonTaskDetail).setOnClickListener {
            showDeleteConfirmationDialog(taskId)
        }

    }
    private fun showDeleteConfirmationDialog(taskId: Int) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Are you sure you want to delete this task?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                // Task deletion confirmed, remove it
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
        //Toast.makeText(this, "Task deleted successfully!", Toast.LENGTH_SHORT).show()
        finish()  // Close this activity and return to MainActivity
    }
}

