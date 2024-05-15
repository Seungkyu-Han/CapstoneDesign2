package knu.capstoneDesign.repository

import knu.capstoneDesign.data.entity.UserConsulting
import org.springframework.data.jpa.repository.JpaRepository

interface UserConsultingRepository: JpaRepository<UserConsulting, Int> {
}