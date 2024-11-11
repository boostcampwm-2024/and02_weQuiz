package kr.boostcamp_2024.course.wequiz

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeQuizApplication : Application() {
	override fun onCreate() {
		super.onCreate()
		FirebaseApp.initializeApp(this)
	}
}
