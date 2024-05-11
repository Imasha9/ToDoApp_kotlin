package com.example.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.task.databinding.ActivityAddTasksBinding
import com.example.task.databinding.ActivityMainBinding

class AddTasks : AppCompatActivity() {
    private  lateinit var binding: ActivityAddTasksBinding
    private lateinit var db:TaskDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db= TaskDatabase(this)
        binding.saveBt.setOnClickListener {
            val title=binding.titleEdit.text.toString()
            val description=binding.descriptionEdit.text.toString()
            val priority=binding.priorityEdit.text.toString()
            val deadline=binding.deadlineEdit.text.toString()
            val task=Task(0,title,description,priority,deadline)
            db.insertTask(task)
            finish()
            Toast.makeText(this,"Note Saved",Toast.LENGTH_SHORT).show()

        }
    }
}