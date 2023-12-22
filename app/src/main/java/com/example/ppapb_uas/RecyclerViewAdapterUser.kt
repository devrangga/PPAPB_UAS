package com.example.ppapb_uas

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ppapb_uas.database.Note

class RecyclerViewAdapterUser(private var itemList: List<Note>) : RecyclerView
    .Adapter<RecyclerViewAdapterUser.MyViewHolder>() {

//    companion object {
//        private lateinit var adapter: RecyclerViewAdapterUser
//
//        fun initAdapterInstance(adapterInstance: RecyclerViewAdapterUser) {
//            adapter = adapterInstance
//        }
//
//        fun setFilteredList(filteredList: ArrayList<Item>) {
//            adapter.itemList = filteredList
//            adapter.notifyDataSetChanged() // Notify the adapter that the data has changed
//        }
//    }









    class MyViewHolder(itemList : View) : RecyclerView.ViewHolder(itemList){
        val title : TextView = itemList.findViewById(R.id.eachItemTextOneUser)
        val author : TextView = itemList.findViewById(R.id.eachItemTextTwoUser)
        val image : ImageView = itemList.findViewById(R.id.eachItemImgViewUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item_user,parent,false)
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

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context,UserDetailItem::class.java)
            intent.putExtra("title",currentItem.title)
            intent.putExtra("author",currentItem.author)
            intent.putExtra("description",currentItem.description)
            intent.putExtra("imgId", currentItem.imageUrl)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }



    fun updateData(newList: List<Note>) {
        itemList = newList
        notifyDataSetChanged()
    }






}