package knu.capstoneDesign.data.dto.chatGPT.res

import knu.capstoneDesign.data.entity.UserConsulting
import java.time.LocalDateTime

data class ChatGPTPostConsultingRes(
    var id: Int,
    var question: String,
    var answer: String,
    var localDateTime: LocalDateTime
){
    constructor(userConsulting: UserConsulting): this(id = userConsulting.id?:0, question = userConsulting.question, answer = userConsulting.answer, localDateTime = userConsulting.localDateTime)
}
