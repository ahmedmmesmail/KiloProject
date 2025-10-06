package com.amme.noterex

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.amme.noterex.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    Snackbar.make(
                        binding.root,
                        "Google sign in failed: ${e.message}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.newUser.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        binding.googleBtn.setOnClickListener {
            signInWithGoogle()
        }

        binding.emailEt.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                binding.emailTi.error = null
            }
        }

        binding.passEt.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                binding.passTi.error = null
            }
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            val pass = binding.passEt.text.toString().trim()

            var valid = true

            when {
                email.isBlank() -> {
                    binding.emailTi.error = "Required"
                    valid = false
                }

                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.emailTi.error = "Invalid email"
                    valid = false
                }

                else -> binding.emailTi.error = null
            }

            when {
                pass.isBlank() -> {
                    binding.passTi.error = "Required"
                    valid = false
                }

                else -> binding.passTi.error = null
            }

            if (valid) {
                signIn(email, pass)
            }
        }

        binding.forgetPass.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            if (email.isBlank()) {
                binding.emailTi.error = "Required"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailTi.error = "Invalid email"
            } else {
                binding.emailTi.error = null
                Firebase.auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Snackbar.make(
                                binding.root,
                                "Password reset link sent",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }


    }

    private fun signIn(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    if (auth.currentUser!!.isEmailVerified) {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else
                        Snackbar.make(binding.root, "Check your Email", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                } else {
                    Snackbar.make(
                        binding.root, "Authentication failed.", Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Snackbar.make(
                        binding.root,
                        "Authentication Failed: ${task.exception?.message}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            if (currentUser.isEmailVerified) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Snackbar.make(binding.root, "Please activate your email", Snackbar.LENGTH_LONG)
                    .show()
                auth.signOut()
            }
        }
    }
}