package com.example.onechat.main

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onechat.ChatDatabaseHelper
import com.example.onechat.R
import com.example.onechat.SharedPreferenceUtil
import com.example.onechat.databinding.ActivityMainBinding
import com.example.onechat.login.LoginActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var isLogin: Boolean = false
    private var chatItems = ArrayList<ChatItem>()
    private lateinit var adapter: MainChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        isLogin = SharedPreferenceUtil.IsLogin(this)

        // DB에 저장된 채팅내용 가져오기
        chatItems = getChatItemsInDatabase()

        adapter = MainChatAdapter(chatItems)

        var linearLayoutManager = LinearLayoutManager(this)
        binding.rvChat.layoutManager = linearLayoutManager
        binding.rvChat.adapter = adapter

        binding.btnSend.isEnabled = false

        binding.etInput.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트 창이 비어있으면 버튼 비활성화
                binding.btnSend.isEnabled = !text.isNullOrEmpty()
            }
        })

        binding.btnSend.setOnClickListener{
            if(!isLogin){
                //게스트 상태로 로그인 요청
                Snackbar.make(binding.root, "로그인이 필요합니다.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("로그인"){
                        var startLoginActivityIntent = Intent(this, LoginActivity::class.java)
                        startActivityForResult(startLoginActivityIntent, LoginActivity.REQUEST_CODE)
                    }
                    .show()
            }
            else{
                //보내기를 눌렀을 경우 동작
                var message =  binding.etInput.text.toString()

                var chatItem = ChatItem(message)
                adapter.addItem(chatItem)

                //db에 저장한다.
                insertChatItemInDatabase(chatItem)

                // 입력 후에 내용 텍스트 초기화시키기
                binding.etInput.setText("")
                // 입력 후 스크롤 내리기
                binding.rvChat.smoothScrollToPosition(chatItems.size -1)
            }

        }

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

    // 데이터베이스에 chatItem을 추가하기
    fun insertChatItemInDatabase(chatItem: ChatItem){
        var db = ChatDatabaseHelper(this).writableDatabase
        db.execSQL("INSERT INTO ${ChatDatabaseHelper.TABLE_CHAT} (message) VALUES('${chatItem.message}')")
        db.close()
    }

    fun getChatItemsInDatabase(): ArrayList<ChatItem>{
        var db = ChatDatabaseHelper(this).readableDatabase
        var cursor = db.rawQuery("SELECT * FROM ${ChatDatabaseHelper.TABLE_CHAT}", null)

        var chatItems = ArrayList<ChatItem>()
        while(cursor.moveToNext()){
            var message = cursor.getString(1)
            var chatItem = ChatItem(message)
            chatItems.add(chatItem)
        }
        cursor.close()
        db.close()

        return chatItems
    }
}
