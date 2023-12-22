package com.example.ppapb_uas

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ppapb_uas.database.Note
import com.example.ppapb_uas.databinding.ActivityAdminListMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class AdminListMain : AppCompatActivity() {
    private lateinit var binding : ActivityAdminListMainBinding
    private lateinit var database : DatabaseReference
    private lateinit var itemAdapter : RecyclerViewAdapterAdmin
    private lateinit var itemList : ArrayList<Item>
    private lateinit var recyclerViewItem : RecyclerView
    private lateinit var searchView : androidx.appcompat.widget.SearchView

    private lateinit var auth : FirebaseAuth

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminListMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        binding.adminLogout.setOnClickListener{
            val sharedPreferences = this.getSharedPreferences("user_data", Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()


            auth.signOut()
            startActivity(Intent(this@AdminListMain,MainActivity::class.java))
        }

        recyclerViewItem = binding.adminRecyclerView
        recyclerViewItem.setHasFixedSize(true)
        recyclerViewItem.layoutManager = LinearLayoutManager(this)

        itemList = arrayListOf()
        itemAdapter = RecyclerViewAdapterAdmin(itemList)
        recyclerViewItem.adapter = itemAdapter

        RecyclerViewAdapterAdmin.initAdapterInstance(itemAdapter)
        searchView = binding.adminSearch
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle query submission
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList : ArrayList<Item> = arrayListOf()
                for (item : Item in itemList){
                    if (item.title!!.lowercase().contains(newText!!.lowercase())){
                        filteredList.add(item)
                    }
                }

                if(filteredList.isEmpty()){
                    Toast.makeText(this@AdminListMain,"No Data Found",Toast.LENGTH_SHORT).show()
                }else {
                    RecyclerViewAdapterAdmin.setFilteredList(filteredList)
                }
                return true
            }
        })

        with(binding){
            adminAddButton.setOnClickListener{
                startActivity(Intent(this@AdminListMain,AdminListAdd::class.java))
            }
        }

        database = FirebaseDatabase.getInstance().getReference("Admin")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the existing list
                itemList.clear()

                // Iterate through the snapshot and add items to the list
                for (dataSnapshot in snapshot.children) {
                    val item = dataSnapshot.getValue(Item::class.java)
                    if (item != null) {
                        itemList.add(item)
                    }
                }

                // Notify the adapter that the data has changed
                itemAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors if needed
                Toast.makeText(this@AdminListMain, "Data retrieval failed!", Toast.LENGTH_SHORT).show()
            }
        })


    }




}


