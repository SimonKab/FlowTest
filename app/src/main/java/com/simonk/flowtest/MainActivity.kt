package com.simonk.flowtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (supportFragmentManager.fragments.none { it is FirstFragment }) {
            supportFragmentManager.commit {
                replace(R.id.container, FirstFragment())
            }
        }
    }

    fun openSecondFragment() {
        supportFragmentManager.commit {
            replace(R.id.container, SecondFragment()).addToBackStack(null)
        }
    }
}