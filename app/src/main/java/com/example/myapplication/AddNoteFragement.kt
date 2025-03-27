package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddNoteFragement : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_note_fragement, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find button and set click listener
        val addButton = view.findViewById<Button>(R.id.addNoteButton)
        val title = view.findViewById<EditText>(R.id.title)
        val description = view.findViewById<EditText>(R.id.description)

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        addButton.setOnClickListener {
            val titleText = title.text.toString()  // Changed variable name
            val descriptionText = description.text.toString()  // Changed variable name

            if (titleText.isEmpty() || descriptionText.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                val currentUser = auth.currentUser
                currentUser?.let { user ->
                    val noteKey = database.child("user").child(user.uid).child("notes").push().key
                    val note = NoteItem(titleText, descriptionText, noteKey ?: "")

                    if (noteKey != null) {
                        database.child("user").child(user.uid).child("notes").child(noteKey)
                            .setValue(note)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(requireContext(), "Note added successfully", Toast.LENGTH_SHORT).show()
                                    title.text.clear()
                                    description.text.clear()
                                }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(requireContext(), "Failed to add note: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
        }
    }
}
