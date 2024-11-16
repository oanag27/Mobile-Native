package com.example.firstapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val taskViewModel: TaskViewModel by viewModels()
    private lateinit var adapter: CustomAdapter
    private val addTaskRequestCode = 1 // Request code for adding task
    private val taskDetailRequestCode = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        adapter = CustomAdapter { task ->
            // Pass selected task details to TaskDetailActivity
            val intent = Intent(this, TaskDetailActivity::class.java).apply {
                putExtra("TASK_ID", task.id)
                putExtra("TASK_TITLE", task.title)
                putExtra("TASK_DESCRIPTION", task.description)
                putExtra("TASK_PRIORITY", task.priority_level)
                putExtra(
                    "TASK_DATE",
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(task.dueDate)
                )
                putExtra("TASK_COMPLETION_STATUS", task.completion_status)
            }
            startActivityForResult(intent, taskDetailRequestCode)
        }
        recyclerView.adapter = adapter

        taskViewModel.tasks.observe(this) { taskList ->
            Log.d("MainActivity", "Task List Updated: ${taskList.size} tasks")
            adapter.submitList(taskList) // Update the adapter
        }
        val addTaskButton = findViewById<Button>(R.id.addTaskButton)
        addTaskButton.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivityForResult(intent, addTaskRequestCode)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == addTaskRequestCode && resultCode == RESULT_OK && data != null) {
            val title = data.getStringExtra("TASK_TITLE") ?: ""
            val description = data.getStringExtra("TASK_DESCRIPTION") ?: ""
            val dueDateTimestamp = data.getLongExtra("TASK_DUE_DATE", -1)
            val priority = data.getStringExtra("TASK_PRIORITY") ?: ""
            val completionStatus = data.getBooleanExtra("TASK_COMPLETION_STATUS", false)

            if (dueDateTimestamp != -1L) {
                val dueDate = Date(dueDateTimestamp)

                val newTask = Task(
                    id = 0, // Assuming your ViewModel assigns the ID
                    title = title,
                    description = description,
                    dueDate = dueDate,
                    priority_level = priority,
                    completion_status = completionStatus
                )

                taskViewModel.addTask(newTask) // Add the task to the ViewModel
            } else {
                Log.e("MainActivity", "Invalid due date received from AddActivity")
            }
        }
    }

}