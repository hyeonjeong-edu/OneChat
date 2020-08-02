package com.example.onechat

import android.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.onechat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        var chatItems = ArrayList<ChatItem>
        for(idx in 1 .. 100){
            var chatItem = ChatItem("메시지 입니다. $idx")
            chatItem.add(chatItem)
        }
    }
}
