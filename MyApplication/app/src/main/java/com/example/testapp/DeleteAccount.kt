package com.example.testapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DeleteAccount : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var databaseRef: DatabaseReference

    lateinit var bcktohome: ImageButton
    lateinit var yesbtn: Button
    lateinit var nobtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_delete_account)

        databaseRef = FirebaseDatabase.getInstance().getReference()
        firebaseAuth = FirebaseAuth.getInstance()

        bcktohome = findViewById(R.id.back_to_home)
        yesbtn = findViewById(R.id.btnyes)
        nobtn = findViewById(R.id.btnno)

        bcktohome.setOnClickListener {
            intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        yesbtn.setOnClickListener {
            databaseRef.child("Users").child(firebaseAuth.currentUser?.uid.toString()).removeValue()
            intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }

        nobtn.setOnClickListener{
            intent = Intent(this, MyProfile::class.java)
            startActivity(intent)
            finish()
        }


    }
}