package com.example.onechat.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.onechat.BuildConfig
import com.example.onechat.main.MainActivity
import com.example.onechat.R
import com.example.onechat.SharedPreferenceUtil
import com.example.onechat.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.tvVersion.text = "(v${BuildConfig.VERSION_NAME})"

        setResult(Activity.RESULT_CANCELED)

        binding.btnLogin.setOnClickListener {

            // 로그인을 저장한다.
            SharedPreferenceUtil.putIsLogin(this@LoginActivity, true)

            // 결과를 설정한다.
            setResult(Activity.RESULT_OK)
            finish()
        }
        // 게스트 로그인 버튼 누르면 실행하기
        binding.btnGuest.setOnClickListener (object: View.OnClickListener{
            override fun onClick(p0: View){
                SharedPreferenceUtil.putIsLogin(this@LoginActivity, false)

                // 결과를 설정한다.
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        })
    }
    companion object{
        const val REQUEST_CODE = 1
    }
}
