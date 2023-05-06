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

    private lateinit var empRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var empList: ArrayList<PaymentModel>
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

        empRecyclerView = findViewById(R.id.rvEmp)
        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        empList = arrayListOf<PaymentModel>()

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


        getEmployeesData()
    }


    private fun filterList(query: String?) {
        val filteredList = ArrayList<PaymentModel>()
        for (item in empList) {
            if (item.cardNum?.contains(query.orEmpty(), ignoreCase = true) == true) {
                filteredList.add(item)
            }
        }
        val mAdapter = PayAdapter(filteredList)
        empRecyclerView.adapter = mAdapter

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


    private fun getEmployeesData() {

        empRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Payment")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                empList.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val empData = empSnap.getValue(PaymentModel::class.java)
                        empList.add(empData!!)
                    }
                    mAdapter = PayAdapter(empList)
                    empRecyclerView.adapter = mAdapter
                    setAdapterClickListener()

                    empRecyclerView.visibility = View.VISIBLE
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
                intent.putExtra("cardId", empList[position].cardId)
                intent.putExtra("cardNum", empList[position].cardNum)
                intent.putExtra("expireyDate", empList[position].expireyDate)
                intent.putExtra("cardCVV", empList[position].cardCVV)
                intent.putExtra("cardName", empList[position].cardName)

                startActivity(intent)
            }

        })
    }
}
