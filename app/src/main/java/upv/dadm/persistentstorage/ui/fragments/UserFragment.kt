package upv.dadm.persistentstorage.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import upv.dadm.persistentstorage.R
import upv.dadm.persistentstorage.databinding.FragmentUserBinding
import upv.dadm.persistentstorage.ui.viewmodels.SettingsViewModel
import upv.dadm.persistentstorage.ui.viewmodels.UserViewModel
import upv.dadm.persistentstorage.utils.NoUserException

@AndroidEntryPoint
class UserFragment : Fragment(R.layout.fragment_user), MenuProvider {

    private val viewModel: UserViewModel by activityViewModels()
    private val settingsViewModel: SettingsViewModel by activityViewModels()

    private var _binding: FragmentUserBinding? = null
    private val binding
        get() = _binding!!

    // Find user by Id
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUserBinding.bind(view)

        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.tilSearchById.setEndIconOnClickListener { button ->
            // Hide the soft input keyboard
            (getSystemService(
                requireContext(),
                InputMethodManager::class.java
            ) as InputMethodManager)
                .hideSoftInputFromWindow(button.windowToken, 0)
            // Display the entered Id
            if (binding.etSearchById.text.isNullOrBlank()) displayMessage(R.string.no_id)
            else viewModel.getUserById(binding.etSearchById.text.toString())
            // Clear the EditText
            binding.etSearchById.text?.clear()
        }

        // Add a new user
        binding.fabAddUser.setOnClickListener { _ ->
            findNavController().navigate(R.id.navigateToAddUserBottomSheet)
        }

        // Display user data when it changes
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collect {
                    it?.let { user ->
                        binding.etUserId.setText(user.id)
                        binding.etUserName.setText(user.name)
                        binding.etUserAddress.setText(
                            getString(
                                R.string.address_two_lines,
                                user.street,
                                user.suite,
                                user.zipcode,
                                user.city
                            )
                        )
                        binding.etUserEmail.setText(user.email)
                        binding.etUserPhone.setText(user.phone)
                    }
                }
            }
        }

        // Display a message when something goes wrong
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.throwable.collect { throwable ->
                    if (throwable != null) {
                        displayMessage(
                            when (throwable) {
                                is NoUserException -> R.string.no_user
                                else -> R.string.unexpected
                            }
                        )
                        viewModel.resetError()
                    }
                }
            }
        }

        // Show/hide additional data when user settings change
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingsViewModel.isDisplayAll.collect { display ->
                    binding.tilUserId.isVisible = display
                    binding.tilUserAddress.isVisible = display
                    binding.tilUserEmail.isVisible = display
                }
            }
        }

    }

    private fun displayMessage(messageId: Int) {
        displayMessage(getString(messageId))
    }

    private fun displayMessage(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) =
        menuInflater.inflate(R.menu.menu_user, menu)

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            // Navigate to the Settings Fragment
            R.id.mSettings -> {
                findNavController().navigate(R.id.navigateToSettingsFragment)
                true
            }

            else -> false
        }
}