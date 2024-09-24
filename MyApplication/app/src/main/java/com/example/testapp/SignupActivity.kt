package com.example.testapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.testapp.DataClass.User
import com.example.testapp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference

        // Sign-up Button Listener
        binding.buttonSignUp.setOnClickListener {
            val name = binding.signupUsername.text.toString()
            val email = binding.signupEmail.text.toString()
            val mobile = binding.signupMobile.text.toString()
            val password = binding.signupPassword.text.toString()
            val repassword = binding.reSignupPassword.text.toString()

            // Debugging Toast
            Toast.makeText(this, "Attempting Sign-Up", Toast.LENGTH_SHORT).show()

            if (name.isNotEmpty() && email.isNotEmpty() && mobile.isNotEmpty() && password.isNotEmpty() && repassword.isNotEmpty()) {
                // Check if passwords match
                if (password == repassword) {
                    signupuser(name, email, mobile, password)
                } else {
                    Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all the details!", Toast.LENGTH_SHORT).show()
            }
        }

        // Direct to Login Listener
        binding.directToLogin.setOnClickListener {
            directToLogin()
        }
    }

    // Firebase Signup Method
    private fun signupuser(
        name: String,
        email: String,
        mobile: String,
        password: String
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val uid = firebaseAuth.currentUser?.uid
                    if (uid != null) {
                        addUserToDatabase(name, email, uid, password, mobile)
                    }
                    Toast.makeText(this, "Sign-Up Success", Toast.LENGTH_SHORT).show()
                    logToHome()
                } else {
                    // Sign-Up Failure Toast
                    Toast.makeText(
                        this, "Sign-Up Failed: ${task.exception?.message}", Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    // Add User to Firebase Realtime Database
    private fun addUserToDatabase(
        name: String, email: String, uid: String, password: String, mobile: String
    ) {
        val user = User(name, email, mobile, password, uid)
        databaseRef.child("Users").child(uid).setValue(user)
    }

    // Direct to Login Activity
    private fun directToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Direct to Home Activity
    private fun logToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
