package knu.capstoneDesign.data.dto.chatGPT.res

import java.time.LocalDateTime

data class ChatGPTGetConsultingRes(
    var id: Int,
    var localDateTime: LocalDateTime,
    var question: String,
    var answer: String
)
