package kr.boostcamp_2024.course.category.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.category.navigation.CreateCategoryRoute
import kr.boostcamp_2024.course.domain.repository.CategoryRepository
import kr.boostcamp_2024.course.domain.repository.StorageRepository
import kr.boostcamp_2024.course.domain.repository.StudyGroupRepository
import javax.inject.Inject

data class CreateCategoryUiState(
    val isLoading: Boolean = false,
    val categoryName: String = "",
    val categoryDescription: String = "",
    val creationSuccess: Boolean = false,
    val currentImage: ByteArray? = null,
    val defaultImageUri: String? = null,
) {
    val isCategoryCreationValid: Boolean = categoryName.length in 1..20 && categoryDescription.length in 0..100 && isLoading.not()
}

@HiltViewModel
class CreateCategoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val categoryRepository: CategoryRepository,
    private val studyGroupRepository: StudyGroupRepository,
    private val storageRepository: StorageRepository,
) : ViewModel() {
    private val studyGroupId = savedStateHandle.toRoute<CreateCategoryRoute>().studyGroupId
    val categoryId = savedStateHandle.toRoute<CreateCategoryRoute>().categoryId

    private val _createCategoryUiState: MutableStateFlow<CreateCategoryUiState> = MutableStateFlow(CreateCategoryUiState())
    val createCategoryUiState: StateFlow<CreateCategoryUiState> = _createCategoryUiState.asStateFlow()

    private val _errorFlow = MutableSharedFlow<Throwable>()
    val errorFlow = _errorFlow.asSharedFlow()

    fun uploadCategory() {
        viewModelScope.launch {
            try {
                setLoading(true)
                when (categoryId) {
                    null -> createCategory()
                    else -> updateCategory(categoryId)
                }
            } catch (e: Exception) {
                _errorFlow.emit(e)
                setLoading(false)
            }
        }
    }

    fun fetchCategoryInfo() {
        viewModelScope.launch {
            try {
                categoryId?.let {
                    setLoading(true)
                    val category = categoryRepository.getCategory(categoryId)
                    _createCategoryUiState.update {
                        it.copy(
                            isLoading = false,
                            categoryName = category.name,
                            categoryDescription = category.description ?: "",
                            defaultImageUri = category.categoryImageUrl,
                        )
                    }
                }
            } catch (e: Exception) {
                _errorFlow.emit(e)
                setLoading(false)
            }
        }
    }

    private suspend fun createCategory() {
        val imageUrl = _createCategoryUiState.value.currentImage?.let { image ->
            storageRepository.uploadImage(image)
        }

        val categoryId = categoryRepository.createCategory(
            _createCategoryUiState.value.categoryName,
            _createCategoryUiState.value.categoryDescription.takeIf { it.isNotBlank() },
            imageUrl,
        )
        saveCategoryToStudyGroup(categoryId)
    }

    private suspend fun updateCategory(categoryId: String) {
        val imageUrl = _createCategoryUiState.value.currentImage?.let { image ->
            storageRepository.uploadImage(image)
        } ?: createCategoryUiState.value.defaultImageUri

        categoryRepository.updateCategory(
            categoryId,
            _createCategoryUiState.value.categoryName,
            _createCategoryUiState.value.categoryDescription.takeIf { it.isNotBlank() },
            imageUrl,
        )
        saveCategoryToStudyGroup(categoryId)
    }

    private suspend fun saveCategoryToStudyGroup(categoryId: String) {
        studyGroupId?.let {
            studyGroupRepository.addCategoryToStudyGroup(studyGroupId, categoryId)
        }

        _createCategoryUiState.update { currentState ->
            currentState.copy(
                isLoading = false,
                creationSuccess = true,
            )
        }
    }

    private fun setLoading(isLoading: Boolean) {
        _createCategoryUiState.update { currentState ->
            currentState.copy(isLoading = isLoading)
        }
    }

    fun onNameChanged(name: String) {
        _createCategoryUiState.update { currentState ->
            currentState.copy(categoryName = name)
        }
    }

    fun onDescriptionChanged(description: String) {
        _createCategoryUiState.update { currentState ->
            currentState.copy(categoryDescription = description)
        }
    }

    fun onImageByteArrayChanged(imageByteArray: ByteArray) {
        _createCategoryUiState.update { it.copy(currentImage = imageByteArray) }
    }
}
