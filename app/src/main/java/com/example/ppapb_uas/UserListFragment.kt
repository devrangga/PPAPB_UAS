package com.example.ppapb_uas

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ppapb_uas.database.Note
import com.example.ppapb_uas.database.NoteDao
import com.example.ppapb_uas.database.NoteRoomDatabase
import com.example.ppapb_uas.databinding.FragmentUserListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserListFragment : Fragment() {
    private lateinit var binding : FragmentUserListBinding
    private lateinit var database : DatabaseReference
    private lateinit var recyclerViewItem : RecyclerView
    private lateinit var itemAdapter : RecyclerViewAdapterUser
    private lateinit var itemList : ArrayList<Item>
    private lateinit var searchView : androidx.appcompat.widget.SearchView

    private lateinit var dao: NoteDao



    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserListBinding.inflate(layoutInflater)

        recyclerViewItem = binding.userRecyclerView
        recyclerViewItem.setHasFixedSize(true)
        recyclerViewItem.layoutManager = LinearLayoutManager(requireActivity())

        itemAdapter = RecyclerViewAdapterUser(emptyList())
        recyclerViewItem.adapter = itemAdapter


        //SEARCHHHH
//        RecyclerViewAdapterUser.initAdapterInstance(itemAdapter)
//        searchView = binding.userSearch
//        searchView.clearFocus()
//        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                // Handle query submission
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                val filteredList : ArrayList<Item> = arrayListOf()
//                for (item : Item in itemList){
//                    if (item.title!!.lowercase().contains(newText!!.lowercase())){
//                        filteredList.add(item)
//                    }
//                }
//
//                if(filteredList.isEmpty()){
//                    Toast.makeText(requireActivity(),"No Data Found",Toast.LENGTH_SHORT).show()
//                }else {
//                    RecyclerViewAdapterUser.setFilteredList(filteredList)
//                }
//                return true
//            }
//        })




        // Initialize Room database
        dao = NoteRoomDatabase.getDatabase(requireContext()).dao()

        // Initialize Firebase reference
        database = FirebaseDatabase.getInstance().getReference("Admin")

        // Fetch data from Firebase and update itemList
        fetchFilmFromFirebase()


        // Observe changes in the LiveData from Room and update the adapter
        dao.getAllFilm().observe(viewLifecycleOwner, Observer { films ->
            // Log the data
            for (film in films) {
                Log.d(
                    "FilmData",
                    "ID: ${film.id}, Title: ${film.title}, Author: ${film.author}, Description: ${film.description}, ImageURL: ${film.imageUrl}"
                )
            }

            // Update the adapter
            itemAdapter.updateData(films)
        })

        return binding.root
    }

    private fun fetchFilmFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val filmList = mutableListOf<Note>()

                for (dataSnapshot in snapshot.children) {
                    val filmEntity = dataSnapshot.getValue(Note::class.java)
                    filmEntity?.let { filmList.add(it) }
                }

                // Update Room database with the new data from Firebase
                GlobalScope.launch(Dispatchers.IO) {
                    dao.deleteAllFilm()
                    dao.insertFilm(filmList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error if needed
            }
        })

        // Observe changes in the LiveData from Room and update the adapter
        dao.getAllFilm().observe(viewLifecycleOwner, Observer { films ->
            itemAdapter.updateData(films)
        })




    }










    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}