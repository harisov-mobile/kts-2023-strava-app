package ru.internetcloud.strava.presentation.onboarding

import androidx.recyclerview.widget.RecyclerView
import ru.internetcloud.strava.databinding.OnboardingItemBinding

class OnBoardingViewHolder(
    private val binding: OnboardingItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(items: OnBoardingUI) {
        setImage(items.imageResId)
        setTitle(items.titleResId)
    }

    private fun setImage(imageResId: Int) {
        binding.onBoardingImage.setImageResource(imageResId)
    }

    private fun setTitle(titleResId: Int) {
        binding.onBoardingTitle.setText(titleResId)
    }
}
