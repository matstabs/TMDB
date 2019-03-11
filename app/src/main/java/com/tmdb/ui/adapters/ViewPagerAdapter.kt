package com.tmdb.ui.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.arellomobile.mvp.MvpAppCompatFragment

class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

    private val fragmentList = ArrayList<MvpAppCompatFragment>()
    private val fragmentTitleList = ArrayList<String>()

    override fun getItem(position: Int) = fragmentList[position]

    override fun getCount() = fragmentList.size

    override fun getPageTitle(position: Int) = fragmentTitleList[position]

    fun addFragment(fragment: MvpAppCompatFragment, title: String) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }

}