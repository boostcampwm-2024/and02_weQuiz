package kr.boostcamp_2024.course.quiz.presentation.question

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate.VORDIPLOM_COLORS
import kr.boostcamp_2024.course.quiz.R

@Composable
fun PieChartScreen(userAnswers: List<Int>) {
    val totalInfo = mutableListOf<PieEntry>()
    val pieChartStrings: List<String> = listOf(
        stringResource(R.string.txt_label_pie_chart_1),
        stringResource(R.string.txt_label_pie_chart_2),
        stringResource(R.string.txt_label_pie_chart_3),
        stringResource(R.string.txt_label_pie_chart_4),
    )

    userAnswers.forEachIndexed { index, i ->
        if (i != 0) {
            totalInfo.add(PieEntry(i.toFloat(), pieChartStrings[index]))
        }
    }

    val pieDataSet = PieDataSet(totalInfo, "")
    pieDataSet.apply {
        val alpha = 128
        colors = VORDIPLOM_COLORS.toList().map { color ->
            (alpha shl 24) or (color and 0x00FFFFFF)
        }
        valueTextColor = Color.Black.toArgb()
        valueTextSize = 16f
        valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String =
                "${value.toInt()}"
        }
    }
    val pieData = PieData(pieDataSet)
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AndroidView(
            factory = { context ->
                PieChart(context).apply {
                    data = pieData
                    description.isEnabled = false // 설명 비활성화
                    centerText = context.getString(R.string.txt_pie_chart_center) // 중앙 텍스트 설정
                    setEntryLabelColor(Color.Black.toArgb()) // 항목 라벨 색상 설정
                    setEntryLabelTextSize(18f)
                    setCenterTextSize(18f) // 중앙 텍스트 크기 설정
                    legend.isEnabled = false // 범례 설정
                    isRotationEnabled = true
                    animateY(1400, Easing.EaseInOutQuad)
                    invalidate()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .padding(horizontal = 20.dp),
        )
    }
}
