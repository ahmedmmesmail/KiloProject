package com.amme.noterex

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.amme.noterex.databinding.ActivityNoteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.log

class NoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val noteTitle = intent.getStringExtra("note_title")
        val noteContent = intent.getStringExtra("note_content")
        val pos = intent.getIntExtra("pos", -1)

        if (noteTitle != null) binding.etTitle.setText(noteTitle)
        if (noteContent != null) binding.etContent.setText(noteContent)

        binding.saveBtn.setOnClickListener {
            val title = binding.etTitle.text.trim().toString()
            val content = binding.etContent.text.trim().toString()

            if (title.isBlank() && content.isBlank()) {
                setResult(Activity.RESULT_CANCELED)
                finish()
                return@setOnClickListener
            }

            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId == null) {
                // المستخدم غير مسجل دخول → ممكن ترجع رسالة خطأ
                setResult(Activity.RESULT_CANCELED)
                finish()
                return@setOnClickListener
            }

            // 🔹 احضر قاعدة البيانات
            val db = FirebaseFirestore.getInstance()

            // 🔹 حضر البيانات للنوتة
            val noteData = hashMapOf(
                "title" to title,
                "content" to content,
                "timestamp" to FieldValue.serverTimestamp()
            )

            // 🔹 إذا جاي من نوتة موجودة (pos != -1) ممكن تستخدم noteId لتحديثها
            val noteId = intent.getStringExtra("note_id")
            if (!noteId.isNullOrEmpty()) {
                // تحديث نوتة موجودة
                db.collection("users").document(userId)
                    .collection("notes").document(noteId)
                    .set(noteData)
                val resultIntent = Intent().apply {
                    putExtra("note_title", title)
                    putExtra("note_content", content)
                    putExtra("pos", pos)
                    putExtra("note_id", noteId)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                // نوتة جديدة
                db.collection("users").document(userId)
                    .collection("notes")
                    .add(noteData)
                    .addOnSuccessListener { docRef ->
                        val resultIntent = Intent().apply {
                            putExtra("note_title", title)
                            putExtra("note_content", content)
                            putExtra("pos", pos)
                            putExtra("note_id", docRef.id) // Document ID الحقيقي
                        }
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }
            }
        }



        /*binding.saveBtn.setOnClickListener {
            val title = binding.etTitle.text.trim().toString()
            val content = binding.etContent.text.trim().toString()

            if (title.trim().isBlank() && content.trim().isBlank()) {
                setResult(Activity.RESULT_CANCELED)
                finish()
                return@setOnClickListener
            } else {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId == null) {
                    // المستخدم غير مسجل دخول → ممكن ترجع رسالة خطأ
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                    return@setOnClickListener
                }
                val resultIntent = Intent().apply {
                    putExtra("note_title", title)
                    putExtra("note_content", content)
                    putExtra("pos", pos)
                }
                setResult(Activity.RESULT_OK, resultIntent)
            }

            finish()
        }*/

    }
}