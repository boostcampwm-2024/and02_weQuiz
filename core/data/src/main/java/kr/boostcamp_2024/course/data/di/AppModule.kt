package kr.boostcamp_2024.course.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

private val Context.weQuizDataStore: DataStore<Preferences> by preferencesDataStore("wequiz_datastore")

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideFirebaseFireStore() = Firebase.firestore

    @Provides
    fun provideWeQuizDataStore(@ApplicationContext context: Context) = context.weQuizDataStore
}