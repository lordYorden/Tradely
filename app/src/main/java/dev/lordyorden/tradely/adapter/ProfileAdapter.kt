package dev.lordyorden.tradely.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.lordyorden.tradely.interfaces.profile.ProfileCallback
import dev.lordyorden.tradely.R
//import dev.lordyorden.test_fragments.utilities.Constants
import dev.lordyorden.tradely.databinding.ProfileItemBinding
//import dev.lordyorden.test_fragments.interfaces.MovieCallback
import dev.lordyorden.tradely.models.Profile
import dev.lordyorden.tradely.models.ProfileManager
import dev.lordyorden.tradely.utilities.ImageLoader

class ProfileAdapter(
    var profiles: List<Profile> = listOf(
        Profile.Builder()
            .name("No data Yet...")
            .build()
    )
) :
    RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    var profileCallback: ProfileCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding = ProfileItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return profiles.size
    }

    private fun getItem(position: Int) = profiles[position]

    private fun setChangeColorAndArrow(binding: ProfileItemBinding, change: Double) {
        val positiveColor = binding.root.resources.getColor(R.color.positive, null)
        val negativeColor = binding.root.resources.getColor(R.color.negative, null)

        val arrowUp: Int = R.drawable.ic_arrow_up
        val arrowDown: Int = R.drawable.ic_arrow_down

        if(change > 0){
            binding.profileLBLChange.setTextColor(positiveColor)
            binding.profileIMGChange.setImageResource(arrowUp)
            binding.profileIMGChange.setColorFilter(positiveColor)
        } else {
            binding.profileLBLChange.setTextColor(negativeColor)
            binding.profileIMGChange.setImageResource(arrowDown)
            binding.profileIMGChange.setColorFilter(negativeColor)
        }
    }

    private fun setFollowButton(binding: ProfileItemBinding, isFollowed: Boolean) {
        val follow = binding.root.resources.getString(R.string.follow)
        val unfollow = binding.root.resources.getString(R.string.unfollow)


        if(isFollowed){
            binding.profileLBLFollow.text = unfollow
        } else {
            binding.profileLBLFollow.text = follow
        }
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        with(holder) {

            val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
            holder.itemView.layoutParams = ItemLoadingHelper.fixedLastMargin(params, position, profiles.size)

            with(getItem(position)) {
                binding.profileLBLName.text = name
                binding.profileLBLDescription.text = description
                binding.profileLBLNetWorth.text = buildString {
                    append("Net Worth: ")
                    append(ItemLoadingHelper.formatBigNumberToString(netWorth))
                    append("$")
                }

                binding.profileLBLChange.text = buildString {
                    append(change)
                    append("%")
                }

                ImageLoader.getInstance().loadImage(profilePic, binding.profileIMGProfile)

                setFollowButton(binding, ProfileManager.getInstance().isFollowing(id))
                setChangeColorAndArrow(binding, change)
            }
        }
    }


    inner class ProfileViewHolder(val binding: ProfileItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.profileRLFollow.setOnClickListener {
                profileCallback?.onFollowClicked(
                    getItem(adapterPosition),
                    adapterPosition
                )
            }
        }
    }


}
