package com.example.testapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.testapp.DataClass.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditProfile : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var databaseRef: DatabaseReference
    lateinit var backtoHome: ImageButton
    lateinit var btnsave: Button

    lateinit var gname: EditText
    lateinit var gemail: EditText
    lateinit var gphone: EditText
    lateinit var gpassword: EditText

    lateinit var userlist: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)

        backtoHome = findViewById(R.id.back_to_home)
        btnsave = findViewById(R.id.btnsave)

        gname = findViewById(R.id.edtprofilename)
        gemail = findViewById(R.id.edtprofileemail)
        gphone = findViewById(R.id.edtprofilephone)
        gpassword = findViewById(R.id.edtprofilepasswprd)

        databaseRef = FirebaseDatabase.getInstance().getReference()
        firebaseAuth = FirebaseAuth.getInstance()

        userlist = ArrayList()

        retrieveuserprofile()

        backtoHome.setOnClickListener {
            intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnsave.setOnClickListener {
            updateprofile()
        }


    }

    private fun updateprofile() {
        val newname = gname.text.toString()
        val newemail = gemail.text.toString()
        val newphone = gphone.text.toString()
        val newpassword = gpassword.text.toString()

        databaseRef.child("Users").child(firebaseAuth.currentUser?.uid.toString())
            .setValue(
                User(
                    newname,
                    newemail,
                    newphone,
                    newpassword,
                    firebaseAuth.currentUser?.uid.toString()
                )
            )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this@EditProfile,
                        "Profile updated successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this, MyProfile::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@EditProfile, "Failed to update data!", Toast.LENGTH_SHORT)
                        .show()
                }

            }
    }

    private fun retrieveuserprofile() {
        val currentuser = firebaseAuth.currentUser
        currentuser?.let {
            val uid = it.uid
            databaseRef.child("Users").child(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val name = snapshot.child("name").value.toString()
                        val email = snapshot.child("email").value.toString()
                        val mobile = snapshot.child("mobile").value.toString()
                        val password = snapshot.child("password").value.toString()

                        findViewById<EditText>(R.id.edtprofilename).setText(name)
                        findViewById<EditText>(R.id.edtprofileemail).setText(email)
                        findViewById<EditText>(R.id.edtprofilephone).setText(mobile)
                        findViewById<EditText>(R.id.edtprofilepasswprd).setText(password)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            this@EditProfile,
                            "Failed to load Profile!: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }
}