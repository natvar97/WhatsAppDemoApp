package com.indialone.whatsappdemoapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.indialone.whatsappdemoapp.R
import com.indialone.whatsappdemoapp.adapters.UsersAdapter
import com.indialone.whatsappdemoapp.databinding.FragmentChatsBinding
import com.indialone.whatsappdemoapp.models.User

class ChatsFragment : Fragment() {

    private lateinit var mBinding: FragmentChatsBinding
    private var list = ArrayList<User>()
    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var adapter : UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentChatsBinding.inflate(inflater, container, false)

        adapter = UsersAdapter(mBinding.root.context, list)
        mBinding.chatsRecyclerview.adapter = adapter
        mBinding.chatsRecyclerview.layoutManager = LinearLayoutManager(mBinding.root.context)

        mFirebaseDatabase = FirebaseDatabase.getInstance()
        loadChats()

        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        loadChats()
    }

    private fun loadChats() {
        mFirebaseDatabase.reference.child("users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (dataSnapShot in snapshot.children) {
                        val users = dataSnapShot.getValue(User::class.java)
                        users!!.userId = dataSnapShot.key!!
                        list.add(users)
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

}