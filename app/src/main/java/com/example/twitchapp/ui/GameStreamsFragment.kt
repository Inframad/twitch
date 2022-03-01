package com.example.twitchapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.twitchapp.App
import com.example.twitchapp.databinding.FragmentGameStreamsBinding
import com.example.twitchapp.presentation.GameStreamViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class GameStreamsFragment : Fragment() {

    private var _binding: FragmentGameStreamsBinding? = null
    private val binding: FragmentGameStreamsBinding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: GameStreamViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameStreamsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[GameStreamViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagingDataAdapter = GameStreamsAdapter(GameStreamComparator)

        binding.rv.adapter = pagingDataAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.gameStreamsFlow.collectLatest {
                pagingDataAdapter.submitData(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}