package com.example.task

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter (private var tasks:List<Task>,context: Context):
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
        private val db:TaskDatabase= TaskDatabase(context)
    private val originalTasks: List<Task> = tasks.toList()

    fun filterTasks(query: String) {
        val filteredTasks = originalTasks.filter { task ->
            task.title.contains(query, ignoreCase = true) || // Filter by title
                    task.description.contains(query, ignoreCase = true) || // Filter by description
                    task.priority.contains(query, ignoreCase = true) || // Filter by priority
                    task.deadline.contains(query, ignoreCase = true) // Filter by deadline
        }
        refreshData(filteredTasks)
    }
    class TaskViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val titleTextView:TextView=itemView.findViewById(R.id.titleText)
        val descriptionTextView:TextView=itemView.findViewById(R.id.description)
        val priorityTextView:TextView=itemView.findViewById(R.id.priority)
        val deadlineTextView:TextView=itemView.findViewById(R.id.deadline)
        val updateButton:ImageView=itemView.findViewById(R.id.updatebtn)
        val deleteButton:ImageView=itemView.findViewById(R.id.deletebtn)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.TaskViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.all_tasks,parent,false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskAdapter.TaskViewHolder, position: Int) {
        val task=tasks[position]
        holder.titleTextView.text=task.title
        holder.descriptionTextView.text=task.description
        holder.priorityTextView.text=task.priority
        holder.deadlineTextView.text=task.deadline

        holder.updateButton.setOnClickListener {
            val intent=Intent(holder.itemView.context,UpdateTask::class.java).apply {
                putExtra("task_id",task.id)
            }
            holder.itemView.context.startActivity(intent)
        }
        holder.deleteButton.setOnClickListener {
            db.deleteTask(task.id)
            refreshData(db.getAllTasks())
            Toast.makeText(holder.itemView.context,"Deleted",Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int =tasks.size

    fun refreshData(newTasks:List<Task>){
        tasks=newTasks
        notifyDataSetChanged()
    }

}