package knu.capstoneDesign.application.impl

import knu.capstoneDesign.data.entity.Diary
import knu.capstoneDesign.repository.AnalysisRepository
import knu.capstoneDesign.repository.DiaryRepository
import knu.capstoneDesign.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class AnalysisServiceImpl(
    private val userRepository: UserRepository,
    private val diaryRepository: DiaryRepository,
    private val analysisRepository: AnalysisRepository,
    @Value("\${ai.server}")
    private val aiServerUrl: String
) {

    fun get(diary: Diary): ResponseEntity<String>{
        val analysis = analysisRepository.findByDiary(diary).orElseThrow{NullPointerException()}
        return ResponseEntity.ok(analysis.emotion.name)
    }

}