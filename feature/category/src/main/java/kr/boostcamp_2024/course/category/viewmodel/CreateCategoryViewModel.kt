package kr.boostcamp_2024.course.category.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.repository.CategoryRepository
import javax.inject.Inject

data class CreateCategoryUiState(
    val isLoading: Boolean = false,
    val categoryName: String = "",
    val categoryDescription: String = "",
    val isCategoryCreationValid: Boolean = false,
    val errorMessage: String? = null,
)

@HiltViewModel
class CreateCategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
) : ViewModel() {
    private val _createCategoryUiState: MutableStateFlow<CreateCategoryUiState> = MutableStateFlow(CreateCategoryUiState())
    val createCategoryUiState: StateFlow<CreateCategoryUiState> = _createCategoryUiState.asStateFlow()

    fun createCategory() {
        setNewLoadingState(true)
        viewModelScope.launch {
            categoryRepository.createCategory(
                _createCategoryUiState.value.categoryName,
                _createCategoryUiState.value.categoryDescription.takeIf { it.isNotBlank() },
            ).onSuccess { categoryId ->
                // todo: 스터디에 카테고리 추가
            }.onFailure {
                Log.e("CreateCategoryViewModel", "Failed to create category")
                setErrorMessage("카테고리 생성에 실패했습니다. 다시 시도해주세요!")
            }
        }
    }

    fun setErrorMessage(message: String?) {
        _createCategoryUiState.update { currentState ->
            currentState.copy(errorMessage = message)
        }
    }

    private fun setNewLoadingState(isLoading: Boolean) {
        _createCategoryUiState.update { currentState ->
            currentState.copy(isLoading = isLoading)
        }
    }

    fun onNameChanged(name: String) {
        _createCategoryUiState.update { currentState ->
            currentState.copy(categoryName = name)
        }
        checkCreateCategoryValid()
    }

    fun onDescriptionChanged(description: String) {
        _createCategoryUiState.update { currentState ->
            currentState.copy(categoryDescription = description)
        }
        checkCreateCategoryValid()
    }

    private fun checkCreateCategoryValid() {
        _createCategoryUiState.update { currentState ->
            currentState.copy(
                isCategoryCreationValid = currentState.categoryName.isNotBlank(),
            )
        }
    }
}
