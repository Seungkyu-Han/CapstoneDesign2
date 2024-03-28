package knu.capstoneDesign.presentation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import knu.capstoneDesign.data.dto.diary.DiaryPostReq
import knu.capstoneDesign.application.DiaryService
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/diary")
@Tag(name = "일기")
class DiaryController(private val diaryService: DiaryService) {

    @PostMapping
    @Operation(summary = "일기 등록 API")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공", content = arrayOf(Content()))
    )
    fun post(@RequestBody diaryPostReq:DiaryPostReq):ResponseEntity<HttpStatusCode>{
        return diaryService.post(diaryPostReq)
    }
}