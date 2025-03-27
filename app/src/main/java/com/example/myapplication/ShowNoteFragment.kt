package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ShowNoteFragment : Fragment(), NotesAdapter.OnItemClickListener {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_show_note, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val noteRef = database.child("user").child(user.uid).child("notes")

            noteRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val notesList = mutableListOf<NoteItem>()
                    for (noteSnapshot in snapshot.children) {
                        val note = noteSnapshot.getValue(NoteItem::class.java)
                        note?.let { notesList.add(it) }
                    }
                    notesList.reverse()
                    recyclerView.adapter = NotesAdapter(notesList, this@ShowNoteFragment)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

        return view
    }

    override fun onDeleteClick(noteId: String) {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val noteReference =
                database.child("user").child(user.uid).child("notes").child(noteId)
            noteReference.removeValue()
        }
    }

    override fun onUpdateClick(noteId: String, title: String, description: String) {
        val dialogBinding = layoutInflater.inflate(R.layout.dialough_update, null)
        val editTitle = dialogBinding.findViewById<EditText>(R.id.editTextText)
        val editDescription = dialogBinding.findViewById<EditText>(R.id.editTextText2)

        editTitle.setText(title)
        editDescription.setText(description)

        AlertDialog.Builder(requireContext())
            .setView(dialogBinding)
            .setTitle("Update Note")
            .setPositiveButton("Update") { dialog, _ ->
                val newTitle = editTitle.text.toString()
                val newDescription = editDescription.text.toString()
                updateNote(noteId, newTitle, newDescription)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun updateNote(noteId: String, newTitle: String, newDescription: String) {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val noteReference =
                database.child("user").child(user.uid).child("notes").child(noteId)
            val updatedNote = NoteItem(newTitle, newDescription, noteId)

            noteReference.setValue(updatedNote)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            context,
                            "Note updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Failed to update note",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}
