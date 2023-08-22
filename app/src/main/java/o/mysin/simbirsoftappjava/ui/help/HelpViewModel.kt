package o.mysin.simbirsoftappjava.ui.help

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import o.mysin.simbirsoftappjava.domain.repository.HelpCategoryRepository
import o.mysin.simbirsoftappjava.domain.model.HelpCategory
import o.mysin.simbirsoftappjava.utils.ErrorMessage

class HelpViewModel(
    private val helpCategoryRepository: HelpCategoryRepository,
) : ViewModel() {


    private val _helpCategoryList: MutableLiveData<List<HelpCategory>> = MutableLiveData()
    val helpCategoryList: LiveData<List<HelpCategory>>
        get() = _helpCategoryList

    private val _errorMessage: MutableLiveData<ErrorMessage> = MutableLiveData()
    val errorMessage: LiveData<ErrorMessage>
        get() = _errorMessage

    fun updateData() {
        if (_helpCategoryList.value.isNullOrEmpty()) {
            loadHelpCategory()
        }
    }

    private fun loadHelpCategory() {
        viewModelScope.launch {
            helpCategoryRepository.getHelpCategories()
                .flowOn(Dispatchers.IO)
                .catch { error ->
                    Log.e("MOD_TAG", "loadHelpCategory: $error")
                    _errorMessage.value = ErrorMessage("loadHelpCategory: $error")
                }
                .collect { helpList ->
                    _helpCategoryList.value = helpList
                }
        }
    }
}
