package knu.capstoneDesign.presentation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import knu.capstoneDesign.application.ChatGPTService
import knu.capstoneDesign.data.dto.chatGPT.req.ChatGPTPostConsultingReq
import knu.capstoneDesign.data.dto.chatGPT.res.ChatGPTDiaryRes
import knu.capstoneDesign.data.dto.chatGPT.res.ChatGPTGetConsultingRes
import knu.capstoneDesign.data.dto.chatGPT.res.ChatGPTPostConsultingRes
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chatGPT")
@Tag(name="chatGPT")
class ChatGPTController(private val chatGPTService: ChatGPTService) {

    @GetMapping
    @Operation(summary = "Just Chat with ChatGPT")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공", content= arrayOf(Content()))
    )
    fun get(@RequestParam content: String, @Parameter(hidden = true) authentication: Authentication): ResponseEntity<String>{
        return chatGPTService.get(content, authentication)
    }

    @GetMapping("/diary")
    @Operation(summary = "일기 분석 API")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공", content = arrayOf(Content(schema = Schema(implementation = ChatGPTDiaryRes::class))))
    )
    fun getDiary(@RequestParam diaryId: Int, @Parameter(hidden = true) authentication: Authentication): ResponseEntity<ChatGPTDiaryRes>{
        return chatGPTService.getDiary(diaryId, authentication)
    }

    @PostMapping("/consult")
    @Operation(summary = "유저 상담 API")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공", content = arrayOf(Content(schema = Schema(implementation = ChatGPTPostConsultingRes::class))))
    )
    fun postConsult(@RequestBody chatGPTPostConsultingReq: ChatGPTPostConsultingReq, @Parameter(hidden = true) authentication: Authentication): ResponseEntity<ChatGPTPostConsultingRes>{
        return chatGPTService.postConsult(chatGPTPostConsultingReq, authentication)
    }

    @GetMapping("/consult")
    @Operation(summary = "유저 상담 조회 API")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공", content = arrayOf(Content(schema = Schema(implementation = ChatGPTGetConsultingRes::class))))
    )
    fun getConsult(@RequestParam diaryId: Int, @Parameter(hidden = true) authentication: Authentication): ResponseEntity<List<ChatGPTGetConsultingRes>> {
        return chatGPTService.getConsult(diaryId, authentication)
    }

    @DeleteMapping("/consult")
    @Operation(summary = "유저 상담 삭제 API")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공")
    )
    fun deleteConsult(@RequestParam userConsultingId: Int, @Parameter(hidden = true) authentication: Authentication): ResponseEntity<HttpStatus>{
        return chatGPTService.deleteConsult(userConsultingId, authentication)
    }
}