package knu.capstoneDesign.presentation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import knu.capstoneDesign.application.ChatGPTService
import knu.capstoneDesign.data.dto.chatGPT.res.ChatGPTDiaryRes
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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
        ApiResponse(responseCode = "200", description = "성공")
    )
    fun getDiary(@RequestParam diaryId: Int, @Parameter(hidden = true) authentication: Authentication): ResponseEntity<ChatGPTDiaryRes>{
        return chatGPTService.getDiary(diaryId, authentication)
    }
}