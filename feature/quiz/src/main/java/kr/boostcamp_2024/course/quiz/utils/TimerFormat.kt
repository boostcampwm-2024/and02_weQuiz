package kr.boostcamp_2024.course.quiz.utils

fun timerFormat(totalSeconds: Int): String {
	val minutes = (totalSeconds / 60).toString().padStart(2, '0')
	val seconds = (totalSeconds % 60).toString().padStart(2, '0')
	return "$minutes:$seconds"
}
