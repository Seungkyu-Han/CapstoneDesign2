package knu.capstoneDesign.application.impl

import knu.capstoneDesign.data.dto.analysis.res.AnalysisGetMonthRes
import knu.capstoneDesign.data.dto.analysis.res.AnalysisGetRes
import knu.capstoneDesign.data.entity.Diary
import knu.capstoneDesign.repository.AnalysisRepository
import knu.capstoneDesign.repository.DiaryRepository
import knu.capstoneDesign.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.YearMonth

@Service
class AnalysisServiceImpl(
    private val userRepository: UserRepository,
    private val diaryRepository: DiaryRepository,
    private val analysisRepository: AnalysisRepository,
    @Value("\${ai.server}")
    private val aiServerUrl: String
) {

    fun get(diary: Diary): ResponseEntity<AnalysisGetRes>{
        val analysis = analysisRepository.findByDiary(diary).orElseThrow{NullPointerException()}
        return ResponseEntity.ok(AnalysisGetRes(analysis))
    }

    fun getMonth(year: Int, month: Int, userId: Long): ResponseEntity<List<AnalysisGetMonthRes>>{
        val startDate = YearMonth.of(year, month).atDay(1)
        val endDate = YearMonth.of(year, month).atEndOfMonth()

        return ResponseEntity.ok(analysisRepository.findByUserIdAndDateBetween(userId, startDate, endDate)
            .map {analysis ->  AnalysisGetMonthRes(
                id = analysis.id, diaryId = analysis.diary.id, date = analysis.diary.date ?: LocalDate.now(), emotion = analysis.emotion, summary = analysis.summary)})
    }

}