package knu.capstoneDesign.repository.custom

import knu.capstoneDesign.data.dto.diary.res.DiaryGetListRes
import java.time.LocalDate

interface DiaryRepositoryCustom {

    fun findByUserIdAndDateBetween(userId: Int, startDate: LocalDate, endDate: LocalDate): List<DiaryGetListRes>
}