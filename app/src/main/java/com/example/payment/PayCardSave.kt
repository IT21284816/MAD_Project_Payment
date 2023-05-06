package com.example.payment

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PayCardSave : AppCompatActivity() {

    private lateinit var inputCardNum: EditText
    private lateinit var inputExpireyDate: EditText
    private lateinit var inputCardCVV: EditText
    private lateinit var inputCardName: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {

        //title hide
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_card_save)

        inputCardNum = findViewById(R.id.inputCardNum)
        inputExpireyDate = findViewById(R.id.inputExpireyDate)
        inputCardCVV = findViewById(R.id.inputCardCVV)
        inputCardName = findViewById(R.id.inputCardName)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Payment")

        btnSaveData.setOnClickListener {
            saveCardData()
        }
    }

    private fun saveCardData() {

        //getting values
        val cardNum = inputCardNum.text.toString()
        val expireyDate = inputExpireyDate.text.toString()
        val cardCVV = inputCardCVV.text.toString()
        val cardName = inputCardName.text.toString()

        if (cardNum.isEmpty()) {
            inputCardNum.error = "Please enter Card Number"
        }
        if (expireyDate.isEmpty()) {
            inputExpireyDate.error = "Please enter Expirey Date"
        }
        if (cardCVV.isEmpty()) {
            inputCardCVV.error = "Please enter CVV"
        }
        if (cardName.isEmpty()) {
            inputCardName.error = "Please enter Name"
        }
        else
        {

        val cardId = dbRef.push().key!!

        val employee = PaymentModel(cardId, cardNum, expireyDate, cardCVV, cardName)

        dbRef.child(cardId).setValue(employee)
            .addOnCompleteListener {
                Toast.makeText(this, "Card saved successfully", Toast.LENGTH_LONG).show()

                inputCardNum.text.clear()
                inputExpireyDate.text.clear()
                inputCardCVV.text.clear()
                inputCardName.text.clear()

                // Create an intent to start the MainActivity
                val intent = Intent(this, MainActivity_Payment::class.java)
                startActivity(intent)


            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }
        }

    }

}