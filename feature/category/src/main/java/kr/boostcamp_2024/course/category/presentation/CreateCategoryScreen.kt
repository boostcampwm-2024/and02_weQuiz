package kr.boostcamp_2024.course.category.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import kr.boostcamp_2024.course.category.R
import kr.boostcamp_2024.course.category.viewModel.CreateCategoryViewModel
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizTextField

@Composable
fun CreateCategoryScreen(
    onNavigationButtonClick: () -> Unit,
    onCreateCategorySuccess: () -> Unit,
    viewModel: CreateCategoryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.createCategoryUiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                viewModel.onImageUriChanged(it)
            }
        },
    )

    LaunchedEffect(uiState) {
        if (uiState.creationSuccess) {
            onCreateCategorySuccess()
        }
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.setErrorMessage(null)
        }
    }

    CreateCategoryScreen(
        name = uiState.categoryName,
        description = uiState.categoryDescription,
        categoryImageUrl = uiState.categoryImageUrl,
        isCategoryCreationValid = uiState.isCategoryCreationValid,
        snackbarHostState = snackbarHostState,
        onNameChanged = viewModel::onNameChanged,
        onDescriptionChanged = viewModel::onDescriptionChanged,
        onNavigationButtonClick = onNavigationButtonClick,
        onCreateCategoryButtonClick = viewModel::createCategory,
        onImagePickClick = { photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCategoryScreen(
    name: String,
    description: String,
    isCategoryCreationValid: Boolean,
    snackbarHostState: SnackbarHostState,
    onNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onNavigationButtonClick: () -> Unit,
    onCreateCategoryButtonClick: () -> Unit,
    onImagePickClick: () -> Unit,
    categoryImageUrl: String?,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.top_app_bar_create_category))
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
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
        ) {
            AsyncImage(
                model = categoryImageUrl ?: R.drawable.default_profile_image, // URL이 없으면 기본 이미지
                contentDescription = stringResource(R.string.des_category_image),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(148.dp)
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .clickable(onClick = onImagePickClick),
                contentScale = ContentScale.Crop,
            )
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                WeQuizTextField(
                    label = stringResource(R.string.txt_create_category_name_label),
                    text = name,
                    onTextChanged = onNameChanged,
                    placeholder = stringResource(R.string.txt_create_category_name_placeholder),
                )
                WeQuizTextField(
                    label = stringResource(R.string.txt_create_category_des_label),
                    text = description,
                    maxLines = 6,
                    minLines = 6,
                    onTextChanged = onDescriptionChanged,
                    placeholder = stringResource(R.string.txt_create_category_des_placeholder),
                )
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCreateCategoryButtonClick,
                    enabled = isCategoryCreationValid,
                ) {
                    Text(text = stringResource(R.string.btn_create_category))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateCategoryScreenPreview() {
    WeQuizTheme {
        CreateCategoryScreen(
            onNavigationButtonClick = {},
            onCreateCategorySuccess = {},
        )
    }
}
