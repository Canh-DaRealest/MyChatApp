package com.canhmai.chatapp.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.canhmai.chatapp.fragment.LoginFragment
import com.canhmai.chatapp.fragment.SignUpFragment

class ViewPager(owner: AppCompatActivity) : FragmentStateAdapter(owner) {


        val listTitle: List<String> = listOf("Đăng nhập", "Đăng ký")

        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {

                0 -> LoginFragment.newInstance()


                else -> SignUpFragment.newInstance()


            }

        }
    }