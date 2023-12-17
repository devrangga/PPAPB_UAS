package com.example.ppapb_uas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.ppapb_uas.databinding.ActivityUserMainMenuBinding

class UserMainMenu : AppCompatActivity() {
    private lateinit var binding : ActivityUserMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(UserListFragment())

        binding.bottomNavbar.setOnItemSelectedListener{
            when(it.itemId){
                R.id.userMainListtt -> replaceFragment(UserListFragment())
                R.id.userProfileee -> replaceFragment(UserProfileFragment())
                else -> {}
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}