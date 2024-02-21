package com.example.adaapaindonesia.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.example.adaapaindonesia.R
import com.example.adaapaindonesia.adapter.ViewPagerAdapter
import com.example.adaapaindonesia.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.vpOnboarding.adapter = ViewPagerAdapter(supportFragmentManager)
        binding.dotsIndicator.attachTo(binding.vpOnboarding)
        binding.vpOnboarding.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (position == (binding.vpOnboarding.adapter as ViewPagerAdapter).count - 1){
                    binding.btnMulai.visibility = View.VISIBLE
                } else {
                    binding.btnMulai.visibility = View.INVISIBLE
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
        binding.btnMulai.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}