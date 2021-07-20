package com.indialone.whatsappdemoapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.indialone.whatsappdemoapp.R
import com.indialone.whatsappdemoapp.adapters.ChatAdapter
import com.indialone.whatsappdemoapp.databinding.ActivityChatDetailBinding
import com.indialone.whatsappdemoapp.models.MessageModel
import java.util.*
import kotlin.collections.ArrayList

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

        val senderId = mFirebaseAuth.uid
        val receiverId = intent.getStringExtra("userId")
        val userName = intent.getStringExtra("userName")
        val profileImage = intent.getStringExtra("profilePic")

        mBinding.tvUsename.text = userName
        Glide.with(this)
            .load(profileImage)
            .placeholder(R.drawable.place_holder)
            .error(R.drawable.place_holder)
            .into(mBinding.ivProfile)

        mBinding.ivBack.setOnClickListener {
            finish()
        }

        val messages = ArrayList<MessageModel>()
        val chatAdapter = ChatAdapter(messages, receiverId!!)
        mBinding.chatRecyclerview.adapter = chatAdapter
        mBinding.chatRecyclerview.layoutManager = LinearLayoutManager(this)

        val senderRoom = senderId + receiverId
        val receiverRoom = receiverId + senderId

        mFirebaseDatabase.reference.child("chats")
            .child(senderRoom)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messages.clear()
                    for (snapShot1 in snapshot.children) {
                        val model = snapShot1.getValue(MessageModel::class.java)
                        model!!.messageId = snapShot1.key!!
                        Log.e("key", snapShot1.key!!)
                        Log.e("messageId", model.messageId)
                        messages.add(model)
                    }
                    chatAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })



        mBinding.btnSend.setOnClickListener {

            val message = mBinding.etMessage.text.toString().trim { it <= ' ' }
            val timeStamp = Date().time
            val model = MessageModel(senderId!!, message, timeStamp)
            mBinding.etMessage.setText("")
            mFirebaseDatabase.reference.child("chats")
                .child(senderRoom)
                .push()
                .setValue(model)
                .addOnSuccessListener {
                    mFirebaseDatabase.reference.child("chats")
                        .child(receiverRoom)
                        .push()
                        .setValue(model)
                        .addOnSuccessListener {

                        }
                }


        }

    }
}