package knu.capstoneDesign.presentation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import knu.capstoneDesign.application.AnalysisService
import knu.capstoneDesign.data.dto.analysis.res.AnalysisGetMonthRes
import knu.capstoneDesign.data.dto.analysis.res.AnalysisGetRes
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/feeling")
@Tag(name = "감정")
class AnalysisController(
    @Qualifier("analysisServiceAuthImpl") private val analysisService: AnalysisService) {

    @GetMapping
    @Operation(summary = "감정 분석 조회 API")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공", content = arrayOf(Content()))
    )
    fun get(@RequestParam diaryId: Int, @Parameter(hidden = true) authentication: Authentication): ResponseEntity<AnalysisGetRes>{
        return analysisService.get(diaryId, authentication)
    }

    @GetMapping("/month")
    @Operation(summary = "월별 감정 분석 조회 API")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공", content = arrayOf(Content(schema = Schema(implementation = AnalysisGetMonthRes::class))))
    )
    fun getMonth(@RequestParam year: Int, @RequestParam month: Int, @Parameter(hidden = true) authentication: Authentication): ResponseEntity<List<AnalysisGetMonthRes>>{
        return analysisService.getMonth(year, month, authentication)
    }
}