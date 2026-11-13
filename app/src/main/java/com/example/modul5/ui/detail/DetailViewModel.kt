package com.example.modul5.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.modul5.data.model.Student
import com.example.modul5.data.repository.StudentRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class DetailViewModel(
    private val repository: StudentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun loadStudent(id: Int) {
        viewModelScope.launch {
            _uiState.value = DetailUiState(isLoading = true)
            try {
                val student = repository.getStudent(id)
                _uiState.value = DetailUiState(student = student)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = DetailUiState(
                    errorMessage = e.message ?: "Data tidak tersedia"
                )
            }
        }
    }

    // Tambahkan companion object factory di sini agar bisa dipanggil di NavigationAppLab
    companion object {
        fun factory(repository: StudentRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return DetailViewModel(repository) as T
                }
            }
        }
    }
}