package kr.boostcamp_2024.course.category.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.Category
import kr.boostcamp_2024.course.domain.model.Quiz
import kr.boostcamp_2024.course.domain.repository.CategoryRepository
import kr.boostcamp_2024.course.domain.repository.QuizRepository
import javax.inject.Inject

data class CategoryUiState(
    val category: Category? = null,
    val quizList: List<Quiz>? = null,
    val snackBarMessage: String? = null,
)

@HiltViewModel
class CategoryViewModel @Inject constructor(
//    savedStateHandle: SavedStateHandle,
    private val categoryRepository: CategoryRepository,
    private val quizRepository: QuizRepository,
) : ViewModel() {
    private val _categoryUiState: MutableStateFlow<CategoryUiState> =
        MutableStateFlow(CategoryUiState())
    val categoryUiState: StateFlow<CategoryUiState> = _categoryUiState
        .onStart {
            loadCategory("bKnDNVc1kOgr5GuAC4CR")
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            CategoryUiState(),
        )

    private fun loadCategory(categoryId: String) {
        viewModelScope.launch {
            categoryRepository.getCategory(categoryId)
                .onSuccess { category ->
                    _categoryUiState.update {
                        it.copy(category = category)
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
            quizRepository.getQuizList(quizIdList)
                .onSuccess { quizList ->
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
}