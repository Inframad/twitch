package com.example.twitchapp.ui.notification

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.twitchapp.R
import com.example.twitchapp.common.BaseFragment
import com.example.twitchapp.common.extensions.bindAction
import com.example.twitchapp.databinding.FragmentNotificationsListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsListFragment
    : BaseFragment<NotificationsListViewModel>(R.layout.fragment_notifications_list) {

    private val viewBinding: FragmentNotificationsListBinding by viewBinding()
    override val viewModel: NotificationsListViewModel by viewModels()

    private var adapter: NotificationsAdapter? = null

    override fun initViews() {
        super.initViews()
        adapter = NotificationsAdapter()
        viewBinding.notificationsRecyclerView.adapter = adapter
    }

    override fun bindViewModel() {
        super.bindViewModel()
        bindAction(viewModel.notifications) {
            adapter?.submitList(it)
        }
    }
}