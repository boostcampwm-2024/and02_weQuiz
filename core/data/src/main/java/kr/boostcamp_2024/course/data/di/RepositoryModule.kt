package kr.boostcamp_2024.course.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.boostcamp_2024.course.data.repository.AuthRepositoryImpl
import kr.boostcamp_2024.course.data.repository.CategoryRepositoryImpl
import kr.boostcamp_2024.course.data.repository.NotificationRepositoryImpl
import kr.boostcamp_2024.course.data.repository.QuestionRepositoryImpl
import kr.boostcamp_2024.course.data.repository.QuizRepositoryImpl
import kr.boostcamp_2024.course.data.repository.StudyGroupRepositoryImpl
import kr.boostcamp_2024.course.data.repository.UserOmrRepositoryImpl
import kr.boostcamp_2024.course.data.repository.UserRepositoryImpl
import kr.boostcamp_2024.course.domain.repository.AuthRepository
import kr.boostcamp_2024.course.domain.repository.CategoryRepository
import kr.boostcamp_2024.course.domain.repository.NotificationRepository
import kr.boostcamp_2024.course.domain.repository.QuestionRepository
import kr.boostcamp_2024.course.domain.repository.QuizRepository
import kr.boostcamp_2024.course.domain.repository.StudyGroupRepository
import kr.boostcamp_2024.course.domain.repository.UserOmrRepository
import kr.boostcamp_2024.course.domain.repository.UserRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    abstract fun provideNotificationRepository(
        notificationRepositoryImpl: NotificationRepositoryImpl
    ): NotificationRepository

    @Binds
    abstract fun provideQuestionRepository(
        questionRepositoryImpl: QuestionRepositoryImpl
    ): QuestionRepository

    @Binds
    abstract fun provideQuizRepository(
        quizRepositoryImpl: QuizRepositoryImpl
    ): QuizRepository

    @Binds
    abstract fun provideStudyGroupRepository(
        studyGroupRepositoryImpl: StudyGroupRepositoryImpl
    ): StudyGroupRepository

    @Binds
    abstract fun provideUserOmrRepository(
        userOmrRepositoryImpl: UserOmrRepositoryImpl
    ): UserOmrRepository

    @Binds
    abstract fun provideUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    abstract fun provideAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}