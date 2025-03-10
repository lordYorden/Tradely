package dev.lordyorden.tradely.ui.home.profile_card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dev.lordyorden.tradely.adapter.ItemLoadingHelper
import dev.lordyorden.tradely.databinding.FragmentProfileCardBinding
import dev.lordyorden.tradely.models.Profile
import dev.lordyorden.tradely.utilities.ImageLoader
import java.util.Locale

class ProfileCardFragment : Fragment() {

//    companion object {
//        fun newInstance() = ProfileCardFragment()
//    }

    private lateinit var binding: FragmentProfileCardBinding
    private val profileSelector: ProfileViewModel by activityViewModels()

    private var profile = Profile.Builder()
        .name("No data Yet...")
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileCardBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        profileSelector.selectedProfile.observe(viewLifecycleOwner) { profile ->
            this.profile = profile
            displayProfile(profile)
        }
    }

    override fun onResume() {
        super.onResume()
        displayProfile(profile)
    }

    private fun displayProfile(profile: Profile?) {
        profile?: return
        val currProfile: Profile = profile

        with(currProfile){
            binding.profileLBLName.text = name
            binding.profileLBLDescription.text = description
            binding.profileLBLNetWorth.text = buildString {
                append("Net Worth: ")
                append(ItemLoadingHelper.formatBigNumberToString(netWorth))
                append("$")
            }

            ItemLoadingHelper.loadFlag(country, binding.profileIMGFlag)
            ImageLoader.getInstance().loadImage(profilePic, binding.profileIMGProfile)

            binding.profileLBLChange.text = String.format(Locale.getDefault(), "%.2f%%", change)
            ItemLoadingHelper.setChangeColorAndArrow(binding,  binding.profileLBLChange, binding.profileIMGChange, change)
        }
    }
}