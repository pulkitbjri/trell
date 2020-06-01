package com.example.trellassign

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.trellassign.fragments.Screen1
import com.example.trellassign.fragments.Screen1.Companion.newInstance
import com.example.trellassign.fragments.Screen2
import com.example.trellassign.fragments.Screen3


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val model: SharedViewModel by viewModels()


        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = Screen1.newInstance()
        fragmentTransaction.add(R.id.container, fragment)
        fragmentTransaction.commit()

        model.switchFragment.observe(this, Observer {
            changeFragment(it)
        })

    }

    private fun changeFragment(it: String) {
        val fragment= if (it=="2") Screen2.newInstance()
        else  Screen3.newInstance()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }


}
