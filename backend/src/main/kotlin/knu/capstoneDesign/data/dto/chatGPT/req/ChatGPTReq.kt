package knu.capstoneDesign.data.dto.chatGPT.req

import com.fasterxml.jackson.annotation.JsonProperty

data class ChatGPTReq(
    @JsonProperty("content") val content: String,
    @JsonProperty("user_id") val user_id: Long,
    @JsonProperty("signnum") val signnum: Int
)
