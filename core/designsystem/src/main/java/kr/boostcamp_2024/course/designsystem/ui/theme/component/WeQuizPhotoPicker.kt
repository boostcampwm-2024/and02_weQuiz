import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.designsystem.R
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizAsyncImage
import java.io.ByteArrayOutputStream

@Composable
fun WeQuizPhotoPickerAsyncImage(
    imageData: Any?,
    onImageDataChanged: (ByteArray) -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    placeholder: Painter = painterResource(R.drawable.img_photo_picker),
    error: Painter = painterResource(R.drawable.img_photo_picker),
    fallback: Painter = painterResource(R.drawable.img_photo_picker),
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val photoPickerLauncher = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            coroutineScope.launch {
                val inputStream = context.contentResolver.openInputStream(uri) ?: return@launch
                val bitmap = BitmapFactory.decodeStream(inputStream)

                val newInputStream = context.contentResolver.openInputStream(uri) ?: return@launch
                val exifInterface = ExifInterface(newInputStream)

                val rotatedBitmap = when (exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, Matrix().apply { postRotate(90f) }, true)
                    ExifInterface.ORIENTATION_ROTATE_180 -> Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, Matrix().apply { postRotate(180f) }, true)
                    ExifInterface.ORIENTATION_ROTATE_270 -> Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, Matrix().apply { postRotate(270f) }, true)
                    else -> bitmap
                }

                val baos = ByteArrayOutputStream()

                val resizedBitmap =
                    if (rotatedBitmap.width > 2560) {
                        val scaleFactor = 2560f / rotatedBitmap.width
                        val newWidth = 2560
                        val newHeight = (rotatedBitmap.height * scaleFactor).toInt()

                        Bitmap.createScaledBitmap(rotatedBitmap, newWidth, newHeight, true)
                    } else {
                        bitmap
                    }

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                    resizedBitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, 60, baos)
                } else {
                    resizedBitmap.compress(Bitmap.CompressFormat.WEBP, 60, baos)
                }

                val data = baos.toByteArray()
                onImageDataChanged(data)

                inputStream.close()
                newInputStream.close()
                bitmap.recycle()
                rotatedBitmap.recycle()
                resizedBitmap.recycle()
            }
        }
    }

    WeQuizAsyncImage(
        modifier = modifier
            .clickable(enabled = true) {
                photoPickerLauncher.launch(PickVisualMediaRequest(ImageOnly))
            },
        imgUrl = imageData,
        contentDescription = contentDescription,
        placeholder = placeholder,
        error = error,
        fallback = fallback,
    )
}

@Preview(showBackground = true)
@Composable
private fun WeQuizPhotoPickerAsyncImagePreview() {
    WeQuizTheme {
        WeQuizPhotoPickerAsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 70.dp)
                .aspectRatio(1f)
                .clip(shape = MaterialTheme.shapes.large),
            imageData = ByteArray(0),
            onImageDataChanged = {},
        )
    }
}
