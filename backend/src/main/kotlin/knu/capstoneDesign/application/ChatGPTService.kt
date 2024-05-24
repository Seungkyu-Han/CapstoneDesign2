package knu.capstoneDesign.application

import knu.capstoneDesign.data.dto.chatGPT.req.ChatGPTPostConsultingReq
import knu.capstoneDesign.data.dto.chatGPT.res.ChatGPTDiaryRes
import knu.capstoneDesign.data.dto.chatGPT.res.ChatGPTGetConsultingRes
import knu.capstoneDesign.data.dto.chatGPT.res.ChatGPTPostConsultingRes
import knu.capstoneDesign.data.entity.Diary
import knu.capstoneDesign.data.entity.UserConsulting
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication

interface ChatGPTService {
    fun getDiary(diaryId: Int, authentication: Authentication): ResponseEntity<ChatGPTDiaryRes>
    fun getDiary(diaryId: Int, userId: Long): ResponseEntity<ChatGPTDiaryRes>
    fun postConsult(chatGPTPostConsultingReq: ChatGPTPostConsultingReq, authentication: Authentication): ResponseEntity<ChatGPTPostConsultingRes>
    fun getConsult(diaryId: Int, authentication: Authentication): ResponseEntity<List<ChatGPTGetConsultingRes>>
    fun getConsult(diary: Diary): ResponseEntity<List<ChatGPTGetConsultingRes>>
    fun deleteConsult(userConsultingId: Int, authentication: Authentication): ResponseEntity<HttpStatus>
    fun deleteConsult(userConsulting: UserConsulting): ResponseEntity<HttpStatus>
}