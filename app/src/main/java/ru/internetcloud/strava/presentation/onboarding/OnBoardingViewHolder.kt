package ru.internetcloud.strava.presentation.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import ru.internetcloud.strava.databinding.OnboardingItemBinding

class OnBoardingViewHolder(
    private val binding: OnboardingItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(items: OnBoardingUI) {
        setImage(items.imageResId)
        setTitle(items.titleResId)
    }

    private fun setImage(@DrawableRes imageResId: Int) {
        binding.onBoardingImage.setImageResource(imageResId)
    }

    private fun setTitle(@StringRes titleResId: Int) {
        binding.onBoardingTitle.setText(titleResId)
    }
}
