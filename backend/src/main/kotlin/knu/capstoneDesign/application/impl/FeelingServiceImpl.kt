package knu.capstoneDesign.application.impl

import knu.capstoneDesign.repository.DiaryRepository
import knu.capstoneDesign.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class FeelingServiceImpl(
    private val userRepository: UserRepository,
    private val diaryRepository: DiaryRepository
) {
}