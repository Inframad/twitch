package com.example.twitchapp.common.flow

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.example.twitchapp.R
import com.example.twitchapp.common.BaseViewModel
import com.example.twitchapp.common.extensions.bindCommandAction
import com.example.twitchapp.navigation.Navigator
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment<TViewModel : BaseViewModel>(@LayoutRes lId: Int) :
    Fragment(lId) {

    protected abstract val viewModel: TViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        bindViewModel()
    }

    protected open fun initViews() {}

    protected open fun bindViewModel() {
        with(viewModel) {
            lifecycle.addObserver(viewModel)
            bindCommandAction(navigateToGameScreenCommand) {
                Navigator.goToGameScreen(this@BaseFragment, it)
            }
            bindCommandAction(showToastCommand) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
            bindCommandAction(showSnackbarCommand) {
                Snackbar.make(
                    this@BaseFragment.requireView(),
                    it.message,
                    Snackbar.LENGTH_LONG
                ).setAction(it.actionName, it.action)
                    .setAnchorView(R.id.bottom_navigation)
                    .show()
            }
        }
    }
}