package com.example.modul5.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.modul5.data.model.Student
import com.example.modul5.data.repository.StudentRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

data class HomeUiState(
    val students: List<Student> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class HomeViewModel(
    private val repository: StudentRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    init {
        observeSearch()
    }
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }
    @OptIn(FlowPreview::class)
    private fun observeSearch() {
        viewModelScope.launch {
            _searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { query ->
                    _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                    try {
                        val data = if (query.isBlank()) {
                            repository.getStudents()
                        } else {
                            repository.searchStudents(query)
                        }
                        _uiState.value = HomeUiState(students = data)
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        _uiState.value = HomeUiState(
                            errorMessage = e.message ?: "Pencarian gagal"
                        )
                    }
                }
        }
    }
    fun retry() {
        val query = _searchQuery.value
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            kotlinx.coroutines.delay(1000)
            try {
                val data = if (query.isBlank()) {
                    repository.getStudents()
                } else {
                    repository.searchStudents(query)
                }
                _uiState.value = HomeUiState(students = data)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = HomeUiState(
                    errorMessage = e.message ?: "Request gagal"
                )
            }
        }
    }
    fun refresh() {
        retry()
    }
}