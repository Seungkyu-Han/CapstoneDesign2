package knu.capstoneDesign.repository

import knu.capstoneDesign.data.entity.Analysis
import knu.capstoneDesign.data.entity.Diary
import org.springframework.data.jpa.repository.JpaRepository

interface AnalysisRepository: JpaRepository<Analysis, Long> {

    fun deleteByDiary(diary: Diary)
}