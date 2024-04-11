package knu.capstoneDesign.data.dto.diary.res

import java.time.LocalDate

data class DiaryGetRes(

    val id: Int,

    val date: LocalDate,
    val title: String,

    val content: String
)
