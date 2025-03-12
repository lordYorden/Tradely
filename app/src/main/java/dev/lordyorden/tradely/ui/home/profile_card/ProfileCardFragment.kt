package dev.lordyorden.tradely.ui.home.profile_card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dev.lordyorden.tradely.R
import dev.lordyorden.tradely.adapter.ItemLoadingHelper
import dev.lordyorden.tradely.databinding.FragmentProfileCardBinding
import dev.lordyorden.tradely.interfaces.profile.ProfileUpdateCallback
import dev.lordyorden.tradely.models.Profile
import dev.lordyorden.tradely.models.ProfileManager
import dev.lordyorden.tradely.utilities.ImageLoader
import java.util.Locale

class ProfileCardFragment : Fragment() {

    private lateinit var binding: FragmentProfileCardBinding
    private val profileSelector: ProfileViewModel by activityViewModels()

    private var profile = ProfileManager.getInstance().myProfile
    private var isToggled = false

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

        binding.profileINGEdit.setOnClickListener{

            if(isToggled){
                val name = binding.profileLBLName.text.let { text -> if (text.isNullOrEmpty()) profile.name else text }
                val description = binding.profileLBLDescription.text.let { text -> if (text.isNullOrEmpty()) profile.description else text }
                val profileChanges = Profile.Builder()
                    .id(profile.id)
                    .name(name.toString())
                    .description(description.toString())
                    .build()
                ProfileManager.getInstance().db.updateProfile(profileChanges, object: ProfileUpdateCallback{
                    override fun onUpdateSuccess(id: String) {
                        profileSelector.selectProfile(profile)
                    }

                    override fun onUpdateFailed(id: String) {
                        Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
                    }

                }, true)
            }

            binding.profileLBLName.clearComposingText()
            binding.profileLBLDescription.clearComposingText()
            isToggled = !isToggled
            toggleEdit()
        }
    }

    private fun renderEditButton() {
        if (profileSelector.selectedProfile.value?.id == ProfileManager.getInstance().myProfile.id) {
            binding.profileINGEdit.visibility = View.VISIBLE
            setIcon(binding.profileINGEdit, isToggled)
        } else {
            binding.profileINGEdit.visibility = View.GONE
        }
    }

    private fun setIcon(profileINGEdit: AppCompatImageView, toggled: Boolean) {
        val toggleIcon = if (toggled) R.drawable.ic_done else R.drawable.ic_edit
        profileINGEdit.setImageResource(toggleIcon)
    }

    private fun toggleEdit() {
        binding.profileLBLName.isEnabled = isToggled
        binding.profileLBLDescription.isEnabled = isToggled
        renderEditButton()
    }

    override fun onResume() {
        super.onResume()
        displayProfile(profile)
    }

    private fun displayProfile(profile: Profile?) {
        profile?: return
        val currProfile: Profile = profile

        with(currProfile){
            binding.profileLBLName.hint = name

            if (description.isNotEmpty()) {
                binding.profileLBLDescription.hint = description
            } else {
                binding.profileLBLDescription.hint = getString(R.string.no_description)
            }

            renderEditButton()

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