package knu.capstoneDesign.repository.custom

import knu.capstoneDesign.data.dto.diary.res.DiaryGetListRes
import java.time.LocalDate

interface DiaryRepositoryCustom {

    fun findByUserIdAndDateBetween(userId: Long, startDate: LocalDate, endDate: LocalDate): List<DiaryGetListRes>
    fun findByUserId(userId: Long): List<DiaryGetListRes>
}