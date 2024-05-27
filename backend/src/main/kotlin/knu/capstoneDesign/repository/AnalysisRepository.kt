package knu.capstoneDesign.repository

import knu.capstoneDesign.data.entity.Analysis
import knu.capstoneDesign.data.entity.Diary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.*

interface AnalysisRepository: JpaRepository<Analysis, Long> {

    @Transactional
    fun deleteByDiary(diary: Diary)
    fun findByDiary(diary: Diary): Optional<Analysis>

    @Query(
        "SELECT a FROM Analysis a " +
                "INNER JOIN a.diary d " +
                "INNER JOIN d.user u " +
                "WHERE u.id = :userId AND d.date BETWEEN :startDate and :endDate"
    )
    fun findByUserIdAndDateBetween(userId: Long, startDate: LocalDate, endDate: LocalDate): List<Analysis>
}