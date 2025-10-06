package com.amme.noterex

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.amme.noterex.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query



class MainActivity : AppCompatActivity() {
    public fun deleteNote(pos: Int) {
        if (pos in notes.indices) {
            notes.removeAt(pos)
            binding.rclr.adapter?.notifyItemRemoved(pos)
            binding.rclr.adapter?.notifyItemRangeChanged(pos, notes.size)
        }
    }

    private lateinit var binding: ActivityMainBinding
    val notes = arrayListOf<Notes>()
    val db = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    lateinit var adapter: NotesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        /*notes.add(
            Notes("00",
                "welcome to NoteRex",
                "\uD83D\uDCD6 How to use:\n" +
                        "\n" +
                        "- To add a new note, tap the ➕ button\n" +
                        "- Tap a note to view the full text and edit note\n" +
                        "- To delete, long press on the note\n" +
                        "\n" +
                        "✨ Enjoy your writing experience with NoteRex\n"
            )
        )
        notes.add(Notes("0", "short", "this is a short note"))
        notes.add(
            Notes("000",
                "long",
                "this is a very loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                        "oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                        "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                        "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong note"
            )
        )*/

        adapter = NotesAdapter(this, notes)

        binding.rclr.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rclr.adapter = adapter

        userId?.let { uid ->
            db.collection("users").document(uid)
                .collection("notes")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { result ->
                    notes.clear()
                    for(doc in result){
                        notes.add(
                            Notes(
                                doc.id, // id لتحديث/حذف لاحقًا
                                doc.getString("title") ?: "",
                                doc.getString("content") ?: ""
                            )
                        )
                    }
                    adapter.notifyDataSetChanged()
                }
        }


        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, NoteActivity::class.java)
            startActivityForResult(intent, 100)
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {
            val title = data.getStringExtra("note_title") ?: ""
            val content = data.getStringExtra("note_content") ?: ""
            val pos = data.getIntExtra("pos", -1)
            val noteId = data.getStringExtra("note_id") // لازم ترجع id من NoteActivity

            when (requestCode) {
                100 -> { // نوتة جديدة
                    if (noteId != null) {
                        notes.add(Notes(noteId, title, content))
                        binding.rclr.adapter?.notifyItemInserted(notes.size - 1)
                    }
                }

                101 -> { // تعديل نوتة موجودة
                    if (pos != -1 && pos < notes.size && noteId != null) {
                        notes[pos].title = title
                        notes[pos].txt = content
                        binding.rclr.adapter?.notifyItemChanged(pos)
                    }
                }
            }
        }
    }


}