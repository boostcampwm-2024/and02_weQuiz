package kr.boostcamp_2024.course.category.viewModel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.category.navigation.CategoryRoute
import kr.boostcamp_2024.course.domain.model.BaseQuiz
import kr.boostcamp_2024.course.domain.model.Category
import kr.boostcamp_2024.course.domain.repository.CategoryRepository
import kr.boostcamp_2024.course.domain.repository.QuizRepository
import kr.boostcamp_2024.course.domain.repository.StudyGroupRepository
import javax.inject.Inject

data class CategoryUiState(
    val category: Category? = null,
    val quizList: List<BaseQuiz>? = null,
    val snackBarMessage: String? = null,
    val isDeleteCategorySuccess: Boolean = false,
    val isDropDownMenuExpanded: Boolean = false,
)

@HiltViewModel
class CategoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val categoryRepository: CategoryRepository,
    private val quizRepository: QuizRepository,
    private val studyGroupRepository: StudyGroupRepository,
) : ViewModel() {
    private val studyGroupId: String = savedStateHandle.toRoute<CategoryRoute>().studyGroupId
    private val categoryId: String = savedStateHandle.toRoute<CategoryRoute>().categoryId
    private val _categoryUiState: MutableStateFlow<CategoryUiState> =
        MutableStateFlow(CategoryUiState())
    val categoryUiState: StateFlow<CategoryUiState> = _categoryUiState

    fun initViewmodel() {
        loadCategory(categoryId)
    }

    private fun loadCategory(categoryId: String) {
        viewModelScope.launch {
            categoryRepository.getCategory(categoryId).onSuccess { category ->
                _categoryUiState.update {
                    it.copy(
                        category = category,
                    )
                }
                loadQuizList(category.quizzes)
            }.onFailure {
                Log.e("CategoryViewModel", "Failed to load category", it)
                setNewSnackBarMessage("카테고리 데이터 로딩에 실패했습니다. 다시 시도해주세요!")
            }
        }
    }

    private fun loadQuizList(quizIdList: List<String>) {
        viewModelScope.launch {
            quizRepository.getQuizList(quizIdList).onSuccess { quizList ->
                _categoryUiState.update {
                    it.copy(quizList = quizList)
                }
            }.onFailure {
                Log.e("CategoryViewModel", "Failed to load quiz list", it)
                setNewSnackBarMessage("퀴즈 데이터 로딩에 실패했습니다. 다시 시도해주세요!")
            }
        }
    }

    fun setNewSnackBarMessage(message: String?) {
        _categoryUiState.update {
            it.copy(snackBarMessage = message)
        }
    }

    fun onCategoryDeleteClick() {
        viewModelScope.launch {
            categoryRepository.deleteCategory(categoryId)
                .onSuccess {
                    setNewSnackBarMessage("카테고리 삭제에 성공했습니다.")
                    studyGroupRepository.deleteCategory(studyGroupId, categoryId)
                        .onSuccess {
                            _categoryUiState.update { it.copy(isDeleteCategorySuccess = true) }
                        }.onFailure {
                            Log.e("CategoryViewModel", "Failed to delete category from study group", it)
                            setNewSnackBarMessage("카테고리 삭제에 실패했습니다. 다시 시도해주세요")
                        }
                }.onFailure {
                    Log.e("CategoryViewModel", "Failed to delete category", it)
                    setNewSnackBarMessage("카테고리 삭제에 실패했습니다. 다시 시도해주세요!")
                }
        }
    }

    fun onDropDownMenuClick() {
        _categoryUiState.update {
            it.copy(isDropDownMenuExpanded = !it.isDropDownMenuExpanded)
        }
    }

    fun onDropDownMenuDismiss() {
        _categoryUiState.update {
            it.copy(isDropDownMenuExpanded = false)
        }
    }

}
