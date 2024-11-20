package kr.boostcamp_2024.course.category.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    val isCategoryCreationValid: Boolean = false,
    val creationSuccess: Boolean = false,
    val errorMessage: String? = null,
    val categoryImageUrl: String? = null,
    val currentImage: ByteArray? = null,
    val defaultImageUri: String? = null,
)

@HiltViewModel
class CreateCategoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val categoryRepository: CategoryRepository,
    private val studyGroupRepository: StudyGroupRepository,
    private val storageRepository: StorageRepository,
) : ViewModel() {
    private val _createCategoryUiState: MutableStateFlow<CreateCategoryUiState> = MutableStateFlow(CreateCategoryUiState())
    val createCategoryUiState: StateFlow<CreateCategoryUiState> = _createCategoryUiState.asStateFlow()
    private val studyGroupId = savedStateHandle.toRoute<CreateCategoryRoute>().studyGroupId
    val categoryId = savedStateHandle.toRoute<CreateCategoryRoute>().categoryId

    fun uploadCategory() {
        setLoading()
        viewModelScope.launch {
            when (categoryId) {
                null -> createCategory()
                else -> updateCategory(categoryId)
            }

        }
    }

    private suspend fun createCategory() {
        _createCategoryUiState.value.currentImage?.let { image ->
            storageRepository.uploadImage(image).onSuccess { imageUrl ->
                categoryRepository.createCategory(
                    _createCategoryUiState.value.categoryName,
                    _createCategoryUiState.value.categoryDescription.takeIf { it.isNotBlank() },
                    imageUrl,
                ).onSuccess { categoryId ->
                    saveCategoryToStudyGroup(categoryId)
                }.onFailure {
                    Log.e("CreateCategoryViewModel", "Failed to create category", it)
                    setErrorMessage("카테고리 생성에 실패했습니다. 다시 시도해주세요!")
                }

            }.onFailure {
                Log.e("CreateCategoryViewModel", "Failed to upload image", it)
                setErrorMessage("이미지 업로드에 실패했습니다. 다시 시도해주세요!")
                _createCategoryUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private suspend fun updateCategory(categoryId: String) {
        _createCategoryUiState.value.currentImage?.let { image ->
            storageRepository.uploadImage(image).onSuccess { imageUrl ->
                categoryRepository.updateCategory(
                    categoryId,
                    _createCategoryUiState.value.categoryName,
                    _createCategoryUiState.value.categoryDescription.takeIf { it.isNotBlank() },
                    imageUrl,
                ).onSuccess {
                    saveCategoryToStudyGroup(categoryId)
                }.onFailure {
                    Log.e("CreateCategoryViewModel", "Failed to create category", it)
                    setErrorMessage("카테고리 생성에 실패했습니다. 다시 시도해주세요!")
                }

            }.onFailure {
                Log.e("CreateCategoryViewModel", "Failed to upload image", it)
                setErrorMessage("이미지 업로드에 실패했습니다. 다시 시도해주세요!")
                _createCategoryUiState.update { it.copy(isLoading = false) }
            }
        }
    }


    private suspend fun saveCategoryToStudyGroup(categoryId: String) {
        try {
            if (studyGroupId != null) {
                studyGroupRepository.addCategoryToStudyGroup(studyGroupId, categoryId).getOrThrow()
            }

            _createCategoryUiState.update { currentState ->
                currentState.copy(
                    isLoading = false,
                    creationSuccess = true,
                )
            }
        } catch (exception: Exception) {
            Log.e("CreateCategoryViewModel", "Failed to save category to study group", exception)
            setErrorMessage("카테고리 저장에 실패했습니다. 다시 시도해주세요!")
        }
    }

    fun setErrorMessage(message: String?) {
        _createCategoryUiState.update { currentState ->
            currentState.copy(errorMessage = message)
        }
    }

    private fun setLoading() {
        _createCategoryUiState.update { currentState ->
            currentState.copy(isLoading = true)
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

    fun onImageByteArrayChanged(imageByteArray: ByteArray) {
        _createCategoryUiState.update { it.copy(currentImage = imageByteArray) }
    }

}
