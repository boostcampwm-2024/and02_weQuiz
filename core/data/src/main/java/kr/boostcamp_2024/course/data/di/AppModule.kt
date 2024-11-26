package kr.boostcamp_2024.course.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import kr.boostcamp_2024.course.data.BuildConfig
import kr.boostcamp_2024.course.data.network.AiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

// todo: encrypt data store
private val Context.weQuizDataStore: DataStore<Preferences> by preferencesDataStore("wequiz_datastore")

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideFirebaseFireStore() = Firebase.firestore

    @Provides
    fun provideFirebaseStorage() = Firebase.storage

    @Provides
    fun provideWeQuizDataStore(
        @ApplicationContext context: Context,
    ) = context.weQuizDataStore

    @Provides
    fun provideJson(): Json = Json { ignoreUnknownKeys = true }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(AddHeadInterceptor())
        .build()

    @Provides
    fun provideAiService(
        client: OkHttpClient,
        json: Json,
    ): AiService = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(client)
        .build()
        .create(AiService::class.java)
}
