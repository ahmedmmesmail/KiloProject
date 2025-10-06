package com.amme.noterex

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NotesAdapter (val activity: Activity, val notes: ArrayList<Notes>)
    : RecyclerView.Adapter<NotesAdapter.MVH>() {

    class MVH (view: View) : RecyclerView.ViewHolder(view) {
        val parent: CardView = view.findViewById(R.id.card)
        val title: TextView = view.findViewById(R.id.title)
        val txt: TextView = view.findViewById(R.id.txt)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int)
    : NotesAdapter.MVH {
        val view = activity.layoutInflater.inflate(R.layout.note_item, parent, false)
        return MVH(view)
    }


    override fun onBindViewHolder(holder: NotesAdapter.MVH, position: Int) {
        val note = notes[position]
        holder.title.text = note.title
        holder.txt.text = note.txt


        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, NoteActivity::class.java)
            intent.putExtra("note_title", note.title)
            intent.putExtra("note_content", note.txt)
            intent.putExtra("pos", position)
            intent.putExtra("note_id", note.id) // مهم للتعديل على Firebase
            (holder.itemView.context as Activity).startActivityForResult(intent, 101)
        }


        holder.itemView.setOnLongClickListener {
            val note = notes[position]
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                FirebaseFirestore.getInstance()
                    .collection("users").document(userId)
                    .collection("notes").document(note.id)
                    .delete()
                    .addOnSuccessListener {
                        notes.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, notes.size)
                    }
            }
            true
        }

    }

    override fun getItemCount() = notes.size

}