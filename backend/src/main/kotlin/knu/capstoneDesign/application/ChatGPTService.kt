package knu.capstoneDesign.application

import knu.capstoneDesign.data.dto.chatGPT.res.ChatGPTDiaryRes
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication

interface ChatGPTService {
    fun get(content: String, authentication: Authentication): ResponseEntity<String>
    fun getDiary(diaryId: Int, authentication: Authentication): ResponseEntity<ChatGPTDiaryRes>
    fun getDiary(diaryId: Int, userId: Long): ResponseEntity<ChatGPTDiaryRes>
}