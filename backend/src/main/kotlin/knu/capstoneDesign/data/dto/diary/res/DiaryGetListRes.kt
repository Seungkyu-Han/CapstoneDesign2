package knu.capstoneDesign.data.dto.diary.res

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDate

data class DiaryGetListRes @QueryProjection constructor(
    val id: Int,
    val date: LocalDate,
    val title: String?,
    val content: String?
)
