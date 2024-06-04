package knu.capstoneDesign.repository

import knu.capstoneDesign.data.entity.Consulting
import knu.capstoneDesign.data.entity.Diary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface ConsultingRepository: JpaRepository<Consulting, Int> {

    fun findByDiary(diary: Diary): Optional<Consulting>

    @Transactional
    fun deleteByDiary(diary: Diary): Int
}