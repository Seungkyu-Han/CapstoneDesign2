package knu.capstoneDesign.application

import knu.capstoneDesign.data.dto.analysis.res.AnalysisGetMonthRes
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication

interface AnalysisService {
    fun get(diaryId: Int, authentication: Authentication): ResponseEntity<String>
    fun getMonth(year: Int, month: Int, authentication: Authentication): ResponseEntity<List<AnalysisGetMonthRes>>
}