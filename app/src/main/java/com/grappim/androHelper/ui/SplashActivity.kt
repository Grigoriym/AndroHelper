package com.grappim.androHelper.ui

import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.grappim.androHelper.R
import com.grappim.androHelper.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity(R.layout.activity_splash) {

    companion object {
        private const val SCREEN_DELAY = 1_000L
    }

    private val viewBinding: ActivitySplashBinding by viewBinding(ActivitySplashBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding.imageLogo
            .animate()
            .alpha(1f)
            .scaleX(1.2f)
            .scaleY(1.2f)
            .setDuration(SCREEN_DELAY)
            .interpolator = AccelerateDecelerateInterpolator()

        lifecycleScope.launch {
            delay(SCREEN_DELAY)
            MainActivity.start(this@SplashActivity)

            finish()
        }
    }
}