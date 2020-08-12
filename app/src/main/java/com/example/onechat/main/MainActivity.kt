package com.example.onechat.main

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onechat.R
import com.example.onechat.SharedPreferenceUtil
import com.example.onechat.databinding.ActivityMainBinding
import com.example.onechat.login.LoginActivity

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var isLogin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        var chatItems = ArrayList<ChatItem>()
        for(idx in 1 .. 100){
            var chatItem = ChatItem("메시지 입니다. $idx")
            chatItems.add(chatItem)
        }

        var adapter = MainChatAdapter(chatItems)
        var linearLayoutManager = LinearLayoutManager(this)
        binding.rvChat.layoutManager = linearLayoutManager
        binding.rvChat.adapter = adapter

    }

    override fun onBackPressed() {
        var dialog = AlertDialog.Builder(this)
            .setTitle("확인")
            .setMessage("앱을 종료 할까요?")
            .setNegativeButton("취소", object: DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1:Int) {
                    // 취소 버튼을 눌렀을 경우
                    p0?.cancel()
                }
            })
            .setPositiveButton("종료"){
                dialogInterface, i ->
                finish()
            }
            .create()

        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            LoginActivity.REQUEST_CODE -> {
                //로그인이 정상적으로 되었는지 판단한다.
                if(resultCode === Activity.RESULT_OK)
                    Toast.makeText(this,"로그인을 성공했습니다.",Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this,"게스트로 실행합니다.",Toast.LENGTH_SHORT).show()
                isLogin = SharedPreferenceUtil.IsLogin(this)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_chat, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        var menuSign = menu?.findItem(R.id.menu_sign)
        menuSign?.title = if(isLogin) "로그아웃" else "로그인"
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.menu_sign -> {
                if(isLogin){
                    //로그아웃 요청
                    isLogin = false
                    SharedPreferenceUtil.putIsLogin(this, false)
                }
                 else{
                    //로그인 요청
                    var startLoginActivityIntent = Intent(this, LoginActivity::class.java)
                    startActivityForResult(startLoginActivityIntent, LoginActivity.REQUEST_CODE)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
