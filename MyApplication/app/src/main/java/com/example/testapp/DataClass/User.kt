package com.example.testapp.DataClass

import android.provider.ContactsContract.CommonDataKinds.Email

class User {
    var name : String? = null
    var email : String? = null
    var mobile : String? = null
    var password : String? = null
    var uid : String? = null

    constructor()

    constructor(name: String, email:String, mobile: String, password: String, uid: String){
        this.name = name
        this.email = email
        this.mobile = mobile
        this.password = password
        this.uid = uid
    }
}