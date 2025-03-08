package kr.boostcamp_2024.course.category.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import kr.boostcamp_2024.course.category.R
import kr.boostcamp_2024.course.category.viewModel.CreateCategoryViewModel
import kr.boostcamp_2024.course.designsystem.ui.annotation.PreviewKoLightDark
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizCircularProgressIndicator
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizPhotoPickerAsyncImage
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizValidateTextField

@Composable
internal fun CreateCategoryScreen(
    onNavigationButtonClick: () -> Unit,
    onCreateCategorySuccess: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onShowErrorSnackbar: (Throwable) -> Unit,
    viewModel: CreateCategoryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.createCategoryUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchCategoryInfo()
    }

    LaunchedEffect(Unit) {
        viewModel.errorFlow.collectLatest { throwable -> onShowErrorSnackbar(throwable) }
    }

    LaunchedEffect(uiState.creationSuccess) {
        if (uiState.creationSuccess) {
            onCreateCategorySuccess()
        }
    }

    val categoryId = viewModel.categoryId
    val guideText by remember(categoryId) {
        mutableIntStateOf(
            if (viewModel.categoryId == null) {
                R.string.txt_create_category
            } else {
                R.string.txt_edit_category
            },
        )
    }

    CreateCategoryScreen(
        name = uiState.categoryName,
        description = uiState.categoryDescription,
        currentCategoryImage = uiState.currentImage,
        defaultCategoryImageUri = uiState.defaultImageUri,
        isCategoryCreationValid = uiState.isCategoryCreationValid,
        onNameChanged = viewModel::onNameChanged,
        onDescriptionChanged = viewModel::onDescriptionChanged,
        onNavigationButtonClick = onNavigationButtonClick,
        onCreateCategoryButtonClick = viewModel::uploadCategory,
        isLoading = uiState.isLoading,
        guideText = stringResource(guideText),
        onCurrentCategoryImageChanged = viewModel::onImageByteArrayChanged,
        snackbarHostState = snackbarHostState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CreateCategoryScreen(
    name: String,
    description: String,
    currentCategoryImage: ByteArray?,
    defaultCategoryImageUri: String?,
    isCategoryCreationValid: Boolean,
    onNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onNavigationButtonClick: () -> Unit,
    onCreateCategoryButtonClick: () -> Unit,
    isLoading: Boolean,
    guideText: String,
    onCurrentCategoryImageChanged: (ByteArray) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = guideText)
                },
                navigationIcon = {
                    IconButton(onClick = onNavigationButtonClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.des_btn_back),
                        )
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
        ) {
            WeQuizPhotoPickerAsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 70.dp)
                    .aspectRatio(1f)
                    .clip(shape = MaterialTheme.shapes.large),
                imageData = currentCategoryImage ?: defaultCategoryImageUri,
                onImageDataChanged = onCurrentCategoryImageChanged,
            )
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                WeQuizValidateTextField(
                    label = stringResource(R.string.txt_create_category_name_label),
                    text = name,
                    onTextChanged = onNameChanged,
                    placeholder = stringResource(R.string.txt_create_category_name_placeholder),
                    errorMessage = stringResource(R.string.txt_category_name_error_message),
                    validFun = { name.length <= 20 },
                )
                WeQuizValidateTextField(
                    label = stringResource(R.string.txt_create_category_des_label),
                    text = description,
                    maxLines = 6,
                    minLines = 6,
                    onTextChanged = onDescriptionChanged,
                    placeholder = stringResource(R.string.txt_create_category_des_placeholder),
                    errorMessage = stringResource(R.string.txt_category_description_error_message),
                    validFun = { description.length <= 100 },
                )
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCreateCategoryButtonClick,
                    enabled = isCategoryCreationValid && !isLoading,
                ) {
                    Text(text = guideText)
                }
            }
        }

        if (isLoading) {
            WeQuizCircularProgressIndicator()
        }
    }
}

@PreviewKoLightDark
@Composable
private fun CreateCategoryScreenPreview() {
    WeQuizTheme {
        CreateCategoryScreen(
            name = "Category Name",
            description = "Category Description",
            currentCategoryImage = null,
            defaultCategoryImageUri = null,
            isCategoryCreationValid = true,
            onNameChanged = {},
            onDescriptionChanged = {},
            onNavigationButtonClick = {},
            onCreateCategoryButtonClick = {},
            isLoading = false,
            guideText = "Create Category",
            onCurrentCategoryImageChanged = {},
        )
    }
}
