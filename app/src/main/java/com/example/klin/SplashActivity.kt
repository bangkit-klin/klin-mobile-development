package com.example.klin

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.res.TypedArray
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.example.klin.api.Preferences
import com.example.klin.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private var currentLayout = -1
    private lateinit var titles: Array<String>
    private lateinit var descriptions: Array<String>
    private lateinit var images: TypedArray
    private lateinit var binding: ActivitySplashBinding

    companion object {
        val LOGIN_ACTIVITY = Login::class.java
        val HOME_ACTIVITY = HomeActivity::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        showSplash()
        setAllClickListener()
        if (Preferences.getEncryptedSharedPrefences(this)?.getString("firebase_uid", "").toString() != "")
            navigateTo(HOME_ACTIVITY)
    }

    private fun init() {
        titles = resources.getStringArray(R.array.spash_title)
        descriptions = resources.getStringArray(R.array.splash_deskripsi)
        images = resources.obtainTypedArray(R.array.iv_spalsh)
    }

    private fun showSplash() {
        currentLayout++
        binding.tvTitle.text = titles[currentLayout]
        binding.tvDeskripsi.text = descriptions[currentLayout]
        binding.ivSplash.setImageDrawable(images.getDrawable(currentLayout))
        binding.tvPage.text = "Halaman ${currentLayout+1} dari ${titles.size}"
        playAnimation()
    }

    private fun playAnimation() {
        val translation = ObjectAnimator.ofFloat(binding.ivSplash, "translationX", 1000f, 0f)
        val rotation = ObjectAnimator.ofFloat(binding.ivSplash, "rotation", 0f, 360f)
        val fadeIn = ObjectAnimator.ofFloat(binding.ivSplash, "alpha", 0f, 1f)
        val titleTranslation = ObjectAnimator.ofFloat(binding.tvTitle, "translationX", -1000f, 0f)
        val descriptionTranslation = ObjectAnimator.ofFloat(binding.tvDeskripsi, "translationX", 1000f, 0f)

        val animator = AnimatorSet()
        animator.playTogether(translation, rotation, fadeIn, titleTranslation, descriptionTranslation)
        animator.duration = 1000
        animator.interpolator = AccelerateInterpolator()
        animator.start()
    }

    private fun setAllClickListener() {
        binding.ivNext.setOnClickListener {
            if(currentLayout == titles.size-1) {
                navigateTo(LOGIN_ACTIVITY)
            } else {
                showSplash()
            }
        }

        binding.tvSkip.setOnClickListener {
            navigateTo(LOGIN_ACTIVITY)
        }
    }

    private fun navigateTo(activityTujuan: Class<*>) {
        val intent = Intent(this@SplashActivity, activityTujuan)
        startActivity(intent)
    }

}
