package com.indialone.whatsappdemoapp.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.indialone.whatsappdemoapp.databinding.ActivitySettingsBinding
import com.indialone.whatsappdemoapp.models.User

class SettingsActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivitySettingsBinding
    private lateinit var mFirebaseStorage: FirebaseStorage
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private val IMAGE_REQUEST_CODE = 33

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        supportActionBar!!.hide()

        mBinding.ivBack.setOnClickListener {
            finish()
        }

        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mFirebaseStorage = FirebaseStorage.getInstance()

        mFirebaseDatabase.reference.child("users")
            .child(mFirebaseAuth.uid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    Glide.with(this@SettingsActivity)
                        .load(user!!.profileImage)
                        .centerCrop()
                        .into(mBinding.ivProfileImage)

                    mBinding.etUsername.setText(user.userName)
                    mBinding.etStatus.setText(user.about)

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        mBinding.ivAddImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_REQUEST_CODE)
        }

        mBinding.btnSave.setOnClickListener {
            val status = mBinding.etStatus.text.toString().trim { it <= ' ' }
            val userName = mBinding.etUsername.text.toString().trim { it <= ' ' }

            val userHashmap = HashMap<String, Any>()
            userHashmap.put("userName", userName)
            userHashmap.put("about", status)

            mFirebaseDatabase.reference.child("users")
                .child(mFirebaseAuth.uid!!)
                .updateChildren(userHashmap)

            Toast.makeText(this, "Your details saved successfully", Toast.LENGTH_SHORT).show()

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_REQUEST_CODE) {
                if (data!!.data != null) {

                    val sFile = data.data
                    Glide.with(this)
                        .load(sFile)
                        .centerCrop()
                        .into(mBinding.ivProfileImage)

                    val sRef: StorageReference =
                        mFirebaseStorage.reference.child("profile_pictures")
                            .child(FirebaseAuth.getInstance().uid!!)

                    sRef.putFile(sFile!!).addOnSuccessListener {
                        sRef.downloadUrl.addOnSuccessListener {
                            mFirebaseDatabase.reference.child("users")
                                .child(mFirebaseAuth.uid!!)
                                .child("profileImage")
                                .setValue(it.toString())
                        }
                    }

                }
            }
        }
    }

}