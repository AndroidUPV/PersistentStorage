package upv.dadm.persistentstorage.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import upv.dadm.persistentstorage.R
import upv.dadm.persistentstorage.databinding.FragmentSettingsBinding
import upv.dadm.persistentstorage.ui.viewmodels.SettingsViewModel

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val viewModel: SettingsViewModel by activityViewModels()

    private var _binding: FragmentSettingsBinding? = null
    private val binding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isDisplayAll.collect { display ->
                    binding.swDisplayAllData.isChecked = display
                }
            }
        }

        binding.swDisplayAllData.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setDisplayAll(isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}