package com.example.twitchapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.twitchapp.databinding.FragmentFavouriteGamesBinding

class FavouriteGamesFragment: Fragment() {

    private var _binding: FragmentFavouriteGamesBinding? = null
    private val binding: FragmentFavouriteGamesBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteGamesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}