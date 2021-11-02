package com.twango.calllogger

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TableLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.twango.calllogger.databinding.ActivityMainBinding
import com.twango.calllogger.ui.CallLogs.CallLogsFragment1
import com.twango.calllogger.ui.CallLogs.CallLogsFragment2
import com.twango.calllogger.ui.CallLogs.CallLogsViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setUpAdapter()
        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:")
            startActivity(intent)
        }
    }

    private fun setUpAdapter() {
        val fragsList: ArrayList<Fragment> = ArrayList()
        for (i in 1..7) {
            when (i) {
                6, 7 -> {
                    val fragment = CallLogsFragment2.newInstance("$i")
                    fragsList.add(fragment)
                }
                else -> {
                    val fragment = CallLogsFragment1.newInstance("$i")
                    fragsList.add(fragment)
                }
            }
        }
        val adapter = CallLogsViewPagerAdapter(fragsList, this)
        binding.fragmentViewPager.adapter = adapter

        TabLayoutMediator(
            binding.tabLayout, binding.fragmentViewPager) { tab, position ->
            binding.apply {
                when (position) {
                    0 -> {
                        tab.text = "All"
                        tab.icon =
                            AppCompatResources.getDrawable(this@MainActivity, R.drawable.call_icon)
                    }
                    1 -> {
                        tab.text = "Incoming"
                        tab.icon = AppCompatResources.getDrawable(
                            this@MainActivity,
                            R.drawable.call_received_icon
                        )
                    }
                    2 -> {
                        tab.text = "Outgoing"
                        tab.icon = AppCompatResources.getDrawable(
                            this@MainActivity,
                            R.drawable.call_outgoing_icon
                        )
                    }
                    3 -> {
                        tab.text = "Missed"
                        tab.icon = AppCompatResources.getDrawable(
                            this@MainActivity,
                            R.drawable.call_missed_icon
                        )
                    }
                    4 -> {
                        tab.text = "Rejected"
                        tab.icon = AppCompatResources.getDrawable(
                            this@MainActivity,
                            R.drawable.call_not_interested
                        )
                    }
                    5 -> {
                        tab.text = "Never Attended"
                        tab.icon = AppCompatResources.getDrawable(
                            this@MainActivity,
                            R.drawable.call_not_received
                        )
                    }
                    6 -> {
                        tab.text = "Not Picked up by client"
                        tab.icon = AppCompatResources.getDrawable(
                            this@MainActivity,
                            R.drawable.call_disabled
                        )
                    }

                } } }.attach()

        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
               when(tab?.text){
                   "Never Attended" ->{
                       binding.apply {
                           floatingActionButton.hide()
                           Handler(Looper.getMainLooper()).postDelayed({
                               floatingActionButton.show()
                           }, 600)

                       }
                   }

               }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.d("unselectedTab","${tab?.text}")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

}
