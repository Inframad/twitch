package com.example.twitchapp.ui.favourite

import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.twitchapp.R
import com.example.twitchapp.databinding.FragmentFavouriteGamesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteGamesFragment: Fragment(R.layout.fragment_favourite_games) {

    private val viewBinding: FragmentFavouriteGamesBinding by viewBinding()

}