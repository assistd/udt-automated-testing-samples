package com.tencent.masterdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tencent.bugly.Bugly
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.masterdemo.common.FragmentHelper
import com.tencent.masterdemo.common.OperateInterface
import com.tencent.masterdemo.ui.dashboard.DashboardFragment
import com.tencent.masterdemo.ui.dashboard.NewsFragment
import com.tencent.masterdemo.ui.home.HomeFragment
import com.tencent.masterdemo.ui.notifications.NotificationsFragment

class MainActivity : AppCompatActivity(), OperateInterface {
    private lateinit var fragments: Array<Fragment>

    private var currentFragmentIndex = FragmentHelper.INDEX_HOME

    private val selectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val i = item.itemId
        if (i == R.id.navigation_home) {
            switchCurrentFragment(FragmentHelper.INDEX_HOME)
            return@OnNavigationItemSelectedListener true
        } else if (i == R.id.navigation_notifications) {
            switchCurrentFragment(FragmentHelper.INDEX_NOTIFICATION)
            return@OnNavigationItemSelectedListener true
        } else if (i == R.id.navigation_dashboard) {
            switchCurrentFragment(FragmentHelper.INDEX_DASHBOARD)
            return@OnNavigationItemSelectedListener true
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val app = applicationContext
        Bugly.init(app, "a51a0d4bb3", false)
        Bugly.setAppChannel(app, "channel-test-crash")
        Bugly.setUserId(app, "test-crash-001")

        CrashReport.closeCrashReport()
        CrashReport.closeNativeReport()

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(selectedListener)
        initFragments()
    }

    private fun initFragments() {
        val homeFragment = HomeFragment()
        val notificationsFragment = NotificationsFragment()
        val dashboardFragment = DashboardFragment()
        val newsFragment = NewsFragment()

        fragments = arrayOf(homeFragment, notificationsFragment,
            dashboardFragment, newsFragment)

        currentFragmentIndex = FragmentHelper.INDEX_HOME

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, homeFragment).hide(homeFragment)
            .add(R.id.fragment_container, notificationsFragment).hide(notificationsFragment)
            .add(R.id.fragment_container, dashboardFragment).hide(dashboardFragment)
            .add(R.id.fragment_container, newsFragment).hide(newsFragment)
            .show(homeFragment)
            .commit()

        FragmentHelper.operInterface = this
    }

    private fun switchCurrentFragment(index: Int) {
        if (currentFragmentIndex != index) {
            switchFragment(currentFragmentIndex, index)
            currentFragmentIndex = index
        }
    }

    private fun switchFragment(lastIndex: Int, index: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.hide(fragments[lastIndex])
        transaction.show(fragments[index]).commitAllowingStateLoss()
    }

    override fun switchFragment(id: Int) {
        this.switchCurrentFragment(id)
    }
}