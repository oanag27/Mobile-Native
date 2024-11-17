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
    private val taskDeleteRequestCode = 3


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
        } else if (requestCode == taskDetailRequestCode && resultCode == RESULT_OK && data != null) {
            val updatedTaskId = data.getIntExtra("UPDATED_TASK_ID", -1)
            val updatedTitle = data.getStringExtra("UPDATED_TASK_TITLE")
            val updatedDescription = data.getStringExtra("UPDATED_TASK_DESCRIPTION")
            val updatedPriority = data.getStringExtra("UPDATED_TASK_PRIORITY")
            val updatedDate = data.getStringExtra("UPDATED_TASK_DATE")
            val updatedCompletionStatus =
                data.getBooleanExtra("UPDATED_TASK_COMPLETION_STATUS", false)

            if (updatedTaskId != -1) {
                val updatedTask = taskViewModel.tasks.value?.find { it.id == updatedTaskId }
                Log.d(
                    "TaskDetailActivity",
                    "Returning Updated Task: ID=$taskId Title=$updatedTitle"
                )
                if (updatedTask != null) {
                    // Update task details
                    updatedTask.title = updatedTitle ?: updatedTask.title
                    updatedTask.description = updatedDescription ?: updatedTask.description
                    updatedTask.priority_level = updatedPriority ?: updatedTask.priority_level
                    updatedTask.dueDate = updatedDate?.let { date ->
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date)
                    } ?: updatedTask.dueDate
                    updatedTask.completion_status = updatedCompletionStatus

                    if (updatedTask != null) {
                        taskViewModel.updateTask(updatedTask)
                        adapter.notifyDataSetChanged()
                        Log.d(
                            "MainActivity",
                            "Task updated: ID=${updatedTask.id}, Title=${updatedTask.title}"
                        )
                    } else {
                        Log.e("MainActivity", "Task not found for ID: $updatedTaskId")
                    }
                }
            }
        }else if (requestCode == taskDeleteRequestCode && resultCode == RESULT_OK && data != null) {
            val deletedTaskId = data.getIntExtra("DELETED_TASK_ID", -1)
            if (deletedTaskId != -1) {
                taskViewModel.deleteTask(deletedTaskId) // Delete the task in ViewModel
                Log.d("MainActivity", "Task deleted: ID=$deletedTaskId")
                taskViewModel.tasks.observe(this) { updatedTaskList ->
                    adapter.submitList(updatedTaskList)
                }
            }
        }
    }
}