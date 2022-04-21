package com.example.mock_off_project.ui

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.mock_off_project.R
import dagger.hilt.android.AndroidEntryPoint

@RequiresApi(Build.VERSION_CODES.M)
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hideSystemUI()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentMain, MainFragment())
            .commitAllowingStateLoss()

    }
    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(
            window,
            window.decorView.findViewById(android.R.id.content)
        ).hide(WindowInsetsCompat.Type.systemBars())
    }
}