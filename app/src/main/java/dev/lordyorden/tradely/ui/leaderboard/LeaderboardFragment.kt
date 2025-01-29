package dev.lordyorden.tradely.ui.leaderboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dev.lordyorden.tradely.adapter.ProfileAdapter
import dev.lordyorden.tradely.interfaces.ProfileCallback
import dev.lordyorden.tradely.databinding.FragmentLeaderboardBinding
import dev.lordyorden.tradely.models.Profile
import dev.lordyorden.tradely.models.ProfileManager

class LeaderboardFragment : Fragment() {

    private var _binding: FragmentLeaderboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var adapter: ProfileAdapter

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
                ProfileManager.getInstance().toggleFollow(profile.id.toString())
                adapter.notifyItemChanged(position)
                Toast.makeText(context, "Follow clicked on ${profile.name}", Toast.LENGTH_SHORT).show()
            }

        }

        viewModel.profiles.observe(viewLifecycleOwner) { profiles ->
            if (!viewModel.isFiltered){
                viewModel.filterProfilesById(ProfileManager.getInstance().myProfile.id)
            }
            adapter.profiles = profiles
            adapter.notifyDataSetChanged()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}