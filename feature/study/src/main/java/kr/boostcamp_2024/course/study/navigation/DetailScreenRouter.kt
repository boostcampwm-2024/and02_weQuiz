package kr.boostcamp_2024.course.study.navigation

import kotlinx.serialization.Serializable
import kr.boostcamp_2024.course.study.R

@Serializable
sealed class DetailMainScreenRouter(val iconId: Int, val title: String)

@Serializable
data object DetailScreenRoute : DetailMainScreenRouter(R.drawable.baseline_dehaze_24, "카테고리")

@Serializable
data object GroupScreenRoute : DetailMainScreenRouter(R.drawable.rounded_directions_walk_24, "그룹원")
