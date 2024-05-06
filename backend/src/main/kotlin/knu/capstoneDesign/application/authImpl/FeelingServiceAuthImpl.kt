package knu.capstoneDesign.application.authImpl

import knu.capstoneDesign.application.AnalysisService
import knu.capstoneDesign.application.impl.AnalysisServiceImpl
import knu.capstoneDesign.repository.DiaryRepository
import knu.capstoneDesign.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class FeelingServiceAuthImpl(
    private val userRepository: UserRepository,
    private val diaryRepository: DiaryRepository
): AnalysisService, AnalysisServiceImpl(userRepository, diaryRepository) {
    override fun get(diaryId: Int, authentication: Authentication): ResponseEntity<String> {
        TODO("Not yet implemented")
    }
}