package upv.dadm.persistentstorage.ui.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(

) : ViewModel() {

    private val _isDisplayAll = MutableStateFlow(false)
    val isDisplayAll = _isDisplayAll.asStateFlow()

    fun setDisplayAll(display: Boolean) {
        _isDisplayAll.update { display }
    }
}