package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class Sign_Up : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val signUpButton = findViewById<Button>(R.id.signUp_Button)
        val logInButton = findViewById<Button>(R.id.RedirectLogInPageBtn)
        val rePasswordField = findViewById<EditText>(R.id.enterRePassword)
        val phoneField = findViewById<EditText>(R.id.enterPhone)
        val nameField = findViewById<EditText>(R.id.enterName)
        val emailField = findViewById<EditText>(R.id.enterEmail)
        val passwordField = findViewById<EditText>(R.id.enterPassword)

        signUpButton.setOnClickListener {
            val password = passwordField.text.toString()
            val name = nameField.text.toString()
            val phone = phoneField.text.toString()
            val email = emailField.text.toString()
            val rePassword = rePasswordField.text.toString()

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else if (password != rePassword) {
                Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, FirstPage::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Sign Up Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        logInButton.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
        }
    }
}
