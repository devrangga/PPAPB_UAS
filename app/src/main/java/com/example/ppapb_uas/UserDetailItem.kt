package com.example.ppapb_uas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ppapb_uas.databinding.ActivityUserDetailItemBinding

class UserDetailItem : AppCompatActivity() {
    private lateinit var binding : ActivityUserDetailItemBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailItemBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val detailTitle = binding.detailTitle
        val detailAuthor = binding.detailAuthor
        val detailDescription = binding.detailDescription

        val originalImageUrl = intent.getStringExtra("imgId")
        Glide.with(this)
            .load(originalImageUrl)
            .skipMemoryCache(true) // Skip caching in memory
            .diskCacheStrategy(DiskCacheStrategy.NONE) // Skip caching on disk
            .into(binding.detailImgView)

        detailTitle.setText(intent.getStringExtra("title"))
        detailAuthor.setText(intent.getStringExtra("author"))
        detailDescription.setText(intent.getStringExtra("description"))

        binding.detailBackButton.setOnClickListener{
            startActivity(Intent(this@UserDetailItem,UserMainMenu::class.java))
        }

    }
}