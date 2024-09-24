package com.example.testapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.testapp.DataClass.User
import com.example.testapp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyProfile : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var databaseRef: DatabaseReference
    lateinit var sharedPreferences: SharedPreferences

    lateinit var backtoHome: ImageButton

    lateinit var nav_edtbtn: Button
    lateinit var nav_dltbtn: Button
    lateinit var logoutbtn: Button

    lateinit var gname: TextView
    lateinit var gemail: TextView
    lateinit var gphone: TextView

    lateinit var userlist: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_profile)

        backtoHome = findViewById(R.id.back_to_home)


        nav_edtbtn = findViewById(R.id.editprofilebtn)
        nav_dltbtn = findViewById(R.id.deleteaccbtn)
        logoutbtn = findViewById(R.id.logoutbtn)

        gname = findViewById(R.id.profilename)
        gemail = findViewById(R.id.profileemail)
        gphone = findViewById(R.id.profilephone)

        databaseRef = FirebaseDatabase.getInstance().getReference()
        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = this.getSharedPreferences("UserSession", Context.MODE_PRIVATE)

        userlist = ArrayList()

        val uid = firebaseAuth.currentUser?.uid.toString()
        listenforprofilechanges(uid)

        nav_edtbtn.setOnClickListener{
            intent = Intent(this, EditProfile::class.java)
            startActivity(intent)
            finish()
        }

        logoutbtn.setOnClickListener{
            logout()
        }

        backtoHome.setOnClickListener {
            intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        nav_dltbtn.setOnClickListener{
            intent = Intent(this, DeleteAccount::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun logout() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        val intent = Intent(this@MyProfile, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun listenforprofilechanges(uid: String) {
        databaseRef.child("Users").child(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
//                    val name = snapshot.getValue(User::class.java)
//                    val email = snapshot.getValue(User::class.java)
//                    val phone = snapshot.getValue(User::class.java)
                    val user = snapshot.getValue(User::class.java)

                    if (user != null) {
                        gname.text = user.name
                        gemail.text = user.email
                        gphone.text = user.mobile
                    } else {
                        Toast.makeText(this@MyProfile, "User Data not Found!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                     Toast.makeText(this@MyProfile, "Error fetching data: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
