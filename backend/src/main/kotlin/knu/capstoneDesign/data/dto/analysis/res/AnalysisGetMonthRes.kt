package knu.capstoneDesign.data.dto.analysis.res

import knu.capstoneDesign.data.enum.Emotion
import java.time.LocalDate

data class AnalysisGetMonthRes(
    val id: Long?,
    val diaryId: Int?,
    val date: LocalDate,
    val emotion: Emotion
)
