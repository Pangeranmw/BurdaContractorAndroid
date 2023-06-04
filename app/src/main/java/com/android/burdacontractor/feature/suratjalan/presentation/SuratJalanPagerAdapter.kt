package com.android.burdacontractor.feature.suratjalan.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class SuratJalanPagerAdapter(fragmentManager: FragmentManager, lifeCycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifeCycle) {
//    override fun createFragment(position: Int): Fragment {
//        val fragment = SjPengirimanGpFragment()
//        fragment.arguments = Bundle().apply {
//            putInt(SjPengirimanGpFragment.ARG_SECTION_NUMBER, position + 1)
//        }
//        return fragment
//    }
//
//    override fun getItemCount(): Int {
//        return 3
//    }
    private val fragmentList = arrayListOf<Fragment>()
    private val titleList = arrayListOf<String>()

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun populateFragment(fragment: Fragment,title: String){
        fragmentList.add(fragment)
        titleList.add(title)
    }

    fun getPageTitle(position: Int) = titleList[position]
}