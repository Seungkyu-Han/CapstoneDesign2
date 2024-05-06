package knu.capstoneDesign.application.authImpl

import knu.capstoneDesign.application.AnalysisService
import knu.capstoneDesign.application.impl.AnalysisServiceImpl
import knu.capstoneDesign.repository.AnalysisRepository
import knu.capstoneDesign.repository.DiaryRepository
import knu.capstoneDesign.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class AnalysisServiceAuthImpl(
    private val userRepository: UserRepository,
    private val diaryRepository: DiaryRepository,
    private val analysisRepository: AnalysisRepository,
    @Value("\${ai.server}")
    private val aiServerUrl: String
): AnalysisService, AnalysisServiceImpl(userRepository, diaryRepository, analysisRepository, aiServerUrl) {
    override fun get(diaryId: Int, authentication: Authentication): ResponseEntity<String> {
        val diary = diaryRepository.findById(diaryId).orElseThrow { NullPointerException() }
        val userId = authentication.name.toLong()
        if(diary.user.id != userId)
            throw NullPointerException()
        return super.get(diary, userId)
    }
}