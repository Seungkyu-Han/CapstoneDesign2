package knu.capstoneDesign.data.dto.diary.req

import java.time.LocalDate

data class DiaryPostReq(
    val userId:Int,
    val title:String?,
    val content: String?,
    val date: LocalDate
)
