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

//    fun updateTask(updatedTask: Task) {
//        val taskIndex = taskList.indexOfFirst { it.id == updatedTask.id }
//        if (taskIndex != -1) {
//            taskList[taskIndex] = updatedTask // Update the task in the list
//            _tasks.value = taskList.toList() // Create a new list and update the LiveData
//            Log.d("TaskViewModel", "Updated Task: $updatedTask at index $taskIndex")
//        } else {
//            Log.e("TaskViewModel", "Task with ID ${updatedTask.id} not found.")
//        }
//    }
//    fun deleteTask(taskId: Int) {
//        val taskToDelete = taskList.find { it.id == taskId }
//        if (taskToDelete != null) {
//            taskList.remove(taskToDelete) // Remove the task from the list
//            _tasks.postValue(taskList.toList()) // Update LiveData with the new list
//        }
//    }
}
