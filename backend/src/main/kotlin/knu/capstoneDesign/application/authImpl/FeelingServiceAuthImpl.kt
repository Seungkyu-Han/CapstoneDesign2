package knu.capstoneDesign.application.authImpl

import knu.capstoneDesign.application.FeelingService
import knu.capstoneDesign.application.impl.FeelingServiceImpl
import knu.capstoneDesign.repository.DiaryRepository
import knu.capstoneDesign.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class FeelingServiceAuthImpl(
    private val userRepository: UserRepository,
    private val diaryRepository: DiaryRepository
): FeelingService, FeelingServiceImpl(userRepository, diaryRepository) {
}