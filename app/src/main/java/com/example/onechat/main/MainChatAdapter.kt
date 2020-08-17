package com.example.onechat.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.onechat.R
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

    // 리스트에 채팅 메시지를 추가한다.
    fun addItem(chatItem: ChatItem){
        chatItems.add(chatItem)
        // 어답터에 아이템 변경 사항을 알려준다.
        notifyItemChanged(chatItems.size -1)
    }

    class ChatViewHolder(view: View): RecyclerView.ViewHolder(view){
        var binding = DataBindingUtil.bind<ItemChattingBinding>(view)

        init{
            binding!!.root.setOnClickListener{
                Toast.makeText(view.context, "$adapterPosition", Toast.LENGTH_SHORT).show()
            }
        }
    }
}