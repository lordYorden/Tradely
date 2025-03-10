package dev.lordyorden.tradely.ui.leaderboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dev.lordyorden.tradely.R
import dev.lordyorden.tradely.adapter.ProfileAdapter
import dev.lordyorden.tradely.databinding.FragmentLeaderboardBinding
import dev.lordyorden.tradely.interfaces.profile.ProfileCallback
import dev.lordyorden.tradely.models.Profile
import dev.lordyorden.tradely.models.ProfileManager
import dev.lordyorden.tradely.ui.home.profile_card.ProfileViewModel

class LeaderboardFragment : Fragment() {

    private var _binding: FragmentLeaderboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var adapter: ProfileAdapter

    private val profileSelector: ProfileViewModel by activityViewModels()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel =
            ViewModelProvider(this).get(LeaderboardViewModel::class.java)

        _binding = FragmentLeaderboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = ProfileAdapter()
        binding.leaderboardRVProfiles.adapter = adapter
        binding.leaderboardRVProfiles.layoutManager = LinearLayoutManager(context)


        adapter.profileCallback = object : ProfileCallback {
            override fun onFollowClicked(profile: Profile, position: Int) {
                ProfileManager.getInstance().toggleFollow(profile.id)
                //adapter.notifyItemChanged(position)
                Toast.makeText(context, "Follow clicked on ${profile.name}", Toast.LENGTH_SHORT).show()
            }

            override fun onProfileClicked(profile: Profile) {
                profileSelector.selectProfile(profile)
                Toast.makeText(context, "Profile clicked on ${profile.name}", Toast.LENGTH_SHORT).show()
                val navController = findNavController()
                navController.navigate(R.id.leaderboard_to_home)
            }

        }

        viewModel.profiles.observe(viewLifecycleOwner) { profiles ->
            if(profiles != null) {
                adapter.profiles = profiles
                adapter.notifyDataSetChanged()
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}