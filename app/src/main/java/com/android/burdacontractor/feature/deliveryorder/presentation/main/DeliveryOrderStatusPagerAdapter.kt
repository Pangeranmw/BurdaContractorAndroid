package com.android.burdacontractor.feature.deliveryorder.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.burdacontractor.core.domain.model.Constant

class DeliveryOrderStatusPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    val fragments: MutableList<Fragment> = mutableListOf()
    override fun createFragment(position: Int): Fragment {
        val fragment = DeliveryOrderFragment()
        fragment.arguments = Bundle().apply {
            putInt(Constant.ARG_SECTION_NUMBER, position + 1)
        }
        fragments.add(fragment)
        return fragment
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return 3
    }
}