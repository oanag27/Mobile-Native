package com.example.firstapp
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter


class CustomAdapter(private val onTaskClicked: (Task) -> Unit) : ListAdapter<Task, CustomAdapter.ViewHolder>(TaskDiffCallback()) {
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_layout, parent, false)
        return ViewHolder(view, onTaskClicked)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(itemView: View, private val onTaskClicked: (Task) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.title)
        private val priorityTextView: TextView = itemView.findViewById(R.id.taskPriority)
        private val detailButton: ImageView = itemView.findViewById(R.id.taskDetail) // Ensure this ID matches your

        // Bind data to views
        fun bind(task: Task) {
            titleTextView.text = task.title
            priorityTextView.text = task.priority_level
            // Set up click listener for the detail button
//            detailButton.setOnClickListener {
//                // Start TaskDetailActivity with task details
//                val context = itemView.context
//                val intent = Intent(context, TaskDetailActivity::class.java).apply {
//                    putExtra("TASK_ID", task.id)
//                    putExtra("TASK_TITLE", task.title)
//                    putExtra("TASK_DESCRIPTION", task.description)
//                    putExtra("TASK_PRIORITY", task.priority_level)
//                    putExtra("TASK_DATE", SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(task.dueDate))
//                    // Include task completion status in the intent
//                    putExtra("TASK_COMPLETION_STATUS", task.completion_status)
//                }
//                context.startActivity(intent)
//                }
//
//            }
            detailButton.setOnClickListener {
                onTaskClicked(task) // Call the click listener with the task
            }

            // Set up click listener for entire item to trigger onTaskClicked with the task
            itemView.setOnClickListener {
                onTaskClicked(task) // Invoke the callback when the item is clicked
            }
        }
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            // Compare item IDs if you have them
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.priority_level == newItem.priority_level
        }
    }
}