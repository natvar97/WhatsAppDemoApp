package com.indialone.whatsappdemoapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.indialone.whatsappdemoapp.databinding.ActivitySignInBinding
import com.indialone.whatsappdemoapp.models.User

class SignInActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivitySignInBinding
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mProgressDialog: ProgressDialog
    private lateinit var mGoogleSignInClient : GoogleSignInClient
    private lateinit var mFirebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        supportActionBar!!.hide()

        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mProgressDialog = ProgressDialog(this)
        mProgressDialog.setTitle("Signing In")
        mProgressDialog.setMessage("Logging into your account")

        // google signin
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)



        mBinding.btnSignin.setOnClickListener {
            val email = mBinding.etEmail.text.toString().trim { it <= ' ' }
            val password = mBinding.etPassword.text.toString().trim { it <= ' ' }

            mProgressDialog.show()

            mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    mProgressDialog.dismiss()
                    if (task.isSuccessful) {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            task.exception!!.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        mBinding.tvDoNotHaveAccount.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        mBinding.btnGoogle.setOnClickListener {
            signIn()
        }

        if (mFirebaseAuth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    val RC_SIGN_IN = 65

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mFirebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    val user = mFirebaseAuth.currentUser

                    val users = User()
                    users.userId = user!!.uid
//                    users.email = user.email!!
                    users.userName = user.displayName!!
                    users.profileImage = user.photoUrl!!.toString()
                    mFirebaseDatabase.reference.child("users").child(user.uid).setValue(users)

                    startActivity(Intent(this , MainActivity::class.java))
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    Snackbar.make(mBinding.root, "Authentication Failed", Snackbar.LENGTH_LONG).show()
//                    updateUI(null)
                }
            }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mFirebaseAuth.currentUser
//        updateUI(currentUser)
    }

}