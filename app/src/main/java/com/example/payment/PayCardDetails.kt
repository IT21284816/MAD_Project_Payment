package com.example.payment
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class PayCardDetails : AppCompatActivity() {


    private lateinit var txtCardNum: TextView
    private lateinit var txtExpireyDate: TextView
    private lateinit var txtCardCVV: TextView
    private lateinit var txtCardName: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private lateinit var btnPay: Button


    override fun onCreate(savedInstanceState: Bundle?) {

        //title hide
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_details)

        btnPay = findViewById(R.id.btnPay)

        btnPay.setOnClickListener {
            val intent = Intent(this, PaymentComplete::class.java)
            startActivity(intent)
        }

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("cardId").toString(),
                intent.getStringExtra("cardNum").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("cardId").toString()
            )
        }

    }

    private fun initView() {
        txtCardName = findViewById(R.id.txtCardName)
        txtCardNum = findViewById(R.id.txtCardNum)
        txtExpireyDate = findViewById(R.id.txtExpireyDate)
        txtCardCVV = findViewById(R.id.txtCardCVV)


        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {

        txtCardNum.text = intent.getStringExtra("cardNum")
        txtExpireyDate.text = intent.getStringExtra("expireyDate")
        txtCardCVV.text = intent.getStringExtra("cardCVV")
        txtCardName.text = intent.getStringExtra("cardName")

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Payment").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Payment data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, SaveCardList::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun openUpdateDialog(
        cardId: String,
        cardNum: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.activity_card_update, null)

        mDialog.setView(mDialogView)

        val inputCardNum = mDialogView.findViewById<EditText>(R.id.inputCardNum)
        val inputExpireyDate = mDialogView.findViewById<EditText>(R.id.inputExpireyDate)
        val inputCardCVV = mDialogView.findViewById<EditText>(R.id.inputCardCVV)
        val inputCardName = mDialogView.findViewById<EditText>(R.id.inputCardName)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        inputCardNum.setText(intent.getStringExtra("cardNum").toString())
        inputExpireyDate.setText(intent.getStringExtra("expireyDate").toString())
        inputCardCVV.setText(intent.getStringExtra("cardCVV").toString())
        inputCardName.setText(intent.getStringExtra("cardName").toString())

        mDialog.setTitle("Updating $cardNum Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateEmpData(
                cardId,
                inputCardNum.text.toString(),
                inputExpireyDate.text.toString(),
                inputCardCVV.text.toString(),
                inputCardName.text.toString()
            )

            Toast.makeText(applicationContext, "Payment Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            txtCardNum.text = inputCardNum.text.toString()
            txtExpireyDate.text = inputExpireyDate.text.toString()
            txtCardCVV.text = inputCardCVV.text.toString()
            txtCardName.text = inputCardName.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateEmpData(
        id: String,
        num: String,
        date: String,
        cvv: String,
        name: String

    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Payment").child(id)
        val cardInfo = PaymentModel(id, num, date, cvv,name)
        dbRef.setValue(cardInfo)
    }

}