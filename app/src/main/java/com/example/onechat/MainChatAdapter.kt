package com.example.onechat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.onechat.databinding.ItemChattingBinding

class MainChatAdapter(var chatItems: ArrayList<ChatItem>): RecyclerView.Adapter<MainChatAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        var view = inflater.inflate(R.layout.item_chatting, parent, false)
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chatItems.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.binding?.tvMessage?.text = chatItems[position].message
    }

    class ChatViewHolder(view: View): RecyclerView.ViewHolder(view){
        var binding = DataBindingUtil.bind<ItemChattingBinding>(view)
    }
}