package com.example.onechat.login

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.onechat.BuildConfig
import com.example.onechat.MainActivity
import com.example.onechat.R
import com.example.onechat.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

       binding.tvVersion.text = "(v${BuildConfig.VERSION_NAME})"

        binding.btnLogin.setOnClickListener {
            // 로그인 엑티비티 --> 메인 덱티비티를 실행한다.
            var startMainActivityIntent = Intent( this@LoginActivity, MainActivity::class.java)
            startActivity(startMainActivityIntent)
            // 로그인 액티비티를 종료한다.
            finish()
        }
        // 게스트 로그인 버튼 누르면 실행하기
        binding.btnGuest.setOnClickListener {

        }
    }
}
