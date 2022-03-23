package com.example.twitchapp.common

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragmentRx<TViewModel : BaseViewModelRx>(@LayoutRes lId: Int) :
    Fragment(lId) {

    protected abstract val viewModel: TViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        bindViewModel()
    }

    protected open fun initViews() {}

    protected open fun bindViewModel() {
    }
}