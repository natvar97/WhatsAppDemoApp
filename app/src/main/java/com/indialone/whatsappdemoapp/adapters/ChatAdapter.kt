package com.indialone.whatsappdemoapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.indialone.whatsappdemoapp.databinding.SampleReceiverBinding
import com.indialone.whatsappdemoapp.databinding.SampleSenderBinding
import com.indialone.whatsappdemoapp.models.MessageModel

class ChatAdapter(
    private val messages: ArrayList<MessageModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val SENDER_VIEW_TYPE = 1
    val RECEIVER_VIEW_TYPE = 2

    class ReceiverViewHolder(itemView: SampleReceiverBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val tvReceiverMessage = itemView.tvReceiverMessage
        private val tvReceiverTime = itemView.tvReceiverTime

        fun bind(message: MessageModel) {
            tvReceiverMessage.text = message.message
            tvReceiverTime.text = "${message.timeStamp}"
        }

    }

    class SenderViewHolder(itemView: SampleSenderBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val tvSenderMessage = itemView.tvSenderMessage
        private val tvSenderTime = itemView.tvSenderTime

        fun bind(message: MessageModel) {
            tvSenderMessage.text = message.message
            tvSenderTime.text = "${message.timeStamp}"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == SENDER_VIEW_TYPE) {
            val view =
                SampleSenderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return SenderViewHolder(view)
        } else {
            val view =
                SampleReceiverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ReceiverViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages.get(position)

        when (holder) {
            is SenderViewHolder -> {
                holder.bind(message)
            }
            is ReceiverViewHolder -> {
                holder.bind(message)
            }
        }

    }


    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        if (messages[position].uid == FirebaseAuth.getInstance().uid) {
            return SENDER_VIEW_TYPE
        } else {
            return RECEIVER_VIEW_TYPE
        }
    }

}