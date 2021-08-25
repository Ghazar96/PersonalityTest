package com.example.personalitytest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.personalitytest.questionPage.QuestionsFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            openFragment(QuestionsFragment())
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.openFragment(
            fragment,
            fragment.tag ?: "",
            R.id.fragmentContainer
        )
    }
}
