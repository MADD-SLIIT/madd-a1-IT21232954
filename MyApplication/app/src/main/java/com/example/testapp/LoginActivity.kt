package com.example.testapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.testapp.databinding.ActivityLoginBinding
import com.example.testapp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference

        binding.buttonDirectToSignup.setOnClickListener {
            directToSignup()
        }

        binding.buttonSignIn.setOnClickListener{
            val email = binding.loginemail.text.toString()
            val password = binding.loginPassword.text.toString()

            // Debugging Toast
            Toast.makeText(this, "Attempting Login", Toast.LENGTH_SHORT).show()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginuser(email, password)
            } else {
                Toast.makeText(this, "Please fill in all the details!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginuser(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful){
                    val uid = firebaseAuth.currentUser?.uid
                    if (uid != null){
                        fetchuserdata(uid)
                    }
                    Toast.makeText(this, "Login Success!", Toast.LENGTH_SHORT).show()
                    loginToHome()
                }else{
                    Toast.makeText(this, "Login Failed! :${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun loginToHome() {
        val intent = Intent(this, HomeActivity ::class.java)
        startActivity(intent)
        finish()
    }

    private fun fetchuserdata(uid: String) {
        databaseRef.child("Users").child(uid).get().addOnSuccessListener {
            if (it.exists()) {
                val name = it.child("name").value
                Toast.makeText(this, "Welcome, $name", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "User Data not Found!", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener{
            Toast.makeText(this, "Failed to Retrieve user data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun directToSignup() {
        val intent = Intent(this, SignupActivity ::class.java)
        startActivity(intent)
        finish()
    }
}
