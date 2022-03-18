package com.example.twitchapp.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.twitchapp.R
import com.example.twitchapp.ui.game.GameFragmentArgs

object Navigator {

    fun goToGameScreen(f: Fragment, args: GameFragmentArgs) {
        f.findNavController().navigate(R.id.gameFragment, args.toBundle())
    }

    fun goToGameScreen(a: FragmentActivity, args: GameFragmentArgs) {
        a.findNavController(R.id.nav_host_fragment).navigate(R.id.gameFragment, args.toBundle())
    }

    fun goBack(f: Fragment) {
        f.findNavController().popBackStack()
    }
}