package knu.capstoneDesign.application

import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication

interface AnalysisService {
    fun get(diaryId: Int, authentication: Authentication): ResponseEntity<String>
}