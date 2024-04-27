package knu.capstoneDesign.data.dto.diary.req

import java.time.LocalDate

data class DiaryPostReq(
    var userId:Long,
    val title:String?,
    val content: String?,
    val date: LocalDate
)
