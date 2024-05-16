package knu.capstoneDesign.repository

import knu.capstoneDesign.data.entity.Diary
import knu.capstoneDesign.data.entity.UserConsulting
import org.springframework.data.jpa.repository.JpaRepository

interface UserConsultingRepository: JpaRepository<UserConsulting, Int> {

    fun findByDiary(diary: Diary): List<UserConsulting>
}