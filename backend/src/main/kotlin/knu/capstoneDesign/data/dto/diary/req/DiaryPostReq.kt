package knu.capstoneDesign.data.dto.diary.req

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

data class DiaryPostReq(
    @Schema(hidden = true)
    var userId:Long,
    val title:String?,
    val content: String?,
    val date: LocalDate
)
