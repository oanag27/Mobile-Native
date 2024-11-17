package com.example.firstapp
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import java.text.SimpleDateFormat
import java.util.Locale

class CustomAdapter(private val onTaskClicked: (Task) -> Unit, private val onDetailButtonClicked: (Task) -> Unit) : ListAdapter<Task, CustomAdapter.ViewHolder>(TaskDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_layout, parent, false)
        return ViewHolder(view, onTaskClicked, onDetailButtonClicked)
    }

    // Bind the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(itemView: View, private val onTaskClicked: (Task) -> Unit, private val onDetailButtonClicked: (Task) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.title)
        private val priorityTextView: TextView = itemView.findViewById(R.id.taskPriority)
        private val detailButton: ImageView = itemView.findViewById(R.id.taskDetail)
        private val descriptionTextView : TextView = itemView.findViewById(R.id.description)
        private val dueDateTextView : TextView = itemView.findViewById(R.id.dueDate)
        // Bind data to views
        fun bind(task: Task) {
            titleTextView.text = task.title
            descriptionTextView.text = task.description
            dueDateTextView.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(task.dueDate)
            priorityTextView.text = task.priority_level
            // Handle click on the task item for delete confirmation
            itemView.setOnClickListener {
                onTaskClicked(task) // Trigger the delete confirmation dialog
            }

            // Handle click on the ">" button to go to TaskDetailActivity
            detailButton.setOnClickListener {
                onDetailButtonClicked(task) // Trigger the TaskDetailActivity for task update
            }
        }
    }

    // DiffUtil callback for efficient updates
    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.title == newItem.title && oldItem.priority_level == newItem.priority_level
        }
    }
}
