package knu.capstoneDesign.repository

import knu.capstoneDesign.data.entity.Diary
import knu.capstoneDesign.data.entity.User
import knu.capstoneDesign.repository.custom.DiaryRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*

@Repository
interface DiaryRepository: JpaRepository<Diary, Int>, DiaryRepositoryCustom {
    fun findByUserAndDate(user: User, date: LocalDate): Optional<Diary>
    fun deleteByUserAndDate(user: User, date: LocalDate): Int
    fun existsByIdAndUser(id: Int, user: User): Boolean
}