package knu.capstoneDesign.repository

import knu.capstoneDesign.data.entity.Diary
import knu.capstoneDesign.repository.custom.DiaryRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository

interface DiaryRepository: JpaRepository<Diary, Int>, DiaryRepositoryCustom {
}