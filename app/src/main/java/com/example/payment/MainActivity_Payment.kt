package com.example.payment

import android.content.Intent
import  androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button

class MainActivity_Payment : AppCompatActivity() {

    private lateinit var btnSaveCard: Button
    private lateinit var btnPayNow: Button



    override fun onCreate(savedInstanceState: Bundle?) {

        //title hide
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)


        btnSaveCard = findViewById(R.id.btnSaveCard)
        btnPayNow = findViewById(R.id.btnPayNow)


        btnSaveCard.setOnClickListener {
            val intent = Intent(this, PayCardSave::class.java)
            startActivity(intent)
        }

        btnPayNow.setOnClickListener {
            val intent = Intent(this, SaveCardList::class.java)
            startActivity(intent)
        }



    }
}//