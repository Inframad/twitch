package com.example.twitchapp.ui

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.twitchapp.R
import com.example.twitchapp.ui.game.GameFragmentArgs

object Navigator {

    fun goToGameScreen(f: Fragment, args: GameFragmentArgs) {
        f.findNavController().navigate(R.id.gameFragment, args.toBundle())
    }

    fun goBack(f: Fragment) {
        f.findNavController().popBackStack()
    }
}