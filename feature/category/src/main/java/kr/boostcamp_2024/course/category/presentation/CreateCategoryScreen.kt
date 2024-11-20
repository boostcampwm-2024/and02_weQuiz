package kr.boostcamp_2024.course.category.presentation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.boostcamp_2024.course.category.R
import kr.boostcamp_2024.course.category.viewModel.CreateCategoryViewModel
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizAsyncImage
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizTextField
import java.io.ByteArrayOutputStream

@Composable
fun CreateCategoryScreen(
    onNavigationButtonClick: () -> Unit,
    onCreateCategorySuccess: () -> Unit,
    viewModel: CreateCategoryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.createCategoryUiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(uiState) {
        if (uiState.creationSuccess) {
            onCreateCategorySuccess()
        }
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.setErrorMessage(null)
        }
    }

    val guideText = if (viewModel.categoryId == null) {
        stringResource(R.string.txt_create_category)
    } else {
        stringResource(R.string.txt_edit_category)
    }

    CreateCategoryScreen(
        name = uiState.categoryName,
        description = uiState.categoryDescription,
        currentCategoryImage = uiState.currentImage,
        defaultCategoryImageUri = uiState.defaultImageUri,
        isCategoryCreationValid = uiState.isCategoryCreationValid,
        snackbarHostState = snackbarHostState,
        onNameChanged = viewModel::onNameChanged,
        onDescriptionChanged = viewModel::onDescriptionChanged,
        onNavigationButtonClick = onNavigationButtonClick,
        onCreateCategoryButtonClick = viewModel::uploadCategory,
        guideText = guideText,
        onCurrentCategoryImageChanged = viewModel::onImageByteArrayChanged,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCategoryScreen(
    name: String,
    description: String,
    currentCategoryImage: ByteArray?,
    defaultCategoryImageUri: String?,
    isCategoryCreationValid: Boolean,
    snackbarHostState: SnackbarHostState,
    onNameChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onNavigationButtonClick: () -> Unit,
    onCreateCategoryButtonClick: () -> Unit,
    guideText: String,
    onCurrentCategoryImageChanged: (ByteArray) -> Unit,
) {
    val context = LocalContext.current
    val photoPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
            val data = baos.toByteArray()

            onCurrentCategoryImageChanged(data)
        }
    }
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
            WeQuizAsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 70.dp)
                    .aspectRatio(1f)
                    .clip(shape = MaterialTheme.shapes.large)
                    .clickable(enabled = true) {
                        photoPickerLauncher.launch(PickVisualMediaRequest(ImageOnly))
                    },
                imgUrl = currentCategoryImage ?: defaultCategoryImageUri,
                contentDescription = stringResource(R.string.des_category_image),
                placeholder = painterResource(R.drawable.default_profile_image),
                error = painterResource(R.drawable.default_profile_image),
                fallback = painterResource(R.drawable.default_profile_image),
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
                    Text(text = guideText)
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
