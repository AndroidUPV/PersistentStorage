package upv.dadm.persistentstorage.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import upv.dadm.persistentstorage.R
import upv.dadm.persistentstorage.databinding.FragmentAddUserBinding
import upv.dadm.persistentstorage.model.User
import upv.dadm.persistentstorage.ui.viewmodels.UserViewModel

class AddUserBottomSheet : BottomSheetDialogFragment(R.layout.fragment_add_user) {

    private val viewModel: UserViewModel by activityViewModels()

    private var _binding: FragmentAddUserBinding? = null
    private val binding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddUserBinding.bind(view)

        // Clear the text fields from previous information
        clearTextFields()

        binding.bCreateUser.setOnClickListener {
            // The user name cannot be empty
            if (binding.etNameNew.text.isNullOrEmpty()) {
                Snackbar.make(binding.bCreateUser, R.string.empty_name, Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                // Set id = 0 to let Room autogenerate the id
                viewModel.addUser(
                    User(
                        id = "0",
                        name = binding.etNameNew.text.toString(),
                        street = binding.etStreetNew.text.toString(),
                        suite = binding.etSuiteNew.text.toString(),
                        zipcode = binding.etZipcodeNew.text.toString(),
                        city = binding.etCityNew.text.toString(),
                        email = binding.etEmailNew.text.toString(),
                        phone = binding.etNameNew.text.toString()
                    )
                )
                dismiss()
            }
        }

        binding.bCancel.setOnClickListener { dismiss() }
    }

    private fun clearTextFields() {
        binding.etNameNew.setText("")
        binding.etStreetNew.setText("")
        binding.etSuiteNew.setText("")
        binding.etZipcodeNew.setText("")
        binding.etCityNew.setText("")
        binding.etEmailNew.setText("")
        binding.etPhoneNew.setText("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}