package com.example.twitchapp.ui

import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment<TViewModel : BaseViewModel>(@LayoutRes lId: Int) :
    Fragment(lId) {

    protected abstract val viewModel: TViewModel

    protected open fun bindViewModel() {
        with(viewModel) {
            bindAction(showToastCommand) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

