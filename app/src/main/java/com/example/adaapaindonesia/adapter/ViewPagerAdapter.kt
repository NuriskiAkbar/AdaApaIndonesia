package com.example.adaapaindonesia.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.adaapaindonesia.view.onboarding.OnBoardingOneFragment
import com.example.adaapaindonesia.view.onboarding.OnBoardingThreeFragment
import com.example.adaapaindonesia.view.onboarding.OnBoardingTwoFragment

class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager){

    private val fragments = listOf(
        OnBoardingOneFragment(),
        OnBoardingTwoFragment(),
        OnBoardingThreeFragment()
    )
    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

}