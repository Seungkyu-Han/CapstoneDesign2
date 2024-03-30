package knu.capstoneDesign.presentation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import knu.capstoneDesign.data.dto.diary.req.DiaryPostReq
import knu.capstoneDesign.application.DiaryService
import knu.capstoneDesign.data.dto.diary.res.DiaryGetRes
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/diary")
@Tag(name = "일기")
class DiaryController(private val diaryService: DiaryService) {

    @PostMapping
    @Operation(summary = "일기 등록 API")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공", content = arrayOf(Content()))
    )
    fun post(@RequestBody diaryPostReq: DiaryPostReq):ResponseEntity<HttpStatusCode>{
        return diaryService.post(diaryPostReq)
    }

    @GetMapping
    @Operation(summary = "일기 조회 API", description = "일단 구현해놨고, 안에 상담 추가 내용 필요할 거 같은거 말씀해주시면 나중에 추가할게요")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공", content = arrayOf(Content()))
    )
    fun get(@RequestParam userId: Int, @RequestParam date: LocalDate):ResponseEntity<DiaryGetRes>{
        return diaryService.get(userId, date)
    }

    @PatchMapping
    @Operation(summary = "일기 수정 API")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공", content = arrayOf(Content()))
    )
    fun patch(@RequestBody diaryPostReq: DiaryPostReq):ResponseEntity<HttpStatusCode>{
        return diaryService.patch(diaryPostReq)
    }
}