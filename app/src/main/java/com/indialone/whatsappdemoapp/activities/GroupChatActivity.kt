package com.indialone.whatsappdemoapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.indialone.whatsappdemoapp.adapters.ChatAdapter
import com.indialone.whatsappdemoapp.databinding.ActivityGroupChatBinding
import com.indialone.whatsappdemoapp.models.MessageModel
import java.util.*
import kotlin.collections.ArrayList

class GroupChatActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityGroupChatBinding
    private lateinit var mFirebaseAuth: FirebaseAuth
    private lateinit var mFirebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityGroupChatBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        supportActionBar!!.hide()

        mBinding.ivBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        val messages = ArrayList<MessageModel>()
        val chatAdapter = ChatAdapter(messages)
        mBinding.chatRecyclerview.layoutManager = LinearLayoutManager(this)
        mBinding.chatRecyclerview.adapter = chatAdapter

        val senderId = mFirebaseAuth.uid
        mBinding.tvUsename.text = "Friends Group"


        mFirebaseDatabase.reference.child("groupChat")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messages.clear()
                    for (snapshot1 in snapshot.children) {
                        val model = snapshot1.getValue(MessageModel::class.java)
                        messages.add(model!!)
                    }
                    chatAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        mBinding.btnSend.setOnClickListener {
            val message = mBinding.etMessage.text.toString().trim { it <= ' ' }

            val model = MessageModel(senderId!!, message, Date().time)

            mBinding.etMessage.setText("")

            mFirebaseDatabase.reference.child("groupChat")
                .push()
                .setValue(model)
                .addOnSuccessListener {

                }

        }


    }
}