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
    val isDeleteCategorySuccess: Boolean = false,
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

    private val _categoryUiState: MutableStateFlow<CategoryUiState> = MutableStateFlow(CategoryUiState())
    val categoryUiState: StateFlow<CategoryUiState> = _categoryUiState.asStateFlow()

    private val _errorFlow = MutableSharedFlow<Throwable>()
    val errorFlow = _errorFlow.asSharedFlow()

    fun initViewmodel() {
        loadCategory(categoryId)
    }

    private fun loadCategory(categoryId: String) {
        viewModelScope.launch {
            try {
                val category = categoryRepository.getCategory(categoryId)
                _categoryUiState.update { it.copy(category = category) }
                loadQuizList(category.quizzes)
            } catch (e: Exception) {
                _errorFlow.emit(e)
            }
        }
    }

    private fun loadQuizList(quizIdList: List<String>) {
        viewModelScope.launch {
            try {
                val quizList = quizRepository.getQuizList(quizIdList)
                _categoryUiState.update { it.copy(quizList = quizList) }
            } catch (e: Exception) {
                _errorFlow.emit(e)
            }
        }
    }

    fun onCategoryDeleteClick() {
        viewModelScope.launch {
            try {
                categoryRepository.deleteCategory(categoryId)
                studyGroupRepository.deleteCategory(studyGroupId, categoryId)
                _categoryUiState.update { it.copy(isDeleteCategorySuccess = true) }
            } catch (e: Exception) {
                _errorFlow.emit(e)
            }
        }
    }
}
