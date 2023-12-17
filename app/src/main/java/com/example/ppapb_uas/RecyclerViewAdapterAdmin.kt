package com.example.ppapb_uas

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class RecyclerViewAdapterAdmin(private val itemList : ArrayList<Item>) : RecyclerView
    .Adapter<RecyclerViewAdapterAdmin.MyViewHolder>() {

    override fun getItemCount(): Int {
        return itemList.size
    }

    class MyViewHolder(itemList : View) : RecyclerView.ViewHolder(itemList){
        val title : TextView = itemList.findViewById(R.id.eachItemTextOneAdmin)
        val author : TextView = itemList.findViewById(R.id.eachItemTextTwoAdmin)
        val image : ImageView = itemList.findViewById(R.id.eachItemImgViewAdmin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item_admin,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.title.text = currentItem.title
        holder.author.text = currentItem.author

        // Load and display the image using Glide
        Glide.with(holder.itemView.context)
            .load(currentItem.imageUrl)
            .skipMemoryCache(true) // Skip caching in memory
            .diskCacheStrategy(DiskCacheStrategy.NONE) // Skip caching on disk
            .into(holder.image)

        holder.itemView.findViewById<ImageView>(R.id.eachItemEditButton).setOnClickListener{
            val intent = Intent(holder.itemView.context, AdminListUpdate::class.java)
            intent.putExtra("title", currentItem.title)
            intent.putExtra("author", currentItem.author)
            intent.putExtra("description", currentItem.description)
            intent.putExtra("imgId", currentItem.imageUrl)
            holder.itemView.context.startActivity(intent)
        }

        holder.itemView.findViewById<ImageView>(R.id.eachItemDeleteButton).setOnClickListener {
            val itemToDelete = Uri.parse(itemList[position].imageUrl.toString()).lastPathSegment?.removePrefix("images/")

            // Remove the item from the list
            itemList.removeAt(position)

            // Notify the adapter of the data change
            notifyDataSetChanged()

            // Delete the corresponding data from the Realtime Database
            deleteItemFromDatabase(itemToDelete.toString())
        }

    }

    private fun deleteItemFromDatabase(imgId: String) {
        // Reference to the Firebase Storage
        val storageReference = FirebaseStorage.getInstance().getReference("images").child(imgId)

        // Delete the image from Firebase Storage
        storageReference.delete().addOnSuccessListener {
            // Image deleted successfully, now delete the corresponding data from the Realtime Database
            val database = FirebaseDatabase.getInstance().getReference("Admin")
            database.child(imgId).removeValue()
                .addOnCompleteListener {
                    // Handle success if needed
                }
                .addOnFailureListener {
                    // Handle failure if needed
                }
        }.addOnFailureListener {
            // Handle failure if the image deletion fails
            Log.e("RecyclerViewAdapterAdmin", "Error deleting image: ${it.message}")
        }
    }


}