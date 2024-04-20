package knu.capstoneDesign.data.dto.diary.req

import java.time.LocalDate

data class DiaryPatchReq(
    val id:Int,
    val title: String,
    val content: String?,
    val date: LocalDate
)

