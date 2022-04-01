package com.example.common.livedata

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.example.common.extensions.bindActionLiveData
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
            bindActionLiveData(showToastCommand) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
            bindActionLiveData(showSnackbarCommand) {
                Snackbar.make(
                    requireView(),
                    it.message,
                    it.length
                ).setAction(it.actionName, it.action)
                    .show()
            }
        }
    }
}