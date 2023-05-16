package com.example.payment

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class PaymentComplete : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        //title hide
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_complete)
    }
}