package com.example.firstapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Date

class TaskViewModel : ViewModel() {
    // Use LiveData for task list
    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> get() = _tasks
    private var lastTaskId = 0
    private val taskList = mutableListOf<Task>() // Mutable list to store tasks
    init {
        val initialTasks = listOf(
            Task(1, "Task 1", "Description for Task 1", Date(), "Low", false),
            Task(2, "Task 2", "Description for Task 2", Date(System.currentTimeMillis() + 86400000), "Medium", false), // +1 day
            Task(3, "Task 3", "Description for Task 3", Date(System.currentTimeMillis() + 172800000), "High", false) // +2 days
        )
        taskList.addAll(initialTasks) // Add initial tasks to taskList
        _tasks.value = taskList.toList() // Set the LiveData value
        lastTaskId = initialTasks.size // Set lastTaskId based on initial tasks
    }

    fun addTask(task: Task) {
        lastTaskId++
        val newTask = task.copy(id = lastTaskId) // Assign an ID
        taskList.add(newTask)
        _tasks.value = taskList.toList()
        Log.d("TaskViewModel", "Added Task: $newTask. Total tasks: ${taskList.size}")
    }
    fun updateTask(task: Task) {
        val updatedTaskList = tasks.value?.map {
            if (it.id == task.id) task else it
        } ?: listOf(task)

        _tasks.value = updatedTaskList
    }
    fun deleteTask(taskId: Int) {
        val updatedList = taskList.filter { it.id != taskId }
        taskList.clear()
        taskList.addAll(updatedList)
        _tasks.value = taskList.toList()
        Log.d("TaskViewModel", "Deleted Task ID: $taskId")
    }
}
