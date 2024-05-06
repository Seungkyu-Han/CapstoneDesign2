package knu.capstoneDesign.application.impl

import knu.capstoneDesign.repository.DiaryRepository
import knu.capstoneDesign.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
open class AnalysisServiceImpl(
    private val userRepository: UserRepository,
    private val diaryRepository: DiaryRepository
) {

    fun get(diaryId: Int, userId: Long):ResponseEntity<String>{




        return ResponseEntity.ok("a")
    }
}