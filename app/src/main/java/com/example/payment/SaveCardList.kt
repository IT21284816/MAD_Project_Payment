package com.example.payment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class SaveCardList : AppCompatActivity() {

    private lateinit var cardRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var cardList: ArrayList<PaymentModel>
    private lateinit var dbRef: DatabaseReference

    private lateinit var searchView: SearchView
    private lateinit var mAdapter: PayAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        //title hide
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_savecard_list)

        cardRecyclerView = findViewById(R.id.rvEmp)
        cardRecyclerView.layoutManager = LinearLayoutManager(this)
        cardRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        cardList = arrayListOf<PaymentModel>()

        //search
        searchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filter the list based on the search query
                filterList(newText)
                return true
            }
        })


        getPaymentData()
    }


    private fun filterList(query: String?) {
        val filteredList = ArrayList<PaymentModel>()
        for (item in cardList) {
            if (item.cardNum?.contains(query.orEmpty(), ignoreCase = true) == true) {
                filteredList.add(item)
            }
        }
        val mAdapter = PayAdapter(filteredList)
        cardRecyclerView.adapter = mAdapter

        mAdapter.setOnItemClickListener(object : PayAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {

                val intent = Intent(this@SaveCardList, PayCardDetails::class.java)

                //put extras
                intent.putExtra("cardId", filteredList[position].cardId)
                intent.putExtra("cardNum", filteredList[position].cardNum)
                intent.putExtra("expireyDate", filteredList[position].expireyDate)
                intent.putExtra("cardCVV", filteredList[position].cardCVV)
                intent.putExtra("cardName", filteredList[position].cardName)

                startActivity(intent)
            }

        })
    }


    private fun getPaymentData() {

        cardRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Payment")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cardList.clear()
                if (snapshot.exists()){
                    for (cardSnap in snapshot.children){
                        val cardData = cardSnap.getValue(PaymentModel::class.java)
                        cardList.add(cardData!!)
                    }
                    mAdapter = PayAdapter(cardList)
                    cardRecyclerView.adapter = mAdapter
                    setAdapterClickListener()

                    cardRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setAdapterClickListener() {
        mAdapter.setOnItemClickListener(object : PayAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {

                val intent = Intent(this@SaveCardList, PayCardDetails::class.java)

                //put extras
                intent.putExtra("cardId", cardList[position].cardId)
                intent.putExtra("cardNum", cardList[position].cardNum)
                intent.putExtra("expireyDate", cardList[position].expireyDate)
                intent.putExtra("cardCVV", cardList[position].cardCVV)
                intent.putExtra("cardName", cardList[position].cardName)

                startActivity(intent)
            }

        })
    }
}
