package com.indialone.whatsappdemoapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.indialone.whatsappdemoapp.databinding.ActivitySignUpBinding
import com.indialone.whatsappdemoapp.models.User

class SignUpActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivitySignUpBinding
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var mProgressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        supportActionBar!!.hide()

        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mProgressDialog = ProgressDialog(this)
        mProgressDialog.setTitle("Creating Account")
        mProgressDialog.setMessage("We are creating your account")

        mBinding.btnSignup.setOnClickListener {

            val email = mBinding.etEmail.text.toString().trim { it <= ' ' }
            val password = mBinding.etPassword.text.toString().trim { it <= ' ' }
            val userName = mBinding.etUsename.text.toString().trim { it <= ' ' }

            mProgressDialog.show()

            mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    mProgressDialog.dismiss()
                    if (task.isSuccessful) {

                        val user = User(userName = userName, email = email, password = password)
                        val id = task.result!!.user!!.uid

                        mFirebaseDatabase.reference.child("users").child(id).setValue(user)

                        Toast.makeText(this, "User Created Successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            this,
                            task.exception!!.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        mBinding.tvAlreadyHaveAccount.setOnClickListener {
            startActivity(Intent(this , SignInActivity::class.java))
        }


    }

}