package knu.capstoneDesign.application.authImpl

import knu.capstoneDesign.application.AnalysisService
import knu.capstoneDesign.application.impl.AnalysisServiceImpl
import knu.capstoneDesign.data.dto.analysis.res.AnalysisGetMonthRes
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
        if(diary.user.id != authentication.name.toLong())
            throw NullPointerException()
        return super.get(diary)
    }

    override fun getMonth(year: Int, month: Int, authentication: Authentication): ResponseEntity<List<AnalysisGetMonthRes>> {
        return super.getMonth(year, month, authentication.name.toLong())
    }
}