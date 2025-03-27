package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlin.jvm.java

class FirstPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_first_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val addNote = findViewById<Button>(R.id.addNote)
        val showNote = findViewById<Button>(R.id.showNote)
        val SignOutBtn = findViewById<Button>(R.id.SignOutBtn)

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.frameLayout, AddNoteFragement())
        transaction.commit()

        addNote.setOnClickListener {
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.frameLayout, AddNoteFragement())
            transaction.commit()
        }
        showNote.setOnClickListener {
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.frameLayout, ShowNoteFragment())
            transaction.commit()
        }

        SignOutBtn.setOnClickListener {
            val dialog = AlertDialog.Builder(it.context)
                .setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Yes") { _, _ ->
                    // Sign out from Firebase Auth
                    FirebaseAuth.getInstance().signOut()

                    // Sign out from Google Sign-In
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build()

                    GoogleSignIn.getClient(this, gso)
                        .signOut()
                        .addOnCompleteListener {
                            // Clear backstack to prevent returning to FirstPage
                            val intent = Intent(this, LogIn::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                }
                .setNegativeButton("No", null)
                .show()
        }
    }
}
