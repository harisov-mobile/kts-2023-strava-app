package ru.internetcloud.strava.presentation.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.internetcloud.strava.databinding.OnboardingItemBinding

class OnBoardingAdapter : RecyclerView.Adapter<OnBoardingViewHolder>() {

    private var items: List<OnBoardingUI> = listOf()

    fun setItems(items: List<OnBoardingUI>) {
        this.items = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        return OnBoardingViewHolder(
            OnboardingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
