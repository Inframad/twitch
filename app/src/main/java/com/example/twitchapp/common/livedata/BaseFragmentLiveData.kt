package com.example.twitchapp.common.livedata

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.example.twitchapp.R
import com.example.twitchapp.common.extensions.bindActionLiveData
import com.example.twitchapp.navigation.Navigator
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragmentLiveData<TViewModel : BaseViewModelLiveData>(@LayoutRes lId: Int) :
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
            bindActionLiveData(navigateToGameScreenCommand) {
                Navigator.goToGameScreen(this@BaseFragmentLiveData, it)
            }
            bindActionLiveData(showToastCommand) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
            bindActionLiveData(showSnackbarCommand) {
                Snackbar.make(
                    this@BaseFragmentLiveData.requireView(),
                    it.message,
                    it.length
                ).setAction(it.actionName, it.action)
                    .setAnchorView(R.id.bottom_navigation)
                    .show()
            }
        }
    }
}