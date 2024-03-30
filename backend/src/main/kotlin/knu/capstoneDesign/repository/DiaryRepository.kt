package knu.capstoneDesign.repository

import knu.capstoneDesign.data.entity.Diary
import knu.capstoneDesign.data.entity.User
import knu.capstoneDesign.repository.custom.DiaryRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface DiaryRepository: JpaRepository<Diary, Int>, DiaryRepositoryCustom {
    fun findTopByOrderByIdDesc():Diary
    fun findByUserAndDate(user: User, date: LocalDate): Diary
    fun deleteByUserAndDate(user: User, date: LocalDate)
}