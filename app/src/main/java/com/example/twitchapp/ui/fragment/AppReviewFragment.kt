package com.example.twitchapp.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.twitchapp.R
import com.example.twitchapp.databinding.FragmentAppReviewBinding
import com.example.twitchapp.ui.util.showToast

class AppReviewFragment : Fragment() {

    private var _binding: FragmentAppReviewBinding? = null
    private val binding: FragmentAppReviewBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)


        binding.apply {
            sendReviewBtn.setOnClickListener {
                context?.showToast(
                    String.format(
                        getString(R.string.review_is_sent_msg),
                        appReviewEt.text,
                        appRatingBar.rating.toString()
                    )
                )
                findNavController().popBackStack()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}