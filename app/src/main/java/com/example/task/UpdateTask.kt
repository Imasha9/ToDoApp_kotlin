package com.example.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.example.task.databinding.ActivityUpdateTaskBinding

class UpdateTask : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateTaskBinding
    private lateinit var db:TaskDatabase
    private var taskId:Int=-1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUpdateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db=TaskDatabase(this)

        taskId=intent.getIntExtra("task_id",-1)
        if (taskId==-1)
        {
            finish()
            return
        }

        val task=db.getTaskById(taskId )
        binding.edittitleText.setText(task.title)
        binding.edittimeText.setText(task.time)

        binding.updatesaveBtn.setOnClickListener{
            val newtitle=binding.edittitleText.text.toString()
            val newtime=binding.edittimeText.text.toString()
            val updatetask=Task(taskId,newtitle,newtime)
            db.updateTask(updatetask)
            finish()
            Toast.makeText(this,"Change Saved",Toast.LENGTH_SHORT).show()
        }
    }
}