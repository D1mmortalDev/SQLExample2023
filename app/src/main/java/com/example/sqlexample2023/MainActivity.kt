package com.example.sqlexample2023

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sqlexample2023.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

       databaseHelper = DatabaseHelper(this)
        viewData()
        binding.btnAdd.setOnClickListener{
            val title = binding.editTxtTitle.text.toString()
            val content = binding.editTxtContent.text.toString()
            insertData(title,content)
        }
        binding.btnUpdate.setOnClickListener{
            val title = binding.editTxtTitle.text.toString()
            val content = binding.editTxtContent.text.toString()
            val id =binding.editTxtId.text.toString().toInt()
            update(id,title,content)
        }
        binding.btnDelete.setOnClickListener {
            val id =binding.editTxtId.text.toString().toInt()
            delete(id)
        }
    }
    private fun viewData(){
        var allData=databaseHelper.getAllNotes()
        var resultString=""

        for(note in allData){
            resultString+="${note.title} ${note.content}\n"
        }
        binding.txtResult.text=resultString
    }
    //INSERT
    private fun insertData(title: String, content: String) {
        val sampleNote=Note(0,title,content)
        databaseHelper.insertNote(sampleNote)
        //view data from database
        viewData()
        Toast.makeText(applicationContext,"New note added -${sampleNote.id}",Toast.LENGTH_SHORT).show()
    }
    //UPDATE
    private  fun update(id:Int,title: String,content: String){
        val noteObject=Note(id,title,content)
        databaseHelper.updateNote(noteObject)
        viewData()
        Toast.makeText(applicationContext,"Note updated!",Toast.LENGTH_SHORT).show()
    }
    //DELETE
    private fun delete(id:Int){
     databaseHelper.deleteNote(id)
        viewData()
        Toast.makeText(applicationContext,"Note deleted!",Toast.LENGTH_SHORT).show()

    }
}