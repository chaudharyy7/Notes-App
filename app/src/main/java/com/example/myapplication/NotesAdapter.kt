package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(private val notes: List<NoteItem>, private var listener: OnItemClickListener) :
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    interface OnItemClickListener {
        fun onDeleteClick(noteId: String)
        fun onUpdateClick(noteId: String, title: String, description: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notesitem, parent, false)
        return NotesViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
        holder.updateBtn.setOnClickListener {
            listener.onUpdateClick(note.noteId, note.title, note.description)
        }
        holder.deleteBtn.setOnClickListener {
            listener.onDeleteClick(note.noteId)
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val description: TextView = itemView.findViewById(R.id.tvDescription)
        val updateBtn: TextView = itemView.findViewById(R.id.updateBtn)
        val deleteBtn: TextView = itemView.findViewById(R.id.deletBtn)

        fun bind(note: NoteItem) {
            title.text = note.title
            description.text = note.description
        }
    }
}
