package com.indialone.whatsappdemoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.indialone.whatsappdemoapp.databinding.ActivityChatDetailBinding

class ChatDetailActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityChatDetailBinding
    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var mFirebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityChatDetailBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        supportActionBar!!.hide()

        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mFirebaseAuth = FirebaseAuth.getInstance()

        val userId = mFirebaseAuth.uid
        val receiveId = intent.getStringExtra("userId")
        val userName = intent.getStringExtra("userName")
        val profileImage = intent.getStringExtra("profilePic")

        mBinding.tvUsename.text = userName
        Glide.with(this)
            .load(profileImage)
            .placeholder(R.drawable.place_holder)
            .error(R.drawable.place_holder)
            .into(mBinding.ivProfile)

    }
}