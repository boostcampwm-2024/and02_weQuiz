package kr.boostcamp_2024.course.study.navigation

import androidx.annotation.StringRes
import kotlinx.serialization.Serializable
import kr.boostcamp_2024.course.study.R

@Serializable
sealed class DetailMainScreenRouter(
	val iconId: Int,
	@StringRes val title: Int,
)

@Serializable
data object DetailScreenRoute : DetailMainScreenRouter(R.drawable.baseline_dehaze_24, R.string.bottom_nav_icon_category)

@Serializable
data object GroupScreenRoute : DetailMainScreenRouter(
	R.drawable.rounded_directions_walk_24,
	R.string.bottom_nav_icon_group,
)
