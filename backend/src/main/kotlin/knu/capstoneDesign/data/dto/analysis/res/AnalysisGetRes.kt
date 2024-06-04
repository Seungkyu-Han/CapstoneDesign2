package knu.capstoneDesign.data.dto.analysis.res

import knu.capstoneDesign.data.entity.Analysis
import knu.capstoneDesign.data.enum.Emotion
import java.time.LocalDate

data class AnalysisGetRes (
    val id: Long?,
    val diaryId: Int?,
    val date: LocalDate,
    val emotion: Emotion,
    val summary: String
){
    constructor(analysis: Analysis): this(
        id = analysis.id, diaryId = analysis.diary.id, date = analysis.diary.date ?: LocalDate.now(), emotion = analysis.emotion, summary = analysis.summary
    )
}