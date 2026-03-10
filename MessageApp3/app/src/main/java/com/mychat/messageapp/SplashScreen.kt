package com.mychat.messageapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.mychat.messageapp.databinding.ActivitySplashScreenBinding

@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {
    private val splashScreen = 3000
    private lateinit var binding: ActivitySplashScreenBinding  // Düzgün tanımlama

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)  // Burada `binding`i doğru şekilde kullanıyoruz
        enableEdgeToEdge()
        setContentView(binding.root)
        supportActionBar?.hide()

        val animasyon1 = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.animasyon1)
        val imageView = binding.imageView
        imageView.animation = animasyon1

        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, splashScreen.toLong())
    }
}
