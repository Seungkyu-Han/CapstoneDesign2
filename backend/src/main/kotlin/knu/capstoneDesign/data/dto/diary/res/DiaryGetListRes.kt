package knu.capstoneDesign.data.dto.diary.res

import com.querydsl.core.annotations.QueryProjection

data class DiaryGetListRes @QueryProjection constructor(
    val content: String
)