package com.example.ppapb_uas

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.transition.Transition
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.example.ppapb_uas.databinding.ActivityAdminListAddBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.*

class AdminListAdd : AppCompatActivity() {

    private lateinit var binding: ActivityAdminListAddBinding
    private lateinit var database: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri

    private val channelId = "SimpleNotificationChannel"
    private val notificationId = 1

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri
                binding.imageViewAdd.setImageURI(uri)
                // Optionally, you can call uploadData(imageUri) here if needed
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminListAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.adminListAddButton.setOnClickListener {
            uploadData(imageUri)
        }

        binding.adminListAddImage.setOnClickListener {
            getContent.launch("image/*")
        }
    }

        private fun uploadData(imageUri: Uri? = null) {
        val title: String = binding.adminListAddTitle.text.toString()
        val author: String = binding.adminListAddAuthor.text.toString()
        val description: String = binding.adminListAddDescription.text.toString()

        val imageId = UUID.randomUUID().toString()

        if (title.isNotEmpty() && author.isNotEmpty() && description.isNotEmpty() && imageUri != null) {
            // Generate a unique ID for the image

            // Upload image to Firebase Storage with the generated ID
            storageReference = FirebaseStorage.getInstance().reference.child("images/$imageId")
            val uploadTask: UploadTask = storageReference.putFile(imageUri)

            uploadTask.addOnSuccessListener {
                // Image uploaded successfully, now get the download URL
                storageReference.downloadUrl.addOnSuccessListener { imageUrl ->
                    val item = Item(title, author, description, imageUrl.toString())
                    database = FirebaseDatabase.getInstance().getReference("Admin")
                    database.child(imageId).setValue(item)
                        .addOnCompleteListener {
                            binding.adminListAddTitle.text!!.clear()
                            binding.adminListAddAuthor.text!!.clear()
                            binding.adminListAddDescription.text!!.clear()


                            startActivity(Intent(this,AdminListMain::class.java))
                            Toast.makeText(this, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Adding Data Failed!", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Image Upload Failed!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show()
        }
    }
}
