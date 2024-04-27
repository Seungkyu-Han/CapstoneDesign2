package knu.capstoneDesign.presentation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import knu.capstoneDesign.data.dto.diary.req.DiaryPostReq
import knu.capstoneDesign.application.authImpl.DiaryServiceAuthImpl
import knu.capstoneDesign.data.dto.diary.req.DiaryPatchReq
import knu.capstoneDesign.data.dto.diary.res.DiaryGetListRes
import knu.capstoneDesign.data.dto.diary.res.DiaryGetRes
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
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
class DiaryController(private val diaryService: DiaryServiceAuthImpl) {

    @PostMapping
    @Operation(summary = "일기 등록 API")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공", content = arrayOf(Content()))
    )
    fun post(@RequestBody diaryPostReq: DiaryPostReq, @Parameter(hidden = true) authentication: Authentication):ResponseEntity<HttpStatusCode>{
        return diaryService.post(diaryPostReq, authentication)
    }

    @GetMapping
    @Operation(summary = "일기 조회 API", description = "일단 구현해놨고, 안에 상담 추가 내용 필요할 거 같은거 말씀해주시면 나중에 추가할게요")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공", content = arrayOf(Content()))
    )
    fun get(@RequestParam id: Int, @Parameter(hidden = true) authentication: Authentication):ResponseEntity<DiaryGetRes>{
        return diaryService.get(id, authentication)
    }

    @PatchMapping
    @Operation(summary = "일기 수정 API")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공", content = arrayOf(Content()))
    )
    fun patch(@RequestBody diaryPatchReq: DiaryPatchReq, @Parameter(hidden = true) authentication: Authentication):ResponseEntity<HttpStatusCode>{
        return diaryService.patch(diaryPatchReq, authentication)
    }

    @DeleteMapping
    @Operation(summary = "일기 삭제 API")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공", content = arrayOf(Content()))
    )
    fun delete(@RequestParam id:Int, @Parameter(hidden = true) authentication: Authentication): ResponseEntity<HttpStatusCode>{
        return diaryService.delete(id, authentication)
    }

    @GetMapping("/list")
    @Operation(summary = "일기 리스트 조회 API")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공", content = arrayOf(Content()))
    )
    fun getList(@RequestParam startDate: LocalDate, @RequestParam endDate: LocalDate, @Parameter(hidden = true) authentication: Authentication): ResponseEntity<List<DiaryGetListRes>>{
        return diaryService.getList(startDate, endDate, authentication)
    }

    @GetMapping("/month")
    @Operation(summary = "일기 월별 리스트 조회 API")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공", content = arrayOf(Content()))
    )
    fun getMonth(@RequestParam year: Int, @RequestParam month: Int, @Parameter(hidden = true) authentication: Authentication): ResponseEntity<List<DiaryGetListRes>>{
        return diaryService.getMonth(year, month, authentication)
    }

    @GetMapping("/all")
    @Operation(summary = "해당 유저의 전체 일기 조회 API")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공", content = arrayOf(Content()))
    )
    fun getAll(@Parameter(hidden = true) authentication: Authentication): ResponseEntity<List<DiaryGetListRes>>{
        return diaryService.getAll(authentication)
    }

}