package com.example.sqlexample2023


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sqlexample2023.databinding.ActivityMainScreenBinding
import com.example.sqlexample2023.databinding.DialogLayoutBinding
import java.util.Random

class MainScreen : AppCompatActivity() {
    private lateinit var binding:ActivityMainScreenBinding
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteList:MutableList<Note>
    private lateinit var adapter: NoteItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //object instantiation
        databaseHelper = DatabaseHelper(this)

        //setup recycleview
        recyclerView= binding.recycleView


        //add layout to recycleview
        recyclerView.layoutManager=LinearLayoutManager(this)

        //declare data
         noteList=getData()

        //initialize adapter
        adapter=NoteItemAdapter(noteList)
        adapter.onDeleteClick={ note ->
          showDeleteDialog(note)

        }
        adapter.onUpdateClick={note ->
            showUpdateDialog(note)
        }

        //add to adapter
        recyclerView.adapter=adapter

        binding.floatingActionButton.setOnClickListener {
            showAddDialog()
        }

    }

    private fun showDeleteDialog(note: Note) {
        val alertDialogBuilder= AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Delete Note")
        alertDialogBuilder.setMessage("Do you want to delete this note?")

        alertDialogBuilder.setPositiveButton("OK"){dialog,_->
            delete(note.id)
            noteList.remove(note)
            recyclerView.adapter?.notifyDataSetChanged()
            dialog.dismiss()

        }
        alertDialogBuilder.setNegativeButton("Cancel"){dialog,_->
            dialog.dismiss()
        }
        val alertDialog:AlertDialog=alertDialogBuilder.create()
        alertDialog.show()

    }

    private fun showUpdateDialog(note: Note) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Update Note")
        alertDialogBuilder.setMessage("Enter the updated details for the note:")

        val dialogLayout=layoutInflater.inflate(R.layout.dialog_layout,null)
        val dialogBinding=DialogLayoutBinding.bind(dialogLayout)
        alertDialogBuilder.setView(dialogLayout)

        dialogBinding.etDTitle.setText(note.title)
        dialogBinding.etDContent.setText(note.content)

        alertDialogBuilder.setPositiveButton("OK"){dialog,_->
            val title=dialogBinding.etDTitle.text.toString()
            val content=dialogBinding.etDContent.text.toString()
            val newNote =Note(note.id,title,content)
            update(newNote)

            //find the index of the viewholder in the recycle view
            val updateNotePosition =noteList.indexOfFirst { it.id==note.id}
            if(updateNotePosition!=-1){
                noteList[updateNotePosition] = newNote
                adapter.notifyItemChanged(updateNotePosition)
            }
            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton("Cancel"){dialog,_->
            dialog.dismiss()
        }
        val alertDialog:AlertDialog=alertDialogBuilder.create()
        alertDialog.show()


    }

    private fun showAddDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Add New Note")

        //declare layout for showdialog
        val dialogLayout=layoutInflater.inflate(R.layout.dialog_layout,null)
        val dialogBinding=DialogLayoutBinding.bind(dialogLayout)
        alertDialogBuilder.setView(dialogLayout)


        alertDialogBuilder.setPositiveButton("OK"){dialog,_->
            val title=dialogBinding.etDTitle.text.toString()
            val content=dialogBinding.etDContent.text.toString()
            val newNote=Note(0,title,content)
            addData(newNote)
            noteList.add(newNote)
            recyclerView.adapter?.notifyDataSetChanged()
            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton("Cancel"){dialog,_->
            dialog.dismiss()
        }
        val alertDialog:AlertDialog=alertDialogBuilder.create()
        alertDialog.show()



}

    private fun getData():MutableList<Note>{
        var allData=databaseHelper.getAllNotes()
      return allData

    }
    //INSERT
    private fun addData(note: Note) {
        databaseHelper.insertNote(note)
        Toast.makeText(applicationContext,"New note added ${note.id}", Toast.LENGTH_SHORT).show()
    }
    //UPDATE
    private  fun update(note:Note){
        databaseHelper.updateNote(note)
        getData()
        Toast.makeText(applicationContext,"Note updated! ${note.id}", Toast.LENGTH_SHORT).show()
    }
    //DELETE
    private fun delete(id:Int){
        databaseHelper.deleteNote(id)
        getData()
        Toast.makeText(applicationContext,"Note deleted!", Toast.LENGTH_SHORT).show()

    }
}